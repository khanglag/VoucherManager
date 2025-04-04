function updateQuantity(itemId, change) {
    const quantityElement = document.querySelector(`.item-quantity[data-item-id="${itemId}"]`);
    if (!quantityElement) return;

    let currentQuantity = parseInt(quantityElement.textContent, 10) || 1;
    let newQuantity = currentQuantity + change;
    if (newQuantity < 1) newQuantity = 1;

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
                alert(data.message || "Không thể cập nhật số lượng, vui lòng thử lại!");
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
            alert("Đã xóa sản phẩm khỏi giỏ hàng!");
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
    summarynb = subtotal+ 30000;
    // Cập nhật giá trị vào phần tử subtotal
    const subtotalElement = document.querySelector('#subtotal');
    const summaryElement = document.getElementById('summary')
    if (subtotalElement) {
        // Định dạng số với dấu phẩy (theo kiểu VND)
        const formattedSubtotal = subtotal.toLocaleString('vi-VN', { minimumFractionDigits: 0 });
        subtotalElement.textContent = `${formattedSubtotal}₫`; // Hiển thị dạng X,XXX₫
        summaryElement.textContent = `${formattedSubtotal}₫`;
        summary.textContent = `${summarynb.toLocaleString('vi-VN', { minimumFractionDigits: 0 })}₫`
    }
}



// Danh sách voucher mẫu


function openVoucherModal(type) {
    const modal = document.getElementById("voucherModal");
    const voucherList = document.getElementById("voucher-list");

    // Hiển thị modal
    modal.style.display = "flex";

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
        })
        .catch(error => {
            console.error("Lỗi khi lấy dữ liệu:", error);
            voucherList.innerHTML = "<p>Lỗi khi tải danh sách.</p>";
        });
}

// Hàm đóng modal
function closeVoucherModal() {
    document.getElementById("voucherModal").style.display = "none";
}

let voucherCodelet=null;
// Hàm áp dụng voucher
async function applyVoucher(voucherCode, type, price) {
    const currentCheckbox = document.querySelector(`input[onclick="applyVoucher('${voucherCode}', '${type}', '${price}')"]`);
    const isChecked = currentCheckbox.checked;

    // Nếu checkbox được chọn, bỏ chọn tất cả checkbox khác
    if (isChecked) {
        document.querySelectorAll('.checkbox').forEach(checkbox => {
            if (checkbox !== currentCheckbox) {
                checkbox.checked = false; // Bỏ chọn các checkbox khác
            }
        });
        updateVoucher(type,price);
    }
    voucherCodelet = voucherCode;
}
const appliedVouchers = {
    ship: 0,
    shop: 0
};
function updateVoucher(type, price) {
    const subtotalString = document.getElementById('subtotal').textContent;
    const summary = document.getElementById('summary-summary');
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
            appliedVouchers.ship=numericPrice;
            vouchershipElement.textContent = formattedSubtotal;
            summary.textContent= (summarynb-30000-appliedVouchers.ship-appliedVouchers.shop).toLocaleString('vi-VN', {
                style: 'currency',
                currency: 'VND'
            });
        } else {
            console.error("Không tìm thấy #voucher-ship");
        }
    } else {
        const vouchershopElement = document.getElementById('voucher-shop');
        if (vouchershopElement) {
            if (type==="FIXED"){
                appliedVouchers.shop = numericPrice;
                vouchershopElement.textContent = formattedSubtotal;
                summary.textContent= (summarynb-30000-appliedVouchers.ship-appliedVouchers.shop).toLocaleString('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });
            }else {
                appliedVouchers.shop =((summarynb-30000)*numericPrice/100);
                vouchershopElement.textContent = (summarynb - (summarynb-appliedVouchers.ship-appliedVouchers.shop)).toLocaleString('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });
                summary.textContent= (summarynb-30000-appliedVouchers.ship-appliedVouchers.shop).toLocaleString('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });
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

async function createPurchaseRequest() {
    try {
        // Gửi yêu cầu đến API
        const response = await fetch('/api/purchase/create', {
            method: 'POST', // Hoặc GET tùy theo API của bạn
            headers: {
                'Content-Type': 'application/json'
                // Thêm các header khác nếu cần, ví dụ token
            },
            // Nếu cần gửi dữ liệu, thêm body
            // body: JSON.stringify({ key: 'value' })
        });

        // Chuyển response thành string
        const result = await response.text();

        // Xử lý kết quả (hiển thị hoặc làm gì đó với string)
        console.log('Response từ server:', result);
        alert('Kết quả: ' + result); // Ví dụ hiển thị lên màn hình
    } catch (error) {
        console.error('Lỗi:', error);
        alert('Đã có lỗi xảy ra!');
    }
}


window.onload = restoreVoucherSelection;

// Cập nhật tổng khi trang tải
document.addEventListener('DOMContentLoaded', updateSubtotal);