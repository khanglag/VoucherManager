function filterOrders() {
    const statusFilter = document.getElementById('statusFilter').value;
    const orders = document.querySelectorAll('.orderDetail_show-card');

    orders.forEach(order => {
        const status = order.querySelector('.orderDetail_show-status span').innerText.toUpperCase();

        if (statusFilter === 'ALL' || status === statusFilter.toUpperCase()) {
            order.style.display = 'block';
        } else {
            order.style.display = 'none';
        }
    });
    const noResultMessage = document.getElementById('noResultMessage');
    const visibleOrders = Array.from(orders).filter(order => order.style.display === 'block');
    if (visibleOrders.length === 0) {
        noResultMessage.style.display = 'block';
    } else {
        noResultMessage.style.display = 'none';
    }
}
function toggleDetails(button) {
    const detailDiv = button.nextElementSibling;
    if (detailDiv.style.display === "none") {
        detailDiv.style.display = "block";
        button.textContent = "Ẩn chi tiết";
    } else {
        detailDiv.style.display = "none";
        button.textContent = "Xem chi tiết";
    }
}

