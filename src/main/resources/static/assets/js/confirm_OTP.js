document.addEventListener("DOMContentLoaded", function () {
    const inputs = document.querySelectorAll(".otp-input");
    const hiddenInput = document.getElementById("otp-hidden");
    const resendOTPButton = document.getElementById("resend-otp");
    const reloadPageButton = document.getElementById("reload-page");

    function focusFirstInput() {
        if (inputs.length > 0) {
            inputs.forEach(input => input.value = ""); // Xóa dữ liệu cũ
            inputs[0].focus();
        }
    }

    if (inputs.length > 0) {
        focusFirstInput();

        inputs.forEach((input, index) => {
            input.addEventListener("input", (e) => {
                if (e.target.value && index < inputs.length - 1) {
                    inputs[index + 1].focus();
                }
            });

            input.addEventListener("keydown", (e) => {
                if (e.key === "Backspace" && !e.target.value && index > 0) {
                    inputs[index - 1].focus();
                }
            });
        });

        document.getElementById("confirm-OTP-form").addEventListener("submit", function (e) {
            let otp = "";
            inputs.forEach(input => otp += input.value);
            hiddenInput.value = otp;
        });
    } else {
        console.error("Không tìm thấy input OTP");
    }

    if (resendOTPButton) {
        resendOTPButton.addEventListener("click", function (e) {
            e.preventDefault();

            fetch("/resend-OTP")
                .then(response => response.text())
                .then(data => {
                    document.body.innerHTML = data; // Cập nhật toàn bộ trang
                    setTimeout(focusFirstInput, 100); // Chờ một chút để đảm bảo các phần tử đã được render
                })
                .catch(error => console.error("Lỗi khi gửi lại OTP:", error));
        });
    } else {
        console.error("Không tìm thấy nút resend-otp");
    }

    let alertBox = document.querySelector(".alert");
    if (alertBox) {
        alertBox.classList.add('show'); // Hiển thị thông báo
        setTimeout(() => {
            alertBox.style.opacity = "0"; // Làm mờ dần
            setTimeout(() => {
                alertBox.classList.remove('show'); // Ẩn sau khi mờ
            }, 500); // Thời gian hoàn tất quá trình làm mờ
        }, 3000); // Ẩn sau 3 giây
    }
});
