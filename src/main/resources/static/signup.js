document.getElementById("signupForm")
.addEventListener("submit", function(event) {

    event.preventDefault();

    const data = {
        userName: document.getElementById("userName").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(result => {
        document.getElementById("message").innerText =
            result.message || "Signup Successful ";
    })
    .catch(error => {
        document.getElementById("message").innerText =
            "Signup Failed ";
        console.error(error);
    });

});
