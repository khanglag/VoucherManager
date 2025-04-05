function submitAddVoucher(event){
    event.preventDefault();
    const title = document.getElementById("title").value;
    const voucherCode = document.getElementById("voucherCode").value;
    const discountType = document.getElementById("discountType").value;
    const discountValue = document.getElementById("discountValue").value;
    const minimumOrderValue = document.getElementById("minimumOrderValue").value;
    const maxUsage = document.getElementById("maxUsage").value;
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const description = document.getElementById("description").value;
    const applicableForAllProducts = document.getElementById("applicableForAllProducts").checked;

    const requiredFields = [
        { id: "title", value: title, name: "Tên voucher" },
        { id: "voucherCode", value: voucherCode, name: "Mã voucher" },
        { id: "voucherType", value: discountType, name: "Loại giảm giá" },
        { id: "discountValue", value: discountValue, name: "Giá trị giảm giá" },
        { id: "minimumOrderValue", value: minimumOrderValue, name: "Giá trị đơn hàng tối thiểu" },
        { id: "maxUsage", value: maxUsage, name: "Số lần sử dụng tối đa" },
        { id: "startDate", value: startDate, name: "Ngày bắt đầu" },
        { id: "endDate", value: endDate, name: "Ngày kết thúc" },
        { id: "description", value: description, name: "Mô tả" }
    ];

    for (const field of requiredFields) {
        if (!field.value) {
            alert(`Vui lòng nhập ${field.name}`);
            document.getElementById(field.id).focus();
            return;
        }
    }
    if (discountType === "PERCENTAGE" && parseFloat(discountValue) >= 100) {
        showNotification("Không được vượt quá 100%", true);
        return;
    }
    const jsonData = {
        title,
        voucherCode,
        discountType,
        discountValue,
        minimumOrderValue,
        maxUsage,
        startDate,
        endDate,
        description,
        applicableForAllProducts,
        createdDate: new Date().toISOString(),
        createBy: 1,
        usageCount: 0
    };
    fetch('/admin/addVoucher', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(jsonData)
    })
        .then(response => response.json())
        .then(data => {
         if (data.suggestions && data.suggestions.length > 0) {
                showNotification(data.message || "Thêm voucher thành công!");
             document.getElementById("title").value = "";
             document.getElementById("voucherCode").value = "";
             document.getElementById("discountType").value = "";
             document.getElementById("discountValue").value = "";
             document.getElementById("minimumOrderValue").value = "";
             document.getElementById("maxUsage").value = "";
             document.getElementById("startDate").value = "";
             document.getElementById("endDate").value = "";
             document.getElementById("description").value = "";
             document.getElementById("applicableForAllProducts").checked = false;
            } else {
                showNotification(data.message || "Có lỗi xảy ra!", true);
            }
        })
        .catch(error => {
            console.error('Đã xảy ra lỗi:', error);
            showNotification("Lỗi kết nối đến server!", true);
        });

}

// hàm hiển thị thông báo thành công, thất bại
function showNotification(message, isError = false) {
    const notification = document.getElementById("notification");
    notification.textContent = message;
    if (isError) {
        notification.style.backgroundColor = "#ff4d4d";
    } else {
        notification.style.backgroundColor = "#4CAF50";
    }
    notification.classList.add("show");
    setTimeout(() => {
        notification.classList.remove("show");
        window.location.reload();
    }, 3000);
}


//button edit voucher
function editVoucher(button) {
    let row = button.closest('tr');
    document.getElementById('model_voucherCode').value = row.cells[0].innerText;
    document.getElementById('model_title').value = row.cells[1].innerText;
    document.getElementById('model_description').value = row.cells[2].innerText;
    let discountTypeText = row.cells[3].innerText.trim();
    let discountTypeValue = discountTypeText === 'Giảm Phần Trăm' ? 'PERCENTAGE' :
        discountTypeText === 'Giảm Tiền Cố Định' ? 'FIXED' : 'FREESHIP';
    document.getElementById('model_discountType').value = discountTypeValue;

    let discountValueText = row.cells[4].innerText.replace(/đ/g, '').replace('%', '').replace(/,/g, '').trim();
    document.getElementById('model_discountValue').value = parseFloat(discountValueText);

    let minimumOrderValueText = row.cells[5].innerText.replace(/đ/g, '').replace(/,/g, '').trim();
    document.getElementById('model_minimumOrderValue').value = parseFloat(minimumOrderValueText);
    document.getElementById('model_startDate').value = formatDate(row.cells[6].innerText);
    document.getElementById('model_endDate').value = formatDate(row.cells[7].innerText);
    document.getElementById('model_maxUsage').value = row.cells[9].innerText;

    let statusText = row.cells[11].innerText.trim();
    console.log(statusText)
    document.getElementById('model_status').value = statusText === 'ACTIVE' ? 'ACTIVE' :
        statusText === 'EXPIRED' ? 'EXPIRED' : 'CANCELLED';
}


function formatDate(dateString) {
    let parts = dateString.split('/');
    return `${parts[2]}-${parts[1]}-${parts[0]}`;
}

//trường ô input áp dụng cho sản phẩm cố định
function applyProduct() {
    let productIdInput = document.getElementById('model_productId');
    let appliedProductsTextarea = document.getElementById('model_appliedProducts');

    let productId = productIdInput.value.trim();
    if (productId === '' || isNaN(productId)) {
        alert('Vui lòng nhập ID sản phẩm hợp lệ!');
        return;
    }
    let currentProducts = appliedProductsTextarea.value.split(',').map(id => id.trim()).filter(id => id !== '');
    if (!currentProducts.includes(productId)) {
        currentProducts.push(productId);
        appliedProductsTextarea.value = currentProducts.join(', ');
    }
    productIdInput.value = '';
}
function fetchEditVoucher() {
    let voucherData = {
        voucherCode: document.getElementById('model_voucherCode').value,
        title: document.getElementById('model_title').value,
        description: document.getElementById('model_description').value,
        discountType: document.getElementById('model_discountType').value,
        discountValue: parseFloat(document.getElementById('model_discountValue').value),
        minimumOrderValue: parseFloat(document.getElementById('model_minimumOrderValue').value),
        startDate: document.getElementById('model_startDate').value,
        endDate: document.getElementById('model_endDate').value,
        maxUsage: parseInt(document.getElementById('model_maxUsage').value),
        status: document.getElementById('model_status').value,
        appliedProducts: document.getElementById('model_appliedProducts').value.split(',').map(id => id.trim()).filter(id => id !== '')
    };

    console.log(JSON.stringify(voucherData, null, 4));
    fetch('/admin/editVoucher', {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        },
        body: JSON.stringify(voucherData)
    })
        .then(response => {
            if (!response.ok) {
                console.log("Lỗi")
            }
            return response.json();
        })
        .then(data => {
            if (!data.success) {
                showNotification(data.message || "Update voucher thất bại");

            } else {
                showNotification(data.message || "Update voucher thành công!");

            }
        })
        .catch(error => {
            console.error("Lỗi khi cập nhật voucher:", error);
            alert("Có lỗi xảy ra khi cập nhật voucher. Vui lòng thử lại!");
        });
}

//lọc
document.addEventListener("DOMContentLoaded", function () {
    const applyFilterBtn = document.getElementById("applyFilter");
    const resetFilterBtn = document.getElementById("resetFilter");

    const filters = {
        code: document.getElementById("filterCode"),
        type: document.getElementById("filterType"),
        status: document.getElementById("filterStatus"),
        startDate: document.getElementById("filterStartDate"),
        endDate: document.getElementById("filterEndDate"),
        efficiencyMin: document.getElementById("filterEfficiencyMin"),
    };

    const tableRows = document.querySelectorAll(".voucher-table tbody tr");

    function filterTable() {
        tableRows.forEach(row => {
            const code = row.cells[0].textContent.trim().toLowerCase();
            const type = row.cells[3].textContent.trim();
            const status = row.cells[11].textContent.trim();
            const startDate = row.cells[6].textContent.trim();
            const endDate = row.cells[7].textContent.trim();
            const efficiency = parseFloat(row.cells[10].textContent.replace("%", "")) || 0;

            let showRow = true;

            if (filters.code.value && !code.includes(filters.code.value.toLowerCase())) {
                showRow = false;
            }
            if (filters.type.value && type !== filters.type.value) {
                showRow = false;
            }
            if (filters.status.value && status !== filters.status.value) {
                showRow = false;
            }
            if (filters.startDate.value && new Date(startDate.split("/").reverse().join("-")) < new Date(filters.startDate.value)) {
                showRow = false;
            }
            if (filters.endDate.value && new Date(endDate.split("/").reverse().join("-")) > new Date(filters.endDate.value)) {
                showRow = false;
            }
            if (filters.efficiencyMin.value && efficiency < parseFloat(filters.efficiencyMin.value)) {
                showRow = false;
            }

            row.style.display = showRow ? "" : "none";
        });
    }

    applyFilterBtn.addEventListener("click", filterTable);

    resetFilterBtn.addEventListener("click", function () {
        Object.values(filters).forEach(input => input.value = "");
        filterTable();
    });
});