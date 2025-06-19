window.addEventListener("DOMContentLoaded", async () => {
    const currentPath = window.location.pathname;
    let jwtToken = null;

    if (localStorage.getItem("token")) {
        jwtToken = localStorage.getItem("token");
    }

    if (sessionStorage.getItem("token")) {
        jwtToken = sessionStorage.getItem("token");
    }

    if (currentPath === "/frontend/templates/dashboard.html") {
        if (!jwtToken || !getCookie("user_name")) {
            window.location.href = "/frontend/templates/login.html";
            return;
        }

        const userName = document.getElementById("userName");
        if (userName) {
            userName.textContent = getCookie("user_name");
        }

    } else if (currentPath === "/frontend/templates/newPassword.html" ||
               currentPath === "/frontend/templates/token.html") {

        if (!getCookie("changing_password")) {
            window.location.href = "/frontend/templates/login.html";
            return;
        }

        const userEmail = document.getElementById("userEmail");
        if (userEmail) {
            userEmail.textContent = decodeURIComponent(getCookie("user_email"));
        }
    } else {
        if (jwtToken) {
            await sendJwtTokenData(jwtToken.trim());
        }
    }
});

function exitAccount() {
    localStorage.removeItem('token');
    document.cookie = "user_name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "changing_password=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = "/frontend/templates/login.html";
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}