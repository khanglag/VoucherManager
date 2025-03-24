//hàm logout
function logout() {
    localStorage.removeItem("cart"); // Xóa giỏ hàng khỏi localStorage
    document.getElementById('logout-form').submit(); // Gửi form đăng xuất
}
document.addEventListener("DOMContentLoaded", function () {
    function updateTotalPrice() {
        const cartItems = document.querySelectorAll(".cart-item");
        let total = 0;

        cartItems.forEach(item => {
            const price = parseFloat(item.getAttribute("data-price")) || 0;
            const quantity = parseInt(item.getAttribute("data-quantity")) || 1;
            total += price * quantity;
        });

        document.getElementById("totalPrice").textContent = new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND"
        }).format(total);
    }

    updateTotalPrice();
});
// Toggle cart modal
function toggleCart() {
    cartModal.classList.toggle("open");
    overlay.classList.toggle("open");
}
const cartModal = document.getElementById("cartModal");
const cartIcon = document.getElementById("cartIcon");
const closeCart = document.getElementById("closeCart");
const overlay = document.getElementById("overlay");
// Event listeners
cartIcon.addEventListener("click", toggleCart);
closeCart.addEventListener("click", toggleCart);
overlay.addEventListener("click", toggleCart);

const cartCountElement = document.getElementById("cartCount");

const cart = JSON.parse(localStorage.getItem("cart")) || [];
const cartLength = cart.length;
cartCountElement.textContent = cartLength;



