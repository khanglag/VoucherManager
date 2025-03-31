

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