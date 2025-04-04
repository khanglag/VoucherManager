//lọc users
document.addEventListener("DOMContentLoaded", function () {
    const rowsPerPage = 15;
    let currentPage = 1;

    const tableBody = document.getElementById("users_tableBody");
    const rows = Array.from(tableBody.querySelectorAll("tr"));

    const prevBtn = document.getElementById("prevPage");
    const nextBtn = document.getElementById("nextPage");
    const pageInfo = document.getElementById("pageInfo");

    const searchInput = document.getElementById("users_searchInput");
    const roleFilter = document.getElementById("users_roleFilter");
    const resetBtn = document.getElementById("users_resetFilter"); // Nút Đặt lại

    function filterRows() {
        const searchText = searchInput.value.toLowerCase();
        const selectedRole = roleFilter.value.toLowerCase();

        return rows.filter(row => {
            const username = row.cells[1].textContent.toLowerCase();
            const fullName = row.cells[2].textContent.toLowerCase();
            const email = row.cells[3].textContent.toLowerCase();
            const phone = row.cells[4].textContent.toLowerCase();
            const role = row.cells[5].textContent.toLowerCase();

            const matchesSearch = [username, fullName, email, phone].some(field => field.includes(searchText));
            const matchesRole = selectedRole === "" || role === selectedRole;

            return matchesSearch && matchesRole;
        });
    }

    function renderTable(filteredRows) {
        const totalPages = Math.ceil(filteredRows.length / rowsPerPage);
        currentPage = Math.min(currentPage, totalPages) || 1;

        rows.forEach(row => row.style.display = "none");
        filteredRows.forEach((row, index) => {
            row.style.display = (index >= (currentPage - 1) * rowsPerPage && index < currentPage * rowsPerPage) ? "" : "none";
        });

        pageInfo.textContent = `Trang ${currentPage} / ${totalPages || 1}`;
        prevBtn.disabled = currentPage === 1;
        nextBtn.disabled = currentPage === totalPages || totalPages === 0;
    }

    function updateTable() {
        renderTable(filterRows());
    }

    // Xử lý khi ấn nút Đặt lại
    resetBtn.addEventListener("click", function () {
        searchInput.value = "";
        roleFilter.value = "";
        currentPage = 1; // Quay về trang đầu tiên
        updateTable();
    });

    prevBtn.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage--;
            updateTable();
        }
    });

    nextBtn.addEventListener("click", function () {
        if (currentPage < Math.ceil(filterRows().length / rowsPerPage)) {
            currentPage++;
            updateTable();
        }
    });

    searchInput.addEventListener("input", updateTable);
    roleFilter.addEventListener("change", updateTable);

    updateTable();
});

//model thêm người dùng
document.addEventListener("DOMContentLoaded", function () {
    const addUserBtn = document.getElementById("users_addUserBtn");
    const modal = document.getElementById("users_modal");
    const closeModalBtn = document.getElementById("users_closeModal");
    addUserBtn.addEventListener("click", function () {
        modal.style.display = "block";
    });

    closeModalBtn.addEventListener("click", function () {
        modal.style.display = "none";
    });

    window.addEventListener("click", function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    });
});

//thêm người dùng
document.getElementById("users_form").addEventListener("submit", function (event) {
    event.preventDefault();
    const userData = {
        username: document.getElementById("users_username").value.trim(),
        fullName: document.getElementById("users_name").value.trim(),
        email: document.getElementById("users_email").value.trim(),
        phoneNumber: document.getElementById("users_phone").value.trim(),
        role: document.getElementById("users_role").value
    };
    fetch('/admin/addUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showNotificationUser(data.error);
            } else if (data.message) {
                showNotificationUser(data.message );
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
        });

});

// hàm hiển thị thông báo thành công, thất bại
function showNotificationUser(message, isError = false) {
    const notification = document.getElementById("users_notification");
    if (!notification) return;

    notification.classList.remove("error", "success");
    notification.classList.add(isError ? "error" : "success");

    const icon = isError ? "❌" : "✔️";
    notification.innerHTML = `<span>${icon}</span> ${message}`;

    notification.classList.add("show");

    setTimeout(() => {
        notification.classList.remove("show");

        if (!isError) {
            window.location.reload();
        }
    }, 1500);
}

//model sửa ngưi dùng
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".users_btn-edit").forEach(button => {
        button.addEventListener("click", function () {
            let row = this.closest("tr");
            let username = row.cells[1].textContent.trim();
            let fullName = row.cells[2].textContent.trim();
            let email = row.cells[3].textContent.trim();
            let phone = row.cells[4].textContent.trim();
            let role = row.cells[5].textContent.trim();
            let status = row.cells[6].textContent.trim();
            document.getElementById("edit_username").value = username;
            document.getElementById("edit_fullName").value = fullName;
            document.getElementById("edit_email").value = email;
            document.getElementById("edit_phone").value = phone;

            let roleSelect = document.getElementById("edit_role");
            for (let option of roleSelect.options) {
                if (option.text === role) {
                    option.selected = true;
                    break;
                }
            }
            let statusSelect = document.getElementById("edit_status");
            for (let option of statusSelect.options) {
                if (option.text === status) {
                    option.selected = true;
                    break;
                }
            }

            let editModal = new bootstrap.Modal(document.getElementById("editUserModal"));
            editModal.show();
        });
    });
});

//fetch sửa người dùng
document.querySelector("#editUserForm .btnEditUser").addEventListener("click", function (event) {
    event.preventDefault();
    const userData = {
        username: document.getElementById("edit_username").value,
        fullName: document.getElementById("edit_fullName").value,
        email: document.getElementById("edit_email").value,
        phoneNumber: document.getElementById("edit_phone").value,
        role: document.getElementById("edit_role").value,
        status: document.getElementById("edit_status").value
    };

    fetch('/admin/editUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showNotificationUser(data.error, true);
            } else if (data.message) {
                showNotificationUser(data.message, false);
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            showNotificationUser("Đã xảy ra lỗi, vui lòng thử lại!", true);
        });
});


// khóa người dùng
let user_selectedUsername = "";
function user_openModal(button) {
    user_selectedUsername = button.getAttribute("data-username");

    document.getElementById("user_confirmModal").style.display = "block";
}

function user_closeModal() {
    document.getElementById("user_confirmModal").style.display = "none";
}

function user_blockUser() {

    const userData = {
        username: user_selectedUsername
    };

    fetch('/admin/blockUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(userData)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                showNotificationUser(data.error, true);
            } else if (data.message) {
                showNotificationUser(data.message, false);
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            showNotificationUser("Đã xảy ra lỗi, vui lòng thử lại!", true);
        });
    user_closeModal();
}

//phân trang
document.addEventListener("DOMContentLoaded", function () {
    const rowsPerPage = 15;
    let currentPage = 1;

    const tableBody = document.getElementById("users_tableBody");
    const rows = Array.from(tableBody.querySelectorAll("tr"));
    const totalPages = Math.ceil(rows.length / rowsPerPage);

    const prevBtn = document.getElementById("prevPage");
    const nextBtn = document.getElementById("nextPage");
    const pageInfo = document.getElementById("pageInfo");

    function renderTable() {
        rows.forEach((row, index) => {
            row.style.display = (index >= (currentPage - 1) * rowsPerPage && index < currentPage * rowsPerPage) ? "" : "none";
        });

        pageInfo.textContent = `Trang ${currentPage} / ${totalPages}`;
        prevBtn.disabled = currentPage === 1;
        nextBtn.disabled = currentPage === totalPages;
    }

    prevBtn.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage--;
            renderTable();
        }
    });

    nextBtn.addEventListener("click", function () {
        if (currentPage < totalPages) {
            currentPage++;
            renderTable();
        }
    });

    renderTable();
});