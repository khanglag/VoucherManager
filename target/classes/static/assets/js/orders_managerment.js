// // Sample data for orders
// const orders = [
//     {
//         id: "DH001",
//         customer: "Nguyễn Văn A",
//         date: "25/03/2024",
//         total: "9,500,000 ₫",
//         status: "pending",
//         statusText: "Chờ xác nhận",
//     },
//     {
//         id: "DH002",
//         customer: "Trần Thị B",
//         date: "24/03/2024",
//         total: "15,800,000 ₫",
//         status: "confirmed",
//         statusText: "Đã xác nhận",
//     },
//     {
//         id: "DH003",
//         customer: "Lê Văn C",
//         date: "23/03/2024",
//         total: "7,200,000 ₫",
//         status: "shipping",
//         statusText: "Đang giao",
//     },
//     {
//         id: "DH004",
//         customer: "Phạm Thị D",
//         date: "22/03/2024",
//         total: "12,300,000 ₫",
//         status: "delivered",
//         statusText: "Đã giao",
//     },
//     {
//         id: "DH005",
//         customer: "Hoàng Văn E",
//         date: "20/03/2024",
//         total: "5,600,000 ₫",
//         status: "cancelled",
//         statusText: "Đã hủy",
//     },
//     {
//         id: "DH006",
//         customer: "Đỗ Thị F",
//         date: "19/03/2024",
//         total: "8,900,000 ₫",
//         status: "delivered",
//         statusText: "Đã giao",
//     },
//     {
//         id: "DH007",
//         customer: "Vũ Văn G",
//         date: "18/03/2024",
//         total: "10,500,000 ₫",
//         status: "delivered",
//         statusText: "Đã giao",
//     },
// ];
//
// // DOM Elements
// const tableBody = document.getElementById("order_table-body");
// const emptyState = document.getElementById("order_empty-state");
// const modal = document.getElementById("order_modal");
// const modalTitle = document.getElementById("order_modal-title");
// const addBtn = document.getElementById("order_add-btn");
// const createFirstBtn = document.getElementById("order_create-first");
// const modalCloseBtn = document.getElementById("order_modal-close");
// const cancelBtn = document.getElementById("order_cancel-btn");
// const orderForm = document.getElementById("order_form");
// const filterBtns = document.querySelectorAll(".order_filter");
// const searchInput = document.getElementById("order_search-input");
//
// // Current filter
// let currentFilter = "all";
// let searchTerm = "";
//
// // Function to render orders
// function renderOrders() {
//     // Filter orders
//     let filteredOrders = orders;
//
//     if (currentFilter !== "all") {
//         filteredOrders = orders.filter(
//             (order) => order.status === currentFilter
//         );
//     }
//
//     // Apply search
//     if (searchTerm) {
//         filteredOrders = filteredOrders.filter(
//             (order) =>
//                 order.id.toLowerCase().includes(searchTerm.toLowerCase()) ||
//                 order.customer.toLowerCase().includes(searchTerm.toLowerCase())
//         );
//     }
//
//     // Clear table
//     tableBody.innerHTML = "";
//
//     // Show empty state if no orders
//     if (filteredOrders.length === 0) {
//         emptyState.style.display = "block";
//         return;
//     }
//
//     // Hide empty state
//     emptyState.style.display = "none";
//
//     // Render orders
//     filteredOrders.forEach((order) => {
//         const row = document.createElement("tr");
//
//         row.innerHTML = `
//                     <td>${order.id}</td>
//                     <td>${order.customer}</td>
//                     <td>${order.date}</td>
//                     <td>${order.total}</td>
//                     <td><span class="order_status order_status-${order.status}">${order.statusText}</span></td>
//                     <td>
//                         <button class="order_action-btn order_view-btn" data-id="${order.id}">Xem</button>
//                         <button class="order_action-btn order_edit-btn" data-id="${order.id}">Sửa</button>
//                         <button class="order_action-btn order_delete-btn" data-id="${order.id}">Xóa</button>
//                     </td>
//                 `;
//
//         tableBody.appendChild(row);
//     });
//
//     // Add event listeners for action buttons
//     document.querySelectorAll(".order_view-btn").forEach((btn) => {
//         btn.addEventListener("click", viewOrder);
//     });
//
//     document.querySelectorAll(".order_edit-btn").forEach((btn) => {
//         btn.addEventListener("click", editOrder);
//     });
//
//     document.querySelectorAll(".order_delete-btn").forEach((btn) => {
//         btn.addEventListener("click", deleteOrder);
//     });
// }
//
// // Filter click handler
// filterBtns.forEach((btn) => {
//     btn.addEventListener("click", function () {
//         // Remove active class from all filter buttons
//         filterBtns.forEach((btn) => btn.classList.remove("order_active"));
//
//         // Add active class to clicked button
//         this.classList.add("order_active");
//
//         // Update current filter
//         currentFilter = this.dataset.filter;
//
//         // Re-render orders
//         renderOrders();
//     });
// });
//
// // Search input handler
// searchInput.addEventListener("input", function () {
//     searchTerm = this.value;
//     renderOrders();
// });
//
// // Add button click handler
// addBtn.addEventListener("click", function () {
//     modalTitle.textContent = "Tạo đơn hàng mới";
//     orderForm.reset();
//     openModal();
// });
//
// // Create first button click handler
// createFirstBtn.addEventListener("click", function () {
//     modalTitle.textContent = "Tạo đơn hàng mới";
//     orderForm.reset();
//     openModal();
// });
//
// // Close modal buttons
// modalCloseBtn.addEventListener("click", closeModal);
// cancelBtn.addEventListener("click", closeModal);
//
// // Form submit handler
// orderForm.addEventListener("submit", function (e) {
//     e.preventDefault();
//     // Here you would normally save the form data
//     // For demo purposes, we just close the modal
//     closeModal();
//
//     // Show success message
//     alert("Đơn hàng đã được lưu thành công!");
//
//     // Re-render orders
//     renderOrders();
// });
//
// // Functions to open/close modal
// function openModal() {
//     modal.classList.add("order_show");
// }
//
// function closeModal() {
//     modal.classList.remove("order_show");
// }
//
// // View order
// function viewOrder() {
//     const orderId = this.dataset.id;
//     alert(`Xem chi tiết đơn hàng ${orderId}`);
//     // Here you would normally open a modal with order details
// }
//
// // Edit order
// function editOrder() {
//     const orderId = this.dataset.id;
//     modalTitle.textContent = `Chỉnh sửa đơn hàng ${orderId}`;
//
//     // Here you would normally populate the form with order data
//     // For demo purposes, we just open the modal
//     openModal();
// }
//
// // Delete order
// function deleteOrder() {
//     const orderId = this.dataset.id;
//     if (confirm(`Bạn có chắc chắn muốn xóa đơn hàng ${orderId}?`)) {
//         // Here you would normally delete the order
//         alert(`Đã xóa đơn hàng ${orderId}`);
//
//         // Re-render orders
//         renderOrders();
//     }
// }
//
// // Click outside modal to close
// window.addEventListener("click", function (e) {
//     if (e.target === modal) {
//         closeModal();
//     }
// });
//
// // Initial render
// renderOrders();


document.addEventListener("DOMContentLoaded", function () {
    const filterButtons = document.querySelectorAll(".order_filter");
    const orderRows = document.querySelectorAll("#order_table-body tr");
    const emptyState = document.getElementById("order_empty-state");

    // Xử lý sự kiện click cho từng nút lọc
    filterButtons.forEach((button) => {
        button.addEventListener("click", function () {
            // Xóa class "order_active" khỏi tất cả các nút
            filterButtons.forEach((btn) => btn.classList.remove("order_active"));
            // Thêm class "order_active" vào nút được click
            this.classList.add("order_active");

            // Lấy trạng thái filter từ data-filter
            const filterValue = this.getAttribute("data-filter");
            let visibleCount = 0;

            // Lọc danh sách đơn hàng
            orderRows.forEach((row) => {
                const statusCell = row.querySelector("td span"); // Lấy thẻ <span> chứa trạng thái
                const statusText = statusCell ? statusCell.innerText.trim().toUpperCase() : "";

                if (filterValue === "all" || statusText === filterValue.toUpperCase()) {
                    row.style.display = ""; // Hiển thị hàng
                    visibleCount++;
                } else {
                    row.style.display = "none"; // Ẩn hàng
                }
            });

            // Kiểm tra xem có đơn hàng nào hiển thị không, nếu không thì hiển thị trạng thái rỗng
            emptyState.style.display = visibleCount === 0 ? "block" : "none";
        });
    });
});

document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("order_search-input");
    const searchDateInput = document.getElementById("order_search-date");
    const orderRows = document.querySelectorAll("#order_table-body tr");
    const emptyState = document.getElementById("order_empty-state");

    function filterOrders() {
        const searchText = searchInput.value.trim().toLowerCase();
        const searchDate = searchDateInput.value; // Ngày nhập vào (YYYY-MM-DD)
        let visibleCount = 0;

        orderRows.forEach((row) => {
            const orderId = row.cells[0].innerText.trim().toLowerCase(); // Mã đơn
            const customerName = row.cells[1].innerText.trim().toLowerCase(); // Tên KH
            const orderDateTime = row.cells[2].innerText.trim(); // Ngày đặt (dd/MM/yyyy HH:mm:ss)

            // Chuyển ngày đặt từ "20/03/2025 05:37:32" thành "2025-03-20" để so sánh
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