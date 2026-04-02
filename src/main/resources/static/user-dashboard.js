const user = JSON.parse(localStorage.getItem("loggedInUser"));

if(!user){
    window.location.href="login.html";
}

function logout(){
    localStorage.removeItem("loggedInUser");
    window.location.href="login.html";
}

function addData(){
    window.location.href="add-geo.html";
}

// Pagination state
let allGeoData = [];
let currentPage = 1;
const pageSize = 10;

// ==========================
// PAGINATION HELPER FUNCTIONS
// ==========================
function paginate(array, page, size) {
    const startIndex = (page - 1) * size;
    const endIndex = startIndex + size;
    return array.slice(startIndex, endIndex);
}

function getTotalPages(arrayLength, size) {
    return Math.ceil(arrayLength / size);
}

function createPaginationControls(totalPages, currentPage) {
    let html = '<div style="display:flex;justify-content:center;align-items:center;gap:10px;margin-top:20px;">';

    // Previous button
    html += `<button onclick="changePage(${currentPage - 1})"
             style="padding:8px 16px;background:#8b5cf6;color:white;border:none;border-radius:5px;cursor:pointer;"
             ${currentPage === 1 ? 'disabled style="opacity:0.5;cursor:not-allowed;"' : ''}>
             Previous
             </button>`;

    // Page numbers
html += '<span class="pagination-info">Page ' + currentPage + ' of ' + totalPages + '</span>';

    // Next button
    html += `<button onclick="changePage(${currentPage + 1})"
             style="padding:8px 16px;background:#8b5cf6;color:white;border:none;border-radius:5px;cursor:pointer;"
             ${currentPage >= totalPages ? 'disabled style="opacity:0.5;cursor:not-allowed;"' : ''}>
             Next
             </button>`;

    html += '</div>';
    return html;
}

function changePage(newPage) {
    const totalPages = getTotalPages(allGeoData.length, pageSize);
    if (newPage < 1 || newPage > totalPages) return;
    currentPage = newPage;
    renderTable(allGeoData);
}

// Make changePage globally accessible
window.changePage = changePage;

// ==========================
// RENDER TABLE WITH PAGINATION
// ==========================
function renderTable(data) {
    const totalPages = getTotalPages(data.length, pageSize);
    const paginatedData = paginate(data, currentPage, pageSize);

    let table = "";
    let active = 0;

    paginatedData.forEach(g => {
        if(g.status === 1) active++;

        table += `
        <tr>
            <td>${g.id}</td>
            <td>${g.latitude}</td>
            <td>${g.longitude}</td>
            <td>${g.description || 'N/A'}</td>
            <td>${g.remarks || 'N/A'}</td>
            <td><span class="badge">ACTIVE</span></td>
        </tr>
        `;
    });

    document.getElementById("geoTable").innerHTML = table;
    document.getElementById("totalRecords").innerText = allGeoData.length;

    // Count active from all data, not just paginated
    let totalActive = allGeoData.filter(g => g.status === 1).length;
    document.getElementById("activeRecords").innerText = totalActive;

    // Add pagination controls
    const paginationHtml = createPaginationControls(totalPages, currentPage);
    const tableBox = document.querySelector(".table-box");

    // Remove old pagination if exists
    const oldPagination = tableBox.querySelector('.pagination-controls');
    if (oldPagination) oldPagination.remove();

    const paginationDiv = document.createElement('div');
    paginationDiv.className = 'pagination-controls';
    paginationDiv.innerHTML = paginationHtml;
    tableBox.appendChild(paginationDiv);
}

// ==========================
// LOAD DATA
// ==========================
function loadData(){
    fetch("http://localhost:8080/api/geo/user/" + user.userName)
    .then(res => res.json())
    .then(data => {
        allGeoData = data; // Store all data
        renderTable(data);
    })
    .catch(error => {
        console.error("Error loading data:", error);
    });
}

loadData();

// ==========================
// SEARCH FUNCTIONALITY
// ==========================
document.getElementById("searchGeo").addEventListener("input", function() {
    const filter = this.value.toLowerCase();
    const filtered = allGeoData.filter(g => {
        return (g.description && g.description.toLowerCase().includes(filter)) ||
               (g.remarks && g.remarks.toLowerCase().includes(filter)) ||
               (g.latitude && g.latitude.toString().includes(filter)) ||
               (g.longitude && g.longitude.toString().includes(filter));
    });
    currentPage = 1; // Reset to first page on search
    renderTable(filtered);
});