
document.querySelector("#form_infomation_user_personalInfoForm .form_infomation_user_button").addEventListener("click", function (event) {
    event.preventDefault();
    const form = document.getElementById("form_infomation_user_personalInfoForm");
    const form_infomation_user_button = form.querySelector(".form_infomation_user_button");
    const form_infomation_user_loading = form.querySelector(".form_infomation_user_loading");
    const messageSuccess = form.querySelector(".form_infomation_user_success");
    const messageError = form.querySelector(".form_infomation_user_error");
    messageSuccess.classList.remove("form_infomation_user_show");
    messageError.classList.remove("form_infomation_user_show");

    form_infomation_user_button.disabled = true;
    form_infomation_user_loading.style.display = "flex";

    const formData = {
        id: document.getElementById("form_infomation_user_userID").value,
        username: document.getElementById("form_infomation_user_username").value,
        password: document.getElementById("form_infomation_user_password").value,
        fullName: document.getElementById("form_infomation_user_fullName").value,
        email: document.getElementById("form_infomation_user_email").value,
        phoneNumber: document.getElementById("form_infomation_user_phoneNumber").value,
        roleId: document.getElementById("form_infomation_user_roleId").value,
    };
    fetch('/individual/personal_infomation/editUser', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"

        },
        body: JSON.stringify(formData)
    })
        .then(response => response.json())
        .then(data => {


            setTimeout(() => {
                form_infomation_user_button.disabled = false;
                form_infomation_user_loading.style.display = "none";

                if (data.error) {
                    messageError.textContent = data.error;
                    messageError.classList.add("form_infomation_user_show");
                } else if (data.message) {
                    messageSuccess.textContent = data.message;
                    messageSuccess.classList.add("form_infomation_user_show");
                }
            }, 2000); // 2 giây
        })
        .catch(error => {
            setTimeout(() => {
                form_infomation_user_button.disabled = false;
                form_infomation_user_loading.style.display = "none";
                messageError.textContent = "Đã xảy ra lỗi, vui lòng thử lại!";
                messageError.classList.add("form_infomation_user_show");
            }, 2000);

        });
});

//đổi mk
document.querySelector("#form_change_password .changePassword_submit").addEventListener("click", function (event) {
    event.preventDefault();
    const form = document.getElementById("form_change_password");

    const oldPassword = document.getElementById("changePassword_oldPassword").value;
    const newPassword = document.getElementById("changePassword_newPassword").value;
    const confirmPassword = document.getElementById("changePassword_confirmPassword").value;

    const loading = form.querySelector(".changePassword_loading");
    const messageSuccess = form.querySelector(".changePassword_success");
    const messageError = form.querySelector(".changePassword_error");

    // Reset messages
    messageSuccess.classList.remove("show");
    messageError.classList.remove("show");
    loading.style.display = "flex";

    if (newPassword !== confirmPassword) {
        loading.style.display = "none";
        messageError.textContent = "Mật khẩu mới và xác nhận không khớp.";
        messageError.classList.add("show");
        return;
    }

    const data = {
        oldPassword: oldPassword,
        newPassword: newPassword
    };

    fetch('/individual/personal_infomation/changePassword', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            setTimeout(() => {
                loading.style.display = "none";
                if (data.error) {
                    messageError.textContent = data.error;
                    messageError.classList.add("show");
                } else {
                    messageSuccess.textContent = data.message;
                    messageSuccess.classList.add("show");
                }
            }, 2000);
        })
        .catch(error => {
            setTimeout(() => {
                loading.style.display = "none";
                messageError.textContent = "Đã xảy ra lỗi, vui lòng thử lại!";
                messageError.classList.add("show");
            }, 2000);
        });
});