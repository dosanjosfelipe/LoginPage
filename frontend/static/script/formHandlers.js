// Registro
const registerForm = document.getElementById("registerForm");
if (registerForm) {
    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const name = document.getElementById("name").value.trim();
        const email = document.getElementById("email").value.trim().toLowerCase().replace(/\s+/g, '');
        const password = document.getElementById("password").value.trim();
        const secondPassword = document.getElementById("secondPassword").value.trim();

        if (!name || !email || !password || !secondPassword) {
            alert("Preencha todos os campos.");
        } else if (name.length > 16) {
            alert("O nome deve ter menos que 16 caracteres.");
        } else if (password.length < 8) {
            alert("A senha deve conter pelo menos 8 caracteres.");
        } else if (password !== secondPassword) {
            alert("As senhas não coincidem.");
        } else {
            await sendRegisterData(name, email, password);
        }
    });
}

// Login
const loginForm = document.getElementById("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const email = document.getElementById("email").value.trim().toLowerCase();
        const password = document.getElementById("password").value.trim();
        const rememberMe = document.getElementById("rememberMe").checked;

        await sendLoginData(email, password, rememberMe);
    });
}

// Recuperar senha
const passwordForm = document.getElementById("passwordForm");
if (passwordForm) {
    passwordForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const email = document.getElementById("email").value.trim().toLowerCase();
        await sendEmailData(email);
    });
}

// Verificação de token
const tokenForm = document.getElementById("tokenForm");
if (tokenForm) {
    tokenForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        const token = document.getElementById("token").value.trim();
        await sendTokenData(token);
    });
}

// Nova senha
const newPasswordForm = document.getElementById("newPasswordForm");
if (newPasswordForm) {
    newPasswordForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const newPassword = document.getElementById("newPassword").value.trim();
        const secondNewPassword = document.getElementById("secondNewPassword").value.trim();

        if (!newPassword || !secondNewPassword) {
            alert("Preencha todos os campos.");
        } else if (newPassword.length < 8) {
            alert("A senha deve conter pelo menos 8 caracteres.");
        } else if (newPassword !== secondNewPassword) {
            alert("As senhas não coincidem.");
        } else {
            await sendNewPasswordData(newPassword);
        }
    });
}