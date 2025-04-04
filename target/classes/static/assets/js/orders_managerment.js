



// lọc theo status
document.addEventListener("DOMContentLoaded", function () {
    const filterButtons = document.querySelectorAll(".order_filter");
    const orderRows = document.querySelectorAll("#order_table-body tr");
    const emptyState = document.getElementById("order_empty-state");

    filterButtons.forEach((button) => {
        button.addEventListener("click", function () {
            filterButtons.forEach((btn) => btn.classList.remove("order_active"));
            this.classList.add("order_active");
            const filterValue = this.getAttribute("data-filter");
            let visibleCount = 0;
            orderRows.forEach((row) => {
                const statusCell = row.querySelector("td span");
                const statusText = statusCell ? statusCell.innerText.trim().toUpperCase() : "";
                if (filterValue === "all" || statusText === filterValue.toUpperCase()) {
                    row.style.display = "";
                    visibleCount++;
                } else {
                    row.style.display = "none";
                }
            });
            emptyState.style.display = visibleCount === 0 ? "block" : "none";
        });
    });
});
// lọc theo mã đơn, tên, ngày tháng năm
document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("order_search-input");
    const searchDateInput = document.getElementById("order_search-date");
    const orderRows = document.querySelectorAll("#order_table-body tr");
    const emptyState = document.getElementById("order_empty-state");

    function filterOrders() {
        const searchText = searchInput.value.trim().toLowerCase();
        const searchDate = searchDateInput.value;
        let visibleCount = 0;

        orderRows.forEach((row) => {
            const orderId = row.cells[0].innerText.trim().toLowerCase();
            const customerName = row.cells[1].innerText.trim().toLowerCase();
            const orderDateTime = row.cells[2].innerText.trim();

            const orderDateParts = orderDateTime.split(" ")[0].split("/");
            const formattedOrderDate = `${orderDateParts[2]}-${orderDateParts[1]}-${orderDateParts[0]}`;

            const matchesSearch =
                orderId.includes(searchText) || customerName.includes(searchText);
            const matchesDate = searchDate === "" || formattedOrderDate === searchDate;

            if (matchesSearch && matchesDate) {
                row.style.display = "";
                visibleCount++;
            } else {
                row.style.display = "none";
            }
        });

        emptyState.style.display = visibleCount === 0 ? "block" : "none";
    }

    searchInput.addEventListener("input", filterOrders);
    searchDateInput.addEventListener("change", filterOrders);
});

// modal chi tiết đơn hàng

function viewOrder(button) {
    let orderId = button.getAttribute("data-id");

    fetch(`/admin/orders/${orderId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Không thể lấy thông tin đơn hàng');
            }
            return response.json();
        })
        .then(order => {
            let orderDate = new Date(order.orderDate);
            orderDate.setHours(orderDate.getHours() - 7);


            let formattedDate = orderDate.toLocaleString('vi-VN', { timeZone: 'Asia/Ho_Chi_Minh' });


            let totalAmount = order.totalAmount.toLocaleString('vi-VN');
            let finalAmount = order.finalAmount.toLocaleString('vi-VN');
            document.getElementById("orders_detailModal_orderId").innerText = order.orderId;
            document.getElementById("orders_detailModal_userId").innerText = order.userId;
            document.getElementById("orders_detailModal_orderDate").innerText = formattedDate;
            document.getElementById("orders_detailModal_totalAmount").innerText = totalAmount;
            document.getElementById("orders_detailModal_finalAmount").innerText = finalAmount;
            document.getElementById("orders_detailModal_orderStatus").innerText = order.orderStatus;

            fetch(`/admin/orders/${orderId}/details`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Không thể lấy chi tiết đơn hàng');
                    }
                    return response.json();
                })
                .then(details => {
                    let detailsList = document.getElementById("orders_detailModal_orderDetails");
                    detailsList.innerHTML = "";
                    details.forEach(detail => {
                        let li = document.createElement("li");
                        li.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");
                        li.innerHTML = `
                            <div class="product-info">
                                <span class="product-id"><strong>Sản phẩm ID:</strong> ${detail.productId}</span>
                                <span class="quantity"><strong>Số lượng:</strong> ${detail.quantity}</span>
                                <span class="unit-price"><strong>Giá:</strong> ${detail.unitPrice.toLocaleString('vi-VN')}</span>
                                <span class="total-price"><strong>Tổng:</strong> ${detail.totalPrice.toLocaleString('vi-VN')}</span>
                            </div>
                        `;
                        detailsList.appendChild(li);
                    });
                })
                .catch(error => {
                    console.error("Lỗi khi tải chi tiết sản phẩm:", error);
                    alert("Không thể tải danh sách sản phẩm!");
                });

            document.getElementById("orders_detailModal_orderModal").style.display = "block";
        })
        .catch(error => {
            console.error("Lỗi khi tải đơn hàng:", error);
            alert("Không tìm thấy đơn hàng!");
        });
}


document.querySelector(".orders_detailModal_close").addEventListener("click", function() {
    document.getElementById("orders_detailModal_orderModal").style.display = "none";
});

window.addEventListener("click", function(event) {
    let modal = document.getElementById("orders_detailModal_orderModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
});

// hàm duyệt đơn hàng
document.getElementById("orders_confirmApprove").addEventListener("click", function () {
    if (!orders_currentOrderId) return;

    fetch(`/admin/orders/${orders_currentOrderId}/approve`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Duyệt đơn hàng thất bại!");
            }
            return response.text();
        })
        .then(message => {
            console.log("Phản hồi từ server:", message);

            let modal = document.getElementById("orders_confirmModal");
            modal.classList.remove("show");
            setTimeout(() => modal.style.zIndex = "-10", 300);

            let button = document.querySelector(`[data-id="${orders_currentOrderId}"]`);
            if (button) {
                button.disabled = true;
                button.innerHTML = '<i class="fas fa-check-circle"></i> Đã duyệt';
            }

            let statusElement = document.querySelector(`#orders_order-status-${orders_currentOrderId}`);
            if (statusElement) {
                statusElement.textContent = "COMPLETED";
                statusElement.className = "order_status-confirmed";
            }
            showSuccessMessage(message);
            setTimeout(() => {
                location.reload();
            }, 2000);
        })
        .catch(error => {
            console.error("Lỗi khi cập nhật trạng thái:", error);
            alert("Có lỗi xảy ra, vui lòng thử lại!");
        });
});


document.getElementById("orders_cancelApprove").addEventListener("click", function () {
    let modal = document.getElementById("orders_confirmModal");
    modal.classList.remove("show");
});
function approveOrder(button) {
    orders_currentOrderId = button.getAttribute("data-id");

    if (!orders_currentOrderId) {
        alert("Không tìm thấy ID đơn hàng!");
        return;
    }

    let modal = document.getElementById("orders_confirmModal");
    modal.classList.add("show");
}
// hàm hủy
document.getElementById("orders_confirmCancel").addEventListener("click", function () {
    if (!orders_currentOrderId) return;

    fetch(`/admin/orders/${orders_currentOrderId}/cancel`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error("Hủy đơn hàng thất bại!");
            }
            return response.text();
        })
        .then(message => {
            console.log("Phản hồi từ server:", message);

            let modal = document.getElementById("orders_cancelModal");
            modal.classList.remove("show");
            setTimeout(() => modal.style.zIndex = "-10", 300);

            let button = document.querySelector(`[data-id="${orders_currentOrderId}"]`);
            if (button) {
                button.disabled = true;
                button.innerHTML = '<i class="fas fa-times-circle"></i> Đã hủy';
            }
            let statusElement = document.querySelector(`#orders_order-status-${orders_currentOrderId}`);
            if (statusElement) {
                statusElement.textContent = "CANCELLED";
                statusElement.className = "order_status-cancelled";
            }
            showSuccessMessage("Đơn hàng đã được hủy thành công!");
            setTimeout(() => {
                location.reload();
            }, 2000);
        })
        .catch(error => {
            console.error("Lỗi khi cập nhật trạng thái:", error);
            alert("Có lỗi xảy ra, vui lòng thử lại!");
        });
});

function cancelOrder(button) {
    orders_currentOrderId = button.getAttribute("data-id");

    if (!orders_currentOrderId) {
        alert("Không tìm thấy ID đơn hàng!");
        return;
    }
    let modal = document.getElementById("orders_cancelModal");
    modal.classList.add("show");
}

document.getElementById("orders_cancelCancel").addEventListener("click", function () {
    let modal = document.getElementById("orders_cancelModal");
    modal.classList.remove("show");
});

// thông báo
function showSuccessMessage(message) {
    let successMessage = document.createElement('div');
    successMessage.classList.add('success-message');
    successMessage.textContent = message;
    document.body.appendChild(successMessage);
    setTimeout(() => {
        successMessage.style.opacity = 0;
        setTimeout(() => {
            successMessage.remove();
        }, 500);
    }, 2000);
}
// thống kê tiền giảm giá
// document.getElementById("statisticForm").addEventListener("submit", function(e) {
//     e.preventDefault();
//     const formData = new FormData(this);
//     fetch("/admin", {
//         method: "POST",
//         body: formData
//     })
//         .then(response => response.json())
//         .then(data => {
//             document.querySelector(".amount_statistic_totalAmount").innerHTML = data.totalDiscountFormatted + " VNĐ";
//         })
//         .catch(error => {
//             console.error('Có lỗi xảy ra:', error);
//         });
// });

// thống kê doanh thu thực tế
document.getElementById('statisticForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn load lại trang
    const formData = new FormData(this);

    // Gửi yêu cầu POST tới controller
    fetch('/admin', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // Cập nhật tổng số tiền đã giảm giá
            console.log(data.totalDiscount)
            if (data.totalDiscount !== undefined && data.totalDiscount !== null) {
                document.getElementById('totalDiscount').innerText =   (data.totalDiscount ?? 0).toLocaleString('vi-VN') + ' VNĐ';
            }
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('statisticFinalAmountForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Ngăn chặn load lại trang
    const formData = new FormData(this);

    // Gửi yêu cầu POST tới controller
    fetch('/admin', {
        method: 'POST',
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            // Cập nhật tổng doanh thu thực tế
            if (data.totalRevenue !== undefined && data.totalRevenue !== null) {
                document.getElementById('totalRevenue').innerText =   (data.totalRevenue ?? 0).toLocaleString('vi-VN') + ' VNĐ';
            }
        })
        .catch(error => console.error('Error:', error));
});
