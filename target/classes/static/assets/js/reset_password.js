document.addEventListener("DOMContentLoaded", function () {
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
