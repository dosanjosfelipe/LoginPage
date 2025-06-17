// ===============================
//  Script Principal do LoginPage
// ===============================

window.addEventListener("DOMContentLoaded", async () => {
    const currentPath = window.location.pathname;
    const jwtToken = getCookie("jwt_auth_token");

    // Protege as rotas sensíveis
    if (currentPath === "/frontend/templates/index.html") {
        // Impede acesso sem estar logado
        if (!jwtToken || !getCookie("user_name")) {
            window.location.href = "/frontend/templates/login.html";
            return;
        }

        // Exibe o nome do usuário na tela
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

        const userEmail = document.getElementById("userEmail")
        if (userEmail) {
            userEmail.textContent =  decodeURIComponent(getCookie("user_email"));
        }
    } else {
        // Tenta auto-login para outras rotas, se token estiver presente
        if (jwtToken) {
            await sendJwtTokenData(jwtToken.trim());
        }
    }
});

// ===============================
//    Lógica do botão de sair
// ===============================
function exitAccount() {
    document.cookie = "jwt_auth_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "user_name=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    window.location.href = "/frontend/templates/login.html";
}

// ===============================
//    Submissão de formulários
// ===============================

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

// ===============================
//     Funções de requisição
// ===============================

async function sendRegisterData(name, email, password) {
    const URL = "http://localhost:8080/register";
    const data = { name, email, password };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 201) {
            alert("Cadastrado com sucesso!");
            window.location.href = "/frontend/templates/login.html";
        } else if (res.status === 409) {
            alert("Esse email já está cadastrado.");
        } else {
            throw new Error("Erro: " + res.status);
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendLoginData(email, password, rememberMe) {
    const URL = "http://localhost:8080/login";
    const data = { email, password, rememberMe };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 200) {
            window.location.href = "/frontend/templates/index.html";
        } else if (res.status === 401) {
            alert("Email ou senha incorretos.");
        } else {
            throw new Error("Erro: " + res.status);
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendEmailData(email) {
    const URL = "http://localhost:8080/resetPassword";
    const data = { email };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 200) {
            window.location.href = "/frontend/templates/token.html";
        } else if (res.status === 401) {
            alert("Esse email não está cadastrado.");
        } else {
            throw new Error("Erro: " + res.status);
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendTokenData(token) {
    const URL = "http://localhost:8080/token";
    const data = { token };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 200) {
            window.location.href = "/frontend/templates/newPassword.html";
            document.cookie = "user_email=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        } else if (res.status === 401) {
            const msg = await res.text();
            alert(msg);
        } else {
            throw new Error("Erro: " + res.status);
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendNewPasswordData(newPassword) {
    const URL = "http://localhost:8080/newPassword";
    const data = { newPassword };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 200) {
            document.cookie = "changing_password=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            document.cookie = "UserId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            window.location.href = "/frontend/templates/login.html";
        } else {
            const msg = await res.text();
            alert(msg);
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendJwtTokenData(jwtToken) {
    const URL = "http://localhost:8080/autoLogin";
    const data = { jwtToken };

    try {
        const res = await fetch(URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (res.status === 200) {
            window.location.href = "/frontend/templates/index.html";
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

// ===============================
//          ler cookie
// ===============================
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}
