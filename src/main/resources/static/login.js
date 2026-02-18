document.getElementById("loginForm")
.addEventListener("submit", function(event) {

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
    .then(response => response.json())
    .then(result => {
        document.getElementById("loginMessage").innerText =
            result.message || "Login Successful ";
    })
    .catch(error => {
        document.getElementById("loginMessage").innerText =
            "Login Failed ";
    });

});
