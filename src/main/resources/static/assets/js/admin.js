// Toggle sidebar
function toggleSidebar() {
    const sidebar = document.getElementById("sidebar");

    // For large screens, use the existing collapse mechanism
    if (window.innerWidth > 1024) {
        sidebar.classList.toggle("collapsed");
    }
    // For smaller screens, use mobile menu toggle
    else {
        sidebar.classList.toggle("mobile-open");
    }
}

// Ensure sidebar closes when clicking outside on mobile
document.addEventListener("click", function (event) {
    const sidebar = document.getElementById("sidebar");
    const sidebarToggle = document.getElementById("sidebar-toggle");

    if (
        window.innerWidth <= 1024 &&
        sidebar.classList.contains("mobile-open") &&
        !sidebar.contains(event.target) &&
        !sidebarToggle.contains(event.target)
    ) {
        sidebar.classList.remove("mobile-open");
    }
});

document.addEventListener("DOMContentLoaded", function () {
    document.querySelector("#sidebar-nav li").classList.add("active");
});

function changePage(pageId, element) {
    // Hide all pages
    document
        .querySelectorAll(".page")
        .forEach((page) => (page.style.display = "none"));

    // Show selected page
    document.getElementById(pageId).style.display = "block";

    // Remove active class from all sidebar items
    document
        .querySelectorAll("#sidebar-nav li")
        .forEach((li) => li.classList.remove("active"));

    // Add active class to clicked item
    element.classList.add("active");

    // On mobile, close sidebar after page selection
    if (window.innerWidth <= 1024) {
        document.getElementById("sidebar").classList.remove("mobile-open");
    }
}

function logout() {

    document.getElementById('logout-form').submit();
}