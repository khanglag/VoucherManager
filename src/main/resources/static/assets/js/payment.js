function updateQuantity(itemId, change) {
    const quantityElement = document.querySelector(`.item-quantity[data-item-id="${itemId}"]`);
    if (!quantityElement) return;
    let currentQuantity = parseInt(quantityElement.textContent, 10) || 1;
    let newQuantity = currentQuantity + change;
    console.log("Update quatity " +  change)
    if (newQuantity < 1) newQuantity = 1;
    console.log("New quatity " +  newQuantity)
    // Gửi request cập nhật số lượng lên Spring Boot API
    fetch(`/api/payment/update`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            itemId: itemId,
            quantity: newQuantity
        })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                quantityElement.textContent = newQuantity;

                // Lấy giá sản phẩm
                const priceElement = quantityElement.closest('.cart-item').querySelector('.price-box .price span:nth-child(2)');
                const price = parseFloat(priceElement.textContent.replace(/[^\d]/g, '').trim());

                // Lấy phần tử hiển thị tổng tiền
                const totalElement = quantityElement.closest('.cart-item').querySelector('.price-box .item-total');

                // Cập nhật tổng tiền
                totalElement.textContent = `${(newQuantity * price).toLocaleString()}₫`;
                updateSubtotal();
            } else {
            }
        })
        .catch(error => {
            console.error("Lỗi khi cập nhật số lượng:", error);
        });
}
function removeItem(cartItemId) {
    // Hiển thị xác nhận trước khi xóa (tùy chọn)
    if (!confirm("Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng không?")) {
        return;
    }

    // Gửi yêu cầu AJAX đến server để xóa item
    fetch(`/api/payment/remove/${cartItemId}`, {
        method: 'DELETE', // Hoặc 'POST' tùy API của bạn
        headers: {
            'Content-Type': 'application/json',
            // Nếu cần CSRF token (Spring Security), thêm header này
            // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Xóa thất bại: ' + response.status);
            }
            // Xóa phần tử khỏi DOM nếu xóa thành công
            const itemElement = document.querySelector(`.cart-item[data-id="${cartItemId}"]`);
            if (itemElement) {
                itemElement.remove();
            }
            updateSubtotal();
        })
        .catch(error => {
            console.error('Lỗi:', error);
            alert("Có lỗi xảy ra khi xóa sản phẩm!");
        });
}
let summarynb = 0;
// Hàm cập nhật tổng tiền
function updateSubtotal() {
    const cartItems = document.querySelectorAll('.cart-item');
    const summary = document.getElementById("summary-summary")
    let subtotal = 0;
    cartItems.forEach(item => {
        // Lấy giá từ data-price
        const price = parseFloat(item.getAttribute('data-price'));
        // Lấy số lượng từ span.item-quantity
        const quantity = parseInt(item.querySelector('.item-quantity').textContent) || 1; // Mặc định 1 nếu lỗi
        subtotal += price * quantity; // Tính tổng cho mỗi item

    });
    summarynb = subtotal;
    // Cập nhật giá trị vào phần tử subtotal
    const subtotalElement = document.querySelector('#subtotal');
    const summaryElement = document.getElementById('summary')
    if (subtotalElement) {
        // Định dạng số với dấu phẩy (theo kiểu VND)
        const formattedSubtotal = subtotal.toLocaleString('vi-VN', { minimumFractionDigits: 0 });
        subtotalElement.textContent = `${formattedSubtotal}₫`; // Hiển thị dạng X,XXX₫
        summaryElement.textContent = `${formattedSubtotal}₫`;
        summarydiv()
    }
}



// Danh sách voucher mẫu





// Hàm đóng modal
function closeVoucherModal() {
    document.getElementById("voucherModal").style.display = "none";
}

let voucherCodelet=null;
// Hàm áp dụng voucher
async function applyVoucher(voucherCode, type, price) {
    const currentCheckbox = document.querySelector(`input[onclick="applyVoucher('${voucherCode}', '${type}', '${price}')"]`);
    const isChecked = currentCheckbox.checked;
    let style;
    if(type === "FREESHIP")
        style = "shipping"
    else style = "shop-voucher"
    console.log(type)
    console.log(style)
    // Nếu checkbox được chọn, bỏ chọn tất cả checkbox khác
    if (isChecked) {
        document.querySelectorAll('.checkbox').forEach(checkbox => {
            if (checkbox !== currentCheckbox) {
                checkbox.checked = false; // Bỏ chọn các checkbox khác
            }
        });
        updateVoucher(type, price);
        // Thay đổi nút "Chọn voucher" thành nút "X"
        const voucherOption = document.querySelector(`#${style}VoucherOption`); // Sử dụng querySelector với id kiểu #shopVoucherOption hoặc #shippingVoucherOption
        if (voucherOption) {
            const button = voucherOption.querySelector("button");
            if (button) {
                button.innerHTML = "X";  // Đổi nội dung nút thành "X"
                button.setAttribute("onclick", `cancelVoucher('${voucherCode}', '${style}')`);  // Cập nhật sự kiện onclick
            } else {
                console.error("Không tìm thấy nút button trong voucher option");
            }
        } else {
            console.error(`Không tìm thấy phần tử với id ${style}VoucherOption`);
        }
    }
    voucherCodelet = voucherCode;
}

function cancelVoucher(voucherCode, type) {
    // Khôi phục lại nút "Chọn voucher"
    const voucherOption = document.querySelector(`#${type}VoucherOption`);
    if (voucherOption) {
        const button = voucherOption.querySelector("button");
        if (button) {
            button.innerHTML = "Chọn voucher";  // Đổi nội dung nút về "Chọn voucher"
            button.setAttribute("onclick", `openVoucherModal('${type}')`);  // Khôi phục sự kiện onclick
        } else {
            console.error("Không tìm thấy nút button trong voucher option");
        }
    } else {
        console.error(`Không tìm thấy phần tử với id ${type} VoucherOption`);
    }
    fetch('/api/payment/cancelvoucher', {
        method: 'GET', // Hoặc 'POST' nếu yêu cầu sử dụng phương thức POST
        headers: {
            'Content-Type': 'application/json' // Cài đặt header (nếu cần thiết)
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Không thể hủy voucher');
            }
            console.log('Voucher đã được hủy thành công');
        })
        .catch(error => {
            console.error('Lỗi khi hủy voucher:', error);
        });
    // Bỏ chọn tất cả các checkbox
    const checkboxes = document.querySelectorAll('.checkbox');
    checkboxes.forEach(checkbox => {
        checkbox.checked = false;
    });

    // Gọi hàm hủy voucher (nếu có)
    // Có thể thêm logic hủy voucher tại đây nếu cần
    resetVoucherValue(type);  // Giả sử gọi updateVoucher để cập nhật khi hủy voucher
}
function resetVoucherValue(type) {
    const vouchershipElement = document.getElementById('voucher-ship');
    const vouchershopElement = document.getElementById('voucher-shop');
    console.log(type)
    // Kiểm tra kiểu voucher và gán giá trị 0 cho phần tử tương ứng
    if (type === "shipping") {
        vouchershipElement.textContent = "0₫";
        appliedVouchers.ship = 0;
    } else  {
        vouchershopElement.textContent = "0₫";
        appliedVouchers.shop = 0;
    }
    // Cập nhật lại tổng giá trị sau khi reset voucher
    const summary = document.getElementById('summary-summary');
    const totalValue = summarynb - (appliedVouchers.ship || 0) - (appliedVouchers.shop || 0);
    summarydiv();
}

const appliedVouchers = {
    ship: 0,
    shop: 0
};
function summarydiv() {
    const summary = document.getElementById('summary-summary');
    const shipFee = 30000; // Phí ship gốc
    let productTotal = summarynb; // Tổng tiền hàng (không có phí ship)


    let discountShop = 0;
    if (appliedVouchers.shop) {
        if (appliedVouchers.shop.percent) {
            discountShop = productTotal * (appliedVouchers.shop.percent / 100); // Áp dụng giảm giá % chỉ trên tiền hàng
        } else {
            discountShop = appliedVouchers.shop;
        }
    }

    let discountShip = appliedVouchers.ship || 0;
    let finalShipFee = Math.max(0, shipFee - discountShip); // Phí ship sau khi giảm

    // --- 3. Tính tổng tiền phải trả ---
    let total = Math.max(0, (productTotal - discountShop) + finalShipFee); // Tiền hàng sau giảm + phí ship còn lại

    // --- 4. Hiển thị giảm giá chi tiết ---
    document.getElementById('summary').textContent = productTotal.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // Hiển thị giảm giá sản phẩm
    document.getElementById('voucher-shop').textContent = discountShop.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // Hiển thị giảm giá phí ship
    document.getElementById('voucher-ship').textContent = discountShip.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

    // Hiển thị tổng thanh toán cuối cùng
    document.getElementById('summary-summary').textContent = total.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });

}

function updateVoucher(type, price) {
    const subtotalString = document.getElementById('subtotal').textContent;
// Loại bỏ dấu chấm và ký tự ₫, sau đó chuyển thành số
    const numericValue = parseFloat(subtotalString.replace(/[₫,\.]/g, '').replace('₫', ''));
    // Chuyển price thành số trước khi format
    const numericPrice = Number(price);
    const formattedSubtotal = numericPrice.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });
    if (type === "FREESHIP") {
        const vouchershipElement = document.getElementById('voucher-ship');
        if (vouchershipElement) {
            appliedVouchers.ship = numericPrice;
            vouchershipElement.textContent = formattedSubtotal;
            summarydiv();
        } else {
            console.error("Không tìm thấy #voucher-ship");
        }
    } else {
        const vouchershopElement = document.getElementById('voucher-shop');
        if (vouchershopElement) {
            if (type==="FIXED"){
                appliedVouchers.shop = numericPrice;
                vouchershopElement.textContent = formattedSubtotal;
                summarydiv()
            }else {
                appliedVouchers.shop =((summarynb)*numericPrice/100);
                vouchershopElement.textContent = (summarynb - (summarynb-appliedVouchers.ship-appliedVouchers.shop)).toLocaleString('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });
                summarydiv()
            }
        } else {
            console.error("Không tìm thấy #voucher-shop");
        }
    }
}

async function submitVoucher() {
    try {
        const response = await fetch('/api/payment/apply-voucher', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
            },
            body: JSON.stringify({
                voucherCode: voucherCodelet // Gửi voucherCode nếu chọn, null nếu bỏ chọn
            })
        });

        if (response.ok) {
            const result = await response.text();
        } else {
            console.error('Failed to apply voucher:', response.status);
        }
    } catch (error) {
        console.error('Error:', error);
    }
    closeVoucherModal();
}

// Khôi phục trạng thái khi tải trang (nếu cần)
function restoreVoucherSelection() {
    const selectedVoucher = sessionStorage.getItem('selectedVoucher'); // Hoặc lấy từ server
    if (selectedVoucher) {
        const checkbox = document.querySelector(`input[onclick="applyVoucher('${selectedVoucher}')"]`);
        if (checkbox) checkbox.checked = true;
    }
}

// async function createPurchaseRequest() {
//     try {
//         const response = await fetch('/api/purchase/create', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             }
//         });
//
//         if (response.ok) {
//             // Chuyển hướng sau khi xử lý xong
//             window.location.href = "/createOrderVNpay";
//         } else {
//             const errorText = await response.text();
//             console.error("Lỗi từ server:", errorText);
//             alert("Gặp lỗi khi xử lý đơn hàng!");
//         }
//     } catch (error) {
//         console.error('Lỗi:', error);
//         alert('Đã có lỗi xảy ra!');
//     }
// }

window.onload = restoreVoucherSelection;

// Cập nhật tổng khi trang tải
document.addEventListener('DOMContentLoaded', updateSubtotal);

// Lấy các phần tử modal và nút thay đổi địa chỉ
const changeAddressButton = document.querySelector(".change-address");
const modal = document.getElementById("addressModal");
const addressForm = document.getElementById("addressForm");
const newAddressInput = document.getElementById("new-address");
const customerAddress = document.querySelector(".customer-address");

// Hàm mở modal
function openModal() {
    const modal = document.getElementById("addressModal");
    modal.style.display = "flex";

    // Lấy thông tin hiện tại để điền sẵn vào input
    document.getElementById("new-name").value = document.getElementById("customer-name").textContent;
    document.getElementById("new-phone").value = document.getElementById("customer-phone").textContent;
    document.getElementById("new-address").value = document.getElementById("customer-address").textContent;
}

function closeModal() {
    document.getElementById("addressModal").style.display = "none";
}

document.getElementById("addressForm").addEventListener("submit", function(e) {
    e.preventDefault();

    // Lấy dữ liệu từ input
    const newName = document.getElementById("new-name").value;
    const newPhone = document.getElementById("new-phone").value;
    const newAddress = document.getElementById("new-address").value;

    // Cập nhật thông tin hiển thị
    document.getElementById("customer-name").textContent = newName;
    document.getElementById("customer-phone").textContent = newPhone;
    document.getElementById("customer-address").textContent = newAddress;

    // Đóng modal
    closeModal();
});


// Hàm đóng modal
function closeModal() {
    modal.style.display = "none";  // Ẩn modal
}

// Hàm xử lý form khi người dùng thay đổi địa chỉ
addressForm.addEventListener("submit", function(event) {
    event.preventDefault();  // Ngừng gửi form

    const newAddress = newAddressInput.value.trim();
    if (newAddress) {
        // Cập nhật địa chỉ trên giao diện
        customerAddress.textContent = newAddress;
        closeModal();  // Đóng modal sau khi lưu
        console.log("Địa chỉ đã được cập nhật:", newAddress);  // In ra địa chỉ mới (thực hiện hành động khác nếu cần)
    }
});

// Lắng nghe sự kiện click vào nút thay đổi địa chỉ
changeAddressButton.addEventListener("click", openModal);
// Hàm tìm kiếm voucher theo mã voucher
// Hàm tìm kiếm voucher theo mã voucher
function searchVoucher() {
    const searchQuery = document.getElementById("voucherSearch").value.trim(); // Lấy giá trị từ input
    const voucherList = document.getElementById("voucher-list");

    // Ẩn danh sách voucher trước khi tìm kiếm
    voucherList.style.display = "none";

    if (searchQuery === "") {
        alert("Vui lòng nhập mã voucher!");
        return;
    }

    // Gọi API để tìm kiếm voucher theo mã
    fetch(`http://localhost:8080/api/vouchers/search?voucherCode=${searchQuery}`)
        .then(response => response.json())
        .then(vouchers => {
            // Kiểm tra nếu không có voucher nào
            if (vouchers.length === 0) {
                voucherList.innerHTML = "<p>Không có voucher nào với mã bạn tìm kiếm.</p>";
            } else {
                // Cập nhật nội dung voucherList
                voucherList.innerHTML = vouchers.map(voucher => `
                    <li>
                        <div class="coupon-left">
                            <span>${voucher.discountType === "FREESHIP" ? "FREE SHIP" : "GIẢM GIÁ"}</span>
                            <small>${voucher.discountType === "FREESHIP" ? "Mã vận chuyển" : "Mã giảm giá"}</small>
                        </div>
                        <div class="coupon-right">
                            <div class="info-voucher">
                                <span class="voucher-id">${voucher.voucherCode}</span>
                                <small class="title-voucher">${voucher.title}</small>
                                <span class="voucher-date">${voucher.endDate}</span>
                            </div>
                            <div class="voucher-price">
                                <small class="voucher-quantity">X${voucher.maxUsage - voucher.usageCount}</small>
                                <label>
                                    <input type="checkbox" class="checkbox" onclick="applyVoucher('${voucher.voucherCode}', '${voucher.discountType}', '${voucher.discountValue}')">
                                </label>
                            </div>
                        </div>
                    </li>
                `).join(""); // Nối tất cả HTML thành một chuỗi
            }

            // Hiển thị lại danh sách voucher
            voucherList.style.display = "block";

            // Chọn checkbox đầu tiên nếu có voucher
            const firstCheckbox = voucherList.querySelector("input[type='checkbox']");
            if (firstCheckbox) {
                firstCheckbox.checked = true;
                const firstVoucher = vouchers[0]; // Lấy thông tin voucher đầu tiên
                applyVoucher(firstVoucher.voucherCode, firstVoucher.discountType, firstVoucher.discountValue);
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu:", error);
            voucherList.innerHTML = "<p>Lỗi khi tải danh sách.</p>";
            voucherList.style.display = "block"; // Hiển thị lại nếu có lỗi
        });
}
function openVoucherModal(type) {
    const modal = document.getElementById("voucherModal");
    const voucherList = document.getElementById("voucher-list");
    const voucherSearchContainer = document.getElementById("voucher-search");
    // Hiển thị modal
    modal.style.display = "flex";
    voucherSearchContainer.style.display = "none";
    // Gọi API lấy danh sách voucher
    fetch(`http://localhost:8080/api/vouchers?type=${type}`)
        .then(response => response.json())
        .then(vouchers => {
            if (vouchers.length === 0) {
                voucherList.innerHTML = "<p>Không có voucher nào.</p>";
                return;
            }
            // Cập nhật nội dung voucherList
            voucherList.innerHTML = vouchers.map(voucher => `
                <li>
                    <div class="coupon-left">
                        <span>${voucher.discountType === "FREESHIP" ? "FREE SHIP" : "GIẢM GIÁ"}</span>
                        <small>${voucher.discountType === "FREESHIP" ? "Mã vận chuyển" : "Mã giảm giá"}</small>
                    </div>
                    <div class="coupon-right">
                        <div class="info-voucher">
                            <span class="voucher-id">${voucher.voucherCode}</span>
                            <small class="title-voucher">${voucher.title}</small>
                            <span class="voucher-date">${voucher.endDate}</span>
                        </div>
                        <div class="voucher-price">
                            <small class="voucher-quantity">X${voucher.maxUsage - voucher.usageCount}</small>
                            <label>
                                <input type="checkbox" class="checkbox" onclick="applyVoucher('${voucher.voucherCode}', '${voucher.discountType}', '${voucher.discountValue}')">
                            </label>
                        </div>
                    </div>
                </li>
            `).join(""); // Nối tất cả HTML thành một chuỗi

            const firstCheckbox = voucherList.querySelector("input[type='checkbox']");
            if (firstCheckbox) {
                firstCheckbox.checked = true;
                const firstVoucher = vouchers[0]; // Lấy thông tin voucher đầu tiên
                applyVoucher(firstVoucher.voucherCode, firstVoucher.discountType, firstVoucher.discountValue);
            }
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu:", error);
            voucherList.innerHTML = "<p>Lỗi khi tải danh sách.</p>";
        });
}
function createPurchaseRequest() {
    fetch('/api/purchase/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ /* dữ liệu cần thiết */ })
    })
        .then(response => {
            if (response.ok) {
                window.location.href = '/individual/order_history';  // Điều hướng khi thành công
            } else {
                alert('Error occurred');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
}
