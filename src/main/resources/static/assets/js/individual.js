document.addEventListener("DOMContentLoaded", function () {
    const toggleBtn = document.querySelector(".toggle-btn");
    const sidebar = document.querySelector(".sidebar");
    const menuItems = document.querySelectorAll(".menu-item");
    const contentPages = document.querySelectorAll(".page-content");

    // Toggle sidebar
    toggleBtn.addEventListener("click", function () {
        sidebar.classList.toggle("collapsed");
    });

    // Menu item click
    menuItems.forEach((item) => {
        item.addEventListener("click", function (e) {
            // Add ripple effect
            let ripple = document.createElement("span");
            ripple.classList.add("ripple");

            let rect = this.getBoundingClientRect();
            let x = e.clientX - rect.left;
            let y = e.clientY - rect.top;

            ripple.style.left = x + "px";
            ripple.style.top = y + "px";

            this.appendChild(ripple);

            setTimeout(() => {
                ripple.remove();
            }, 600);

            // Set active class
            menuItems.forEach((mi) => mi.classList.remove("active"));
            this.classList.add("active");

            // Show corresponding content
            const pageName = this.getAttribute("data-page");
            showPage(pageName);
        });
    });

    // Function to show the selected page
    function showPage(pageName) {
        contentPages.forEach((page) => {
            page.style.display = "none";
        });

        let pageMapping = {
            personal: "personal-info",
            orders: "order-history",
            vouchers: "voucher-management",
            stats: "statistics",
        };

        document.getElementById(pageMapping[pageName]).style.display =
            "block";
    }

    // Initialize - show first page by default
    showPage("personal");

    // Add animation to menu items on load
    menuItems.forEach((item, index) => {
        item.style.animationDelay = index * 0.1 + "s";
    });
});