console.log("SHEM JavaScript loaded successfully");

function validateLogin() {
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;

    if (email === "" || password === "") {
        alert("Please fill all fields");
        return false;
    }
    return true;
}

function validateRegister() {
    let password = document.getElementById("password").value;
    let confirm = document.getElementById("confirmPassword").value;

    if (password !== confirm) {
        alert("Passwords do not match");
        return false;
    }
    return true;
}

function logoutAlert() {
    alert("You have logged out successfully");
}
