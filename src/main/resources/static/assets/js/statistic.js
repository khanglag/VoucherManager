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
