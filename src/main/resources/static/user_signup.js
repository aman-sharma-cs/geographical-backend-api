console.log("USER SIGNUP JS LOADED");

document.addEventListener("DOMContentLoaded", function () {

    const signupForm = document.getElementById("signupForm");

    if (!signupForm) {
        console.error("Signup form not found!");
        return;
    }

    signupForm.addEventListener("submit", function (event) {
        event.preventDefault(); // prevent page reload

        // collect form data
        const data = {
            userName: document.getElementById("userName").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        console.log("Signup data:", data);

        // call backend API
        fetch("http://localhost:8080/api/auth/user/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(result => {
            console.log("Server response:", result);

            if (result.status === 200) {
                alert(result.message || "Signup successful!");
                // redirect to login page
                window.location.href = "login.html";
            } else {
                alert(result.message || "Signup failed. Please try again.");
            }

            signupForm.reset();
        })
        .catch(error => {
            console.error("Signup error:", error);
            alert("Something went wrong! Please try again.");
        });
    });
});