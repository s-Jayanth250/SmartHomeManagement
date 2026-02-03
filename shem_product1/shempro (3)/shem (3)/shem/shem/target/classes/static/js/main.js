/**
 * Smart Home General Scripts
 */

// 1. Confirm Delete Action
function confirmDelete(deviceName) {
    return confirm(`Are you sure you want to remove '${deviceName}'? This cannot be undone.`);
}

// 2. Auto-hide Alerts after 5 seconds
document.addEventListener("DOMContentLoaded", function() {
    const alerts = document.querySelectorAll('.alert');
    if (alerts.length > 0) {
        setTimeout(() => {
            alerts.forEach(alert => {
                // Fade out effect
                alert.style.transition = "opacity 0.5s ease";
                alert.style.opacity = "0";
                setTimeout(() => alert.remove(), 500);
            });
        }, 5000); // 5 seconds
    }
});

// 3. Simple Client-Side Validation for Passwords
function validatePasswordMatch() {
    const pass = document.getElementById("password").value;
    const confirm = document.getElementById("confirmPassword").value;
    const errorDiv = document.getElementById("passError");

    if (pass !== confirm) {
        errorDiv.style.display = "block";
        errorDiv.innerText = "Passwords do not match!";
        return false; // Prevent form submission
    }
    errorDiv.style.display = "none";
    return true;
}