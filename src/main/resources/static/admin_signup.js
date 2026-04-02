console.log("ADMIN SIGNUP JS LOADED");

document.addEventListener("DOMContentLoaded", function () {

    const form = document.getElementById("adminSignupForm");

    form.addEventListener("submit", function (event) {

        event.preventDefault();

        const data = {
            userName: document.getElementById("userName").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        fetch("http://localhost:8080/api/auth/admin/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {

            alert(result.message);
            console.log(result);

            if (Number(result.status) === 200) {
                window.location.href = "login.html";
            }

            form.reset();
        })
        .catch(error => {
            console.error(error);
            alert("Admin signup failed");
        });

    });

});