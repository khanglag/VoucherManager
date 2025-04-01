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
// Hàm cập nhật tổng tiền
function updateSubtotal() {
    const cartItems = document.querySelectorAll('.cart-item');
    let subtotal = 0;

    cartItems.forEach(item => {
        // Lấy giá từ data-price
        const price = parseFloat(item.getAttribute('data-price'));
        // Lấy số lượng từ span.item-quantity
        const quantity = parseInt(item.querySelector('.item-quantity').textContent) || 1; // Mặc định 1 nếu lỗi
        subtotal += price * quantity; // Tính tổng cho mỗi item
    });

    // Cập nhật giá trị vào phần tử subtotal
    const subtotalElement = document.querySelector('#subtotal');
    if (subtotalElement) {
        // Định dạng số với dấu phẩy (theo kiểu VND)
        const formattedSubtotal = subtotal.toLocaleString('vi-VN', { minimumFractionDigits: 0 });
        subtotalElement.textContent = `${formattedSubtotal}₫`; // Hiển thị dạng X,XXX₫
    }
}


// Cập nhật tổng khi trang tải
document.addEventListener('DOMContentLoaded', updateSubtotal);