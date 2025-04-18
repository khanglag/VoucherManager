//hàm xem ảnh
document.addEventListener("DOMContentLoaded", function () {
    const imageUrlInput = document.getElementById("products_imageUrl");
    const imagePreview = document.getElementById("products_image_preview");

    imageUrlInput.addEventListener("input", function () {
        const url = imageUrlInput.value.trim();

        if (url) {
            imagePreview.innerHTML = `<img src="${url}" alt="Xem trước hình ảnh" 
                class="products_preview_image" 
                style="max-width: 100%; height: auto; display: block;">`;
            imagePreview.classList.remove("products_image_preview_empty");
        } else {
            imagePreview.innerHTML = "Xem trước hình ảnh";
            imagePreview.classList.add("products_image_preview_empty");
        }
    });
});
//Hàm hiển thị thông báo
function showNotificationProduct(message, isError = false) {
    const notification = document.getElementById("products_notification");
    if (!notification) return;

    notification.classList.remove("error", "success");
    notification.classList.add(isError ? "error" : "success");

    const icon = isError ? "❌" : "✔️";
    notification.innerHTML = `<span>${icon}</span> ${message}`;

    notification.classList.add("show");

    setTimeout(() => {
        notification.classList.remove("show");

        // if (!isError) {
        //     window.location.reload();
        // }
    }, 1500);
}
// lọc sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const searchInput = document.getElementById("products_search");
    const priceFilter = document.getElementById("products_price_filter");
    const maxPriceDisplay = document.getElementById("products_max_price");
    //const statusFilter = document.getElementById("products_status_filter");
    const productRows = document.querySelectorAll("#products_list .products_row");

    priceFilter.addEventListener("input", function () {
        const priceValue = parseInt(priceFilter.value).toLocaleString("vi-VN") + "₫";
        maxPriceDisplay.textContent = priceValue;
        filterProducts();
    });

    searchInput.addEventListener("input", filterProducts);
 //   statusFilter.addEventListener("change", filterProducts);

    function filterProducts() {
        const searchValue = searchInput.value.trim().toLowerCase();
        const maxPrice = parseInt(priceFilter.value);
      //  const selectedStatus = statusFilter.value;

        productRows.forEach(row => {
            const productId = row.querySelector(".products_td:nth-child(1)").textContent.trim();
            const productName = row.querySelector(".products_td:nth-child(2)").textContent.trim().toLowerCase();
            const productPriceText = row.querySelector(".products_td:nth-child(3)").textContent.trim().replace(/[^\d]/g, "");
            const productPrice = parseInt(productPriceText) || 0;
            const productStatus = row.querySelector(".products_td:nth-child(5)").dataset.status; // Lấy giá trị từ data-status

            const matchesSearch = searchValue === "" || productId.includes(searchValue) || productName.includes(searchValue);
            const matchesPrice = productPrice <= maxPrice;
           // const matchesStatus = (selectedStatus === "all") || (productStatus === selectedStatus);

            row.style.display = (matchesSearch && matchesPrice ) ? "" : "none";
        });
    }
});

//Hàm add sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("products_form");

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const productData = {
            productName: document.getElementById("products_productName").value.trim(),
            price: parseFloat(document.getElementById("products_price").value) || 0,
            imageUrl: document.getElementById("products_imageUrl").value.trim(),
            status: document.getElementById("products_status").value
        };
        fetch('/admin/addProduct', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(productData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    showNotificationProduct(data.error, true);
                } else if (data.message) {
                    showNotificationProduct(data.message, false);
                }
            })
            .catch(error => {
                showNotificationProduct("Đã xảy ra lỗi, vui lòng thử lại!", true);
            });
    });
});
// Format currency
function formatCurrency(amount) {
    return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
// hàm fill thông tin vào model
document.addEventListener("DOMContentLoaded", function () {
    const editModal = new bootstrap.Modal(document.getElementById("products_edit_modal"));
    const deleteModal = new bootstrap.Modal(document.getElementById("products_delete_modal"));

    document.querySelectorAll(".products_btn_edit").forEach(button => {
        button.addEventListener("click", function () {
            const productRow = this.closest(".products_row"); // Lấy hàng chứa sản phẩm
            const productId = productRow.querySelector(".products_td:nth-child(1)").innerText.trim();
            const productName = productRow.querySelector(".products_td:nth-child(2)").innerText.trim();
            const productPrice = productRow.querySelector(".products_td:nth-child(3)").innerText.replace(/[₫,.]/g, '').trim();
            const productImage = productRow.querySelector(".products_image").src;
            const productStatus = productRow.querySelector(".products_td:nth-child(5)").innerText.trim() === "Hoạt động" ? "1" : "0";

            document.getElementById("products_edit_id").value = productId;
            document.getElementById("products_edit_name").value = productName;
            document.getElementById("products_edit_price").value = productPrice;
            document.getElementById("products_edit_image").value = productImage;
            document.getElementById("products_edit_status").value = productStatus;

            previewImage();
            editModal.show();
        });
    });

    document.querySelectorAll(".products_btn_delete").forEach(button => {
        button.addEventListener("click", function () {
            const productRow = this.closest(".products_row");
            const productName = productRow.querySelector(".products_td:nth-child(2)").innerText.trim();
            const productId = productRow.querySelector(".products_td:nth-child(1)").innerText.trim();
            document.getElementById("products_delete_name").innerText = productName;
            document.getElementById("products_confirm_delete").setAttribute("data-id", productId);
            deleteModal.show();
        });
    });
});

// Hàm cập nhật ảnh xem trước
function previewImage() {
    var imageUrl = document.getElementById("products_edit_image").value;
    var previewImg = document.getElementById("products_preview_image");

    if (imageUrl) {
        previewImg.src = imageUrl;
        previewImg.style.display = "block";
    } else {
        previewImg.style.display = "none";
    }
}

//hàm sửa sp
document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("products_edit_form").addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn chặn form reload trang

        const productData = {
            id: document.getElementById("products_edit_id").value.trim(),
            name: document.getElementById("products_edit_name").value.trim(),
            price: document.getElementById("products_edit_price").value.trim(),
            image: document.getElementById("products_edit_image").value.trim(),
            status: document.getElementById("products_edit_status").value
        };

        fetch('/admin/editProduct', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            },
            body: JSON.stringify(productData)
        })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    showNotificationProduct(data.error, true);
                } else if (data.message) {
                    showNotificationProduct(data.message, false);
                }
            })
            .catch(error => {
                showNotificationProduct("Đã xảy ra lỗi, vui lòng thử lại!", true);
            });
    });
});

// Khóa sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const deleteModal = new bootstrap.Modal(document.getElementById("products_delete_modal"));
    const deleteButton = document.getElementById("products_confirm_delete");

    let selectedProductId = null;
    document.querySelectorAll(".products_btn_delete").forEach(button => {
        button.addEventListener("click", function () {
            selectedProductId = this.getAttribute("data-id");
            const productName = this.closest(".products_row").querySelector(".products_td:nth-child(2)").innerText.trim();
            document.getElementById("products_delete_name").innerText = productName;
            deleteModal.show();
        });
    });
    deleteButton.addEventListener("click", function () {
        if (selectedProductId) {
            const productData = { id: selectedProductId };
            fetch('/admin/blockProduct', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json"
                },
                body: JSON.stringify(productData)
            })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        showNotificationProduct(data.error, true);
                    } else if (data.message) {
                        showNotificationProduct(data.message, false);
                    }
                })
                .catch(error => {
                    showNotificationProduct("Đã xảy ra lỗi, vui lòng thử lại!", true);
                });
            deleteModal.hide();
        }
    });
});

// phân trang
document.addEventListener("DOMContentLoaded", function () {
    const productsPerPage = 15;
    let currentProductPage = 1;

    const productTableBody = document.getElementById("products_list");
    const productRows = Array.from(productTableBody.querySelectorAll("tr"));

    const prevProductBtn = document.getElementById("prevProductPage");
    const nextProductBtn = document.getElementById("nextProductPage");
    const productPageInfo = document.getElementById("productPageInfo");

    const productSearchInput = document.getElementById("products_search");
    const priceFilter = document.getElementById("products_price_filter");
    const statusFilter = document.getElementById("products_status_filter");

    function filterProducts() {
        const searchText = productSearchInput.value.toLowerCase();
        const maxPrice = parseInt(priceFilter.value);
     //   const selectedStatus = statusFilter.value.toLowerCase();

        return productRows.filter(row => {
            const productId = row.cells[0].textContent.toLowerCase();
            const productName = row.cells[1].textContent.toLowerCase();
            const productPrice = parseInt(row.cells[2].textContent.replace(/[^\d]/g, ""));
            const productStatus = row.cells[4].textContent.toLowerCase();

            const matchesSearch = productId.includes(searchText) || productName.includes(searchText);
            const matchesPrice = productPrice <= maxPrice;
          //  const matchesStatus = selectedStatus === "all" || productStatus.includes(selectedStatus);

            return matchesSearch && matchesPrice ;
        });
    }

    function renderProductTable(filteredProducts) {
        const totalPages = Math.ceil(filteredProducts.length / productsPerPage);
        currentProductPage = Math.min(currentProductPage, totalPages) || 1;

        productRows.forEach(row => row.style.display = "none");
        filteredProducts.forEach((row, index) => {
            row.style.display = (index >= (currentProductPage - 1) * productsPerPage && index < currentProductPage * productsPerPage) ? "" : "none";
        });

        productPageInfo.textContent = `Trang ${currentProductPage} / ${totalPages || 1}`;
        prevProductBtn.disabled = currentProductPage === 1;
        nextProductBtn.disabled = currentProductPage === totalPages || totalPages === 0;
    }

    function updateProductTable() {
        renderProductTable(filterProducts());
    }

    prevProductBtn.addEventListener("click", function () {
        if (currentProductPage > 1) {
            currentProductPage--;
            updateProductTable();
        }
    });

    nextProductBtn.addEventListener("click", function () {
        if (currentProductPage < Math.ceil(filterProducts().length / productsPerPage)) {
            currentProductPage++;
            updateProductTable();
        }
    });

    productSearchInput.addEventListener("input", updateProductTable);
    priceFilter.addEventListener("input", updateProductTable);
   // statusFilter.addEventListener("change", updateProductTable);

    updateProductTable();
});

