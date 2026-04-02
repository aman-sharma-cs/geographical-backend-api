console.log("LOGIN JS LOADED");

document.addEventListener("DOMContentLoaded", () => {

    const form = document.getElementById("loginForm");

    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const data = {
            userName: document.getElementById("userName").value,
            password: document.getElementById("password").value
        };

        fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })

        .then(res => res.json())

        .then(result => {

            console.log("Server Response:", result);

            alert(result.message);

            if (result.status == 200) {

                const user = result.data;

                // Save logged user
                localStorage.setItem("loggedInUser", JSON.stringify(user));

                console.log("Logged user:", user);
                console.log("User role:", user.role);

                // Normalize role
                const role = user.role?.toUpperCase();

                if (role === "ROLE_ADMIN" || role === "ADMIN") {

                    window.location.href = "admin-dashboard.html";

                } else {

                    window.location.href = "user-dashboard.html";

                }

            } else {

                alert("Invalid username or password");

            }

        })

        .catch(error => {

            console.error("Login Error:", error);
            alert("Server error. Please try again.");

        });

    });

});