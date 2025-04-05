function filterTable() {
    let searchId = document.getElementById("searchId").value.toLowerCase();
    let searchName = document.getElementById("searchName").value.toLowerCase();
    let searchVoucher = document.getElementById("searchVoucher").value.toLowerCase();

    let table = document.getElementById("voucherTableBody");
    let rows = table.getElementsByTagName("tr");

    for (let i = 0; i < rows.length; i++) {
        let id = rows[i].getElementsByTagName("td")[0].innerText.toLowerCase();
        let name = rows[i].getElementsByTagName("td")[1].innerText.toLowerCase();
        let voucher = rows[i].getElementsByTagName("td")[2].innerText.toLowerCase();

        let matchId = searchId === "" || id.includes(searchId);
        let matchName = searchName === "" || name.includes(searchName);
        let matchVoucher = searchVoucher === "" || voucher.includes(searchVoucher);

        rows[i].style.display = (matchId && matchName && matchVoucher) ? "" : "none";
    }
}

let sortAscending = true;
function sortEfficiency() {
    let table = document.getElementById("voucherTableBody");
    let rows = Array.from(table.getElementsByTagName("tr"));

    rows.sort((rowA, rowB) => {
        let efficiencyA = parseFloat(rowA.getElementsByTagName("td")[6].innerText.replace("%", ""));
        let efficiencyB = parseFloat(rowB.getElementsByTagName("td")[6].innerText.replace("%", ""));
        return sortAscending ? efficiencyA - efficiencyB : efficiencyB - efficiencyA;
    });

    rows.forEach(row => table.appendChild(row));
    sortAscending = !sortAscending;
}
//xuất pdf
function exportToPDF() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    doc.addFont(`data:font/ttf;base64,${ArialFont}`, 'Arial', 'normal');
    doc.setFont('Arial');
    doc.setFontSize(16);
    doc.text("Báo cáo hiệu suất Voucher của từng nhân viên", 14, 15);
    const table = document.querySelector(".statisticEfficiency_voucher-table");
    doc.autoTable({
        html: table,
        startY: 20,
        theme: 'grid',
        headStyles: { fillColor: [41, 128, 185] },
        styles: { font: "Arial", fontSize: 10 },
        columnStyles: { 6: { halign: 'right' } }
    });
    doc.save("BaoCao_Voucher.pdf");
}
// thống kê của admin
function filterTableByAdmin() {
    let inputId = document.getElementById("searchIdByAdmin").value.toUpperCase();
    let inputName = document.getElementById("searchNameByAdmin").value.toUpperCase();
    let inputVoucher = document.getElementById("searchVoucherByAdmin").value.toUpperCase();
    let table = document.querySelector(".statisticEfficiency_voucher-tableByAdmin");
    let rows = table.getElementsByTagName("tr");

    for (let i = 1; i < rows.length; i++) {
        let cols = rows[i].getElementsByTagName("td");
        let idMatch = cols[0].innerText.toUpperCase().includes(inputId);
        let nameMatch = cols[1].innerText.toUpperCase().includes(inputName);
        let voucherMatch = cols[2].innerText.toUpperCase().includes(inputVoucher);

        if (idMatch && nameMatch && voucherMatch) {
            rows[i].style.display = "";
        } else {
            rows[i].style.display = "none";
        }
    }
}

let sortAscByAdmin = true;

function sortEfficiencyByAdmin() {
    const table = document.querySelector(".statisticEfficiency_voucher-tableByAdmin");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));
    const sortedRows = rows.sort((a, b) => {
        const aValue = parseFloat(a.cells[6].innerText.replace("%", "")) || 0;
        const bValue = parseFloat(b.cells[6].innerText.replace("%", "")) || 0;
        return sortAscByAdmin ? bValue - aValue : aValue - bValue;
    });
    tbody.querySelectorAll(".sorted-row").forEach(row => row.classList.remove("sorted-row"));
    tbody.innerHTML = "";
    sortedRows.forEach((row, index) => {
        tbody.appendChild(row);
        if (index === 0) row.classList.add("sorted-row");
    });
    setTimeout(() => {
        tbody.querySelectorAll(".sorted-row").forEach(row => row.classList.remove("sorted-row"));
    }, 1000);
    sortAscByAdmin = !sortAscByAdmin;
}

function exportToPDFByAdmin() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.addFont(`data:font/ttf;base64,${ArialFont}`, 'Arial', 'normal');

    doc.setFont('Arial');
    doc.setFontSize(16);
    doc.text("Báo cáo hiệu suất Voucher của Admin", 14, 15);

    const table = document.querySelector(".statisticEfficiency_voucher-tableByAdmin");

    doc.autoTable({
        html: table,
        startY: 20,
        theme: "grid",
        headStyles: { fillColor: [192, 57, 43] },
        styles: { font: "times", fontSize: 10 },
        columnStyles: { 6: { halign: "right" } }
    });

    doc.save("BaoCao_Voucher_Admin.pdf");
}
//Phân trang cho table thống kê admin
const rowsPerPageByAdmin = 15;
let currentPageByAdmin = 1;

function paginateTableByAdmin() {
    const tableBody = document.getElementById("voucherTableBodyByAdmin");
    const rows = Array.from(tableBody.querySelectorAll("tr"));
    const totalPages = Math.ceil(rows.length / rowsPerPageByAdmin);
    rows.forEach(row => (row.style.display = "none"));
    const start = (currentPageByAdmin - 1) * rowsPerPageByAdmin;
    const end = start + rowsPerPageByAdmin;
    rows.slice(start, end).forEach(row => (row.style.display = "table-row"));
    updatePaginationControlsByAdmin(totalPages);
}

function updatePaginationControlsByAdmin(totalPages) {
    const pageInfo = document.getElementById("pageInfoByAdmin");
    const prevButton = document.getElementById("prevPageByAdmin");
    const nextButton = document.getElementById("nextPageByAdmin");
    pageInfo.innerText = `Trang ${currentPageByAdmin} / ${totalPages}`;
    prevButton.disabled = currentPageByAdmin === 1;
    nextButton.disabled = currentPageByAdmin === totalPages;
    prevButton.onclick = () => {
        if (currentPageByAdmin > 1) {
            currentPageByAdmin--;
            paginateTableByAdmin();
        }
    };
    nextButton.onclick = () => {
        if (currentPageByAdmin < totalPages) {
            currentPageByAdmin++;
            paginateTableByAdmin();
        }
    };
}
document.addEventListener("DOMContentLoaded", paginateTableByAdmin);

//Phân trang cho thống kê của staff
const rowsPerPage = 15;
let currentPage = 1;

function paginateTable() {
    const tableBody = document.getElementById("voucherTableBody");
    const rows = Array.from(tableBody.querySelectorAll("tr"));
    const totalPages = Math.max(1, Math.ceil(rows.length / rowsPerPage)); // Đảm bảo luôn có ít nhất 1 trang
    rows.forEach(row => (row.style.display = "none"));
    const start = (currentPage - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    rows.slice(start, end).forEach(row => (row.style.display = "table-row"));
    updatePaginationControls(totalPages);
}

function updatePaginationControls(totalPages) {
    const pageInfo = document.getElementById("pageInfo");
    const prevButton = document.getElementById("prevPage");
    const nextButton = document.getElementById("nextPage");
    pageInfo.innerText = `Trang ${currentPage} / ${totalPages}`;
    prevButton.disabled = currentPage <= 1;
    nextButton.disabled = currentPage >= totalPages;
    prevButton.onclick = () => {
        if (currentPage > 1) {
            currentPage--;
            paginateTable();
        }
    };
    nextButton.onclick = () => {
        if (currentPage < totalPages) {
            currentPage++;
            paginateTable();
        }
    };
}
document.addEventListener("DOMContentLoaded", paginateTable);
//thống kê doanh thu theo tháng
let revenueChart;

function fetchRevenueData() {
    const year = document.getElementById('yearSelect').value;
    fetch(`/admin/revenue-data?year=${year}`)
        .then(response => response.json())
        .then(data => {
            const monthlyTotals = Array(12).fill(0);

            data.forEach(item => {
                const month = item.month - 1;
                monthlyTotals[month] = item.totalAmount;
            });

            renderRevenueChart(monthlyTotals, year);
        })
        .catch(err => console.error('Lỗi khi lấy dữ liệu doanh thu:', err));
}

function renderRevenueChart(monthlyTotals, year) {
    const ctx = document.getElementById('revenueChart').getContext('2d');

    if (revenueChart) {
        revenueChart.destroy();
    }

    revenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: [
                'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
                'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
            ],
            datasets: [{
                label: `Doanh thu năm ${year}`,
                data: monthlyTotals,
                borderColor: '#3e95cd',
                backgroundColor: 'rgba(62,149,205,0.2)',
                tension: 0.3,
                fill: true,
                pointRadius: 5,
                pointHoverRadius: 7,
            }]
        },
        options: {
            responsive: true,
            animation: {
                duration: 1000,
                easing: 'easeOutQuart'
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value.toLocaleString('vi-VN') + ' đ';
                        }
                    }
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) label += ': ';
                            label += Number(context.raw).toLocaleString('vi-VN') + ' đ';
                            return label;
                        }
                    }
                }
            }
        }
    });
    // Tạo bảng từ dữ liệu chart
    generateRevenueTableData(monthlyTotals, year);
}
// Tạo bảng doanh thu
function generateRevenueTableData(monthlyTotals, year) {
    const tbody = document.getElementById("hiddenRevenueBody");
    const yearLabel = document.getElementById("revenueYear");
    yearLabel.textContent = year;
    tbody.innerHTML = "";

    monthlyTotals.forEach((amount, i) => {
        const row = document.createElement("tr");

        const monthCell = document.createElement("td");
        monthCell.style.padding = "10px";
        monthCell.style.border = "1px solid #ddd";
        monthCell.textContent = i + 1;

        const yearCell = document.createElement("td");
        yearCell.style.padding = "10px";
        yearCell.style.border = "1px solid #ddd";
        yearCell.textContent = year;

        const revenueCell = document.createElement("td");
        revenueCell.style.padding = "10px";
        revenueCell.style.border = "1px solid #ddd";
        revenueCell.textContent = amount.toLocaleString('vi-VN') + ' đ';

        row.appendChild(monthCell);
        row.appendChild(yearCell);
        row.appendChild(revenueCell);

        tbody.appendChild(row);
    });
}

// Export PDF từ bảng ẩn
async function exportHiddenRevenueToPDF() {
    const { jsPDF } = window.jspdf;
    const pdf = new jsPDF('p', 'mm', 'a4');

    // Thêm font Arial đã mã hóa base64
    const arialBase64 = ArialFont; // Thay bằng chuỗi base64 của bạn

    pdf.addFileToVFS('Arial-normal.ttf', arialBase64);
    pdf.addFont('Arial-normal.ttf', 'Arial', 'normal');
    pdf.setFont('Arial');

    const container = document.getElementById("hiddenRevenueTableContainer");
    container.style.display = "block";
    const year = document.getElementById('revenueYear').textContent;

    // Tiêu đề
    pdf.setFontSize(18);
    pdf.text(`BẢNG DOANH THU NĂM ${year}`, 15, 20);

    // Xử lý bảng
    pdf.autoTable({
        html: '#hiddenRevenueTable',
        startY: 30,
        theme: 'grid',
        styles: {
            font: 'Arial',
            fontSize: 12,
            cellPadding: 3,
            valign: 'middle',
            fontStyle: 'normal'
        },
        headStyles: {
            fillColor: [52, 58, 64],
            textColor: 255,
            fontStyle: 'bold',
            font: 'Arial'
        },
        bodyStyles: {
            font: 'Arial'
        },
        columnStyles: {
            0: { halign: 'center' },
            1: { halign: 'center' },
            2: {
                halign: 'right',
                fontStyle: 'normal'
            }
        }
    });

    pdf.save(`Bang_DoanhThu_${year}.pdf`);
    container.style.display = "none";
}
// Gọi lần đầu khi load trang
window.onload = fetchRevenueData;