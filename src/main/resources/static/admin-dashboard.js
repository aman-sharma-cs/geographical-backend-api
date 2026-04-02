// ==========================
// LOGOUT
// ==========================
function logout() {
    localStorage.removeItem("loggedInUser");
    window.location.href = "login.html";
}

// ==========================
// GLOBAL STATE
// ==========================
let allUsers = [];
let allGeoData = [];
let geoData = [];

let usersCurrentPage = 1;
let geoCurrentPage = 1;
const pageSize = 10;

// ==========================
// PAGINATION HELPERS
// ==========================
function paginate(array, page, size) {
    const start = (page - 1) * size;
    return array.slice(start, start + size);
}

function getTotalPages(length, size) {
    return Math.ceil(length / size);
}

function createPaginationControls(totalPages, currentPage, fnName) {
    return `
    <div class="pagination-controls">
        <button onclick="${fnName}(${currentPage - 1})" ${currentPage === 1 ? "disabled" : ""} data-translate>Previous</button>
        <span class="pagination-info">Page ${currentPage} of ${totalPages}</span>
        <button onclick="${fnName}(${currentPage + 1})" ${currentPage >= totalPages ? "disabled" : ""} data-translate>Next</button>
    </div>`;
}

// ==========================
// DELETE USER
// ==========================
function deleteUser(userId) {
    if (!confirm("Are you sure you want to deactivate this user?")) return;

    fetch(`http://localhost:8080/api/user/delete/${userId}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Delete failed");
        // Update local array
        const index = allUsers.findIndex(u => u.id === userId);
        if (index !== -1) allUsers[index].status = 0;
        renderUsersTable(allUsers);
        alert("User deactivated successfully!");
    })
    .catch(err => {
        console.error(err);
        alert("Failed to deactivate user");
    });
}
window.deleteUser = deleteUser;

// ==========================
// EDIT USER
// ==========================
function editUser(userId) {
    const user = allUsers.find(u => u.id === userId);
    if (!user) return alert("User not found");

    const newName = prompt("Enter new username:", user.userName);
    if (newName === null) return;

    const newRole = prompt("Enter new role (ADMIN/USER):", user.role);
    if (newRole === null) return;

    const payload = { userName: newName.trim(), role: newRole.trim() };

    fetch(`http://localhost:8080/api/user/${userId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if (!res.ok) throw new Error("Update failed");
        return res.json();
    })
    .then(updatedUser => {
        const index = allUsers.findIndex(u => u.id === userId);
        if (index !== -1) allUsers[index] = updatedUser;
        renderUsersTable(allUsers);
        alert("User updated successfully!");
    })
    .catch(err => {
        console.error(err);
        alert("Failed to update user");
    });
}
window.editUser = editUser;

// ==========================
// USERS
// ==========================
function loadUsers() {
    fetch("http://localhost:8080/api/user/all")
        .then(res => res.json())
        .then(data => {
            allUsers = data;
            usersCurrentPage = 1;
            renderUsersTable(allUsers);
            attachSearchListeners();
        })
        .catch(err => console.error("Users load error:", err));
}

function renderUsersTable(users) {
    const totalPages = getTotalPages(users.length, pageSize);
    const pageData = paginate(users, usersCurrentPage, pageSize);

    let html = "";
    pageData.forEach(u => {
        const statusText = u.status === 1 ? "ACTIVE" : "INACTIVE";
        html += `
        <tr>
            <td>${u.id}</td>
            <td>${u.userName}</td>
            <td>${u.email}</td>
            <td>${u.role}</td>
            <td>${u.createdDate || u.createdAt || 'N/A'}</td>
            <td>
                <button class="btn edit-btn" onclick="editUser(${u.id})">Edit</button>
                <button class="btn delete-btn" onclick="deleteUser(${u.id})">Delete</button>
            </td>
        </tr>`;
    });

    const container = document.getElementById("usersTable").closest(".table-box");
    container.querySelectorAll(".pagination-controls").forEach(e => e.remove());
    container.insertAdjacentHTML("beforeend", createPaginationControls(totalPages, usersCurrentPage, "changeUsersPage"));

    document.getElementById("usersTable").innerHTML = html;
    document.getElementById("totalUsers").innerText = users.length;
    document.getElementById("adminUsers").innerText = users.filter(u => u.role === "ROLE_ADMIN" || u.role === "ADMIN").length;
}

function changeUsersPage(p) {
    if (p < 1 || p > getTotalPages(allUsers.length, pageSize)) return;
    usersCurrentPage = p;
    renderUsersTable(allUsers);
}
window.changeUsersPage = changeUsersPage;

// ==========================
// GEO DATA
// ==========================
function loadGeo() {
    fetch("http://localhost:8080/api/geo/all")
        .then(res => res.json())
        .then(data => {
            allGeoData = data;
            geoData = data;
            geoCurrentPage = 1;
            renderGeoTable(allGeoData);
            if (typeof addMarkers === "function") addMarkers();
        })
        .catch(err => console.error("Geo load error:", err));
}

function renderGeoTable(arr) {
    const totalPages = getTotalPages(arr.length, pageSize);
    const pageData = paginate(arr, geoCurrentPage, pageSize);

    let html = "";
    pageData.forEach(g => {
        const username = g.user ? g.user.userName : (g.username || "N/A");
        const status = g.status === 1 ? "ACTIVE" : "INACTIVE";

        html += `
        <tr>
            <td>${g.id}</td>
            <td>${username}</td>
            <td>${g.latitude}</td>
            <td>${g.longitude}</td>
            <td>${g.description || 'N/A'}</td>
            <td>${g.remarks || 'N/A'}</td>
            <td><span class="badge">${status}</span></td>
        </tr>`;
    });

    const container = document.getElementById("geoTable").closest(".table-box");
    container.querySelectorAll(".pagination-controls").forEach(e => e.remove());
    container.insertAdjacentHTML("beforeend", createPaginationControls(totalPages, geoCurrentPage, "changeGeoPage"));

    document.getElementById("geoTable").innerHTML = html;
    document.getElementById("totalGeo").innerText = arr.length;
}

function changeGeoPage(p) {
    if (p < 1 || p > getTotalPages(allGeoData.length, pageSize)) return;
    geoCurrentPage = p;
    renderGeoTable(allGeoData);
}
window.changeGeoPage = changeGeoPage;

// ==========================
// SEARCH
// ==========================
function attachSearchListeners() {
    document.getElementById("searchUsers").oninput = function () {
        const val = this.value.toLowerCase();
        const filtered = allUsers.filter(u =>
            (u.userName || "").toLowerCase().includes(val) ||
            (u.email || "").toLowerCase().includes(val) ||
            (u.role || "").toLowerCase().includes(val)
        );
        usersCurrentPage = 1;
        renderUsersTable(filtered);
    };

    document.getElementById("searchGeoData").oninput = function () {
        const val = this.value.toLowerCase();
        const filtered = allGeoData.filter(g => {
            const username = g.user ? g.user.userName : (g.username || "");
            return username.toLowerCase().includes(val) ||
                (g.description || "").toLowerCase().includes(val);
        });
        geoCurrentPage = 1;
        renderGeoTable(filtered);
    };
}

// ==========================
// TRANSLATION
// ==========================
const translationCache = {};

async function translatePage() {
    const langSelect = document.getElementById("languageSelect");
    if (!langSelect) return;
    const lang = langSelect.value;

    const elements = document.querySelectorAll("[data-translate], th");

    for (const el of elements) {
        if (!el.dataset.original) el.dataset.original = el.innerText.trim();
        const originalText = el.dataset.original;

        if (!originalText || !isNaN(originalText) || originalText.includes("@") || originalText.length < 2)
            continue;

        const cacheKey = `${originalText}||${lang}`;
        if (translationCache[cacheKey]) {
            el.innerText = translationCache[cacheKey];
            continue;
        }

        try {
            const res = await fetch("http://localhost:8080/api/translate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ text: originalText, targetLang: lang })
            });
            const data = await res.json();
            if (data.translated_text) {
                el.innerText = data.translated_text;
                translationCache[cacheKey] = data.translated_text;
            }
        } catch (err) {
            console.error("Translation failed:", err);
        }
    }

    document.documentElement.lang = lang;
}

// ==========================
// TRANSLATE BUTTON ONLY
// ==========================
document.querySelector(".translate-box button")?.addEventListener("click", () => {
    translatePage();
});

// ==========================
// MAP
// ==========================
let view;
require(["esri/Map", "esri/views/MapView", "esri/Graphic"], function(Map, MapView, Graphic) {
    view = new MapView({
        container: "viewDivAdmin",
        map: new Map({ basemap: "streets-navigation-vector" }),
        center: [78.9629, 20.5937],
        zoom: 5
    });

    window.addMarkers = function() {
        if (!view) return;
        view.graphics.removeAll();
        geoData.forEach(g => {
            const point = { type: "point", longitude: g.longitude, latitude: g.latitude };
            const graphic = new Graphic({ geometry: point, symbol: { type: "simple-marker", color: "red" } });
            view.graphics.add(graphic);
        });
    };
});

// ==========================
// INIT
// ==========================
loadUsers();
loadGeo();