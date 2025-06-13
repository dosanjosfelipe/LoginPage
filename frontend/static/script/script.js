// Verificar campos do registro e mandar dados para o backend
const registerForm = document.getElementById("registerForm");

if (registerForm) {
    registerForm.addEventListener("submit", function(event) {
    event.preventDefault();

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim().toLowerCase().replace(/\s+/g, '');
    const password = document.getElementById("password").value.trim().replace(/\s+/g, '');
    const secondPassword = document.getElementById("secondPassword").value.trim().replace(/\s+/g, '');

    if (name.length == 0 || email.length == 0 || password.length == 0 || secondPassword.length == 0) {
        alert("Preencha todos os campos.");
    } else if (name.length > 16) {
        alert("O nome deve ter menos que 16 caracteres.")
    } else if (password.length < 8) {
        alert("A senha deve conter pelo menos 8 caracteres.");
    } else if (password != secondPassword){
        alert("As senhas não coincidem.")
    } else {
        sendRegisterData(name, email, password)
    }
})
}

// Verificar campos do login e mandar dados para o backend
const loginForm = document.getElementById("loginForm");

if (loginForm) {
    loginForm.addEventListener("submit", function(event){
    event.preventDefault();

    const email = document.getElementById("email").value.trim().toLowerCase().replace(/\s+/g, '');
    const password = document.getElementById("password").value.trim().replace(/\s+/g, '');

    sendLoginData(email, password)
})
}

// Mandar email da recuperação de senha para o backend
const passwordForm = document.getElementById("passwordForm");

if (passwordForm) {
    passwordForm.addEventListener("submit", function(event) {
    event.preventDefault();

    const email = document.getElementById("email").value.trim().toLowerCase().replace(/\s+/g, '');

    sendEmailData(email);
    })
}

// Mandar token para o backend verificar
const tokenForm = document.getElementById("tokenForm");

if (tokenForm) {
    tokenForm.addEventListener("submit", function(event){
        event.preventDefault();

        const token = document.getElementById("token").value.trim();

        sendTokenData(token);
    })
}

// Mandar nova senha para o Backend
const newPasswordForm = document.getElementById("newPasswordForm");

if (newPasswordForm) {
    newPasswordForm.addEventListener("submit", function(event){
        event.preventDefault();
        
        const newPassword = document.getElementById("newPassword").value.trim().replace(/\s+/g, '');
        const secondNewPassword = document.getElementById("secondNewPassword").value.trim().replace(/\s+/g, '');

        if (newPassword.length == 0 || secondNewPassword.length == 0) {
            alert("Preencha todos os campos.");
        } else if (newPassword.length < 8) {
            alert("A senha deve conter pelo menos 8 caracteres.");
        } else if (newPassword != secondNewPassword){
            alert("As senhas não coincidem.")
        } else {
            console.log("Senha que será enviada:", newPassword);
            sendNewPasswordData(newPassword)
        }
    })
}

//* ======================================================================================

// Função para mandar dados do registro para o backend
async function sendRegisterData(name, email, password) {
    const URL = "http://localhost:8080/register";

    const data = {
        name: name,
        email: email,
        password: password
    };
    
    try {
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (statusResponse.status === 201) {
            alert("Cadastrado com sucesso!");
            window.location.href = "/frontend/templates/login.html";
        } else if (statusResponse.status === 409) {
            alert("Esse email já está cadastrado.");
        } else {
            throw new Error("Erro ao enviar dados: " + statusResponse.status);
        }

    } catch (error) {
        alert("ERRO: " + error.message);
    } 
}

// Função para mandar dados do login para o backend
async function sendLoginData(email, password) {
    const URL = "http://localhost:8080/login";

    const data = {
        email: email,
        password: password
    }

    try {
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

        if (statusResponse.status === 200) {
            window.location.href = "/frontend/templates/index.html"
        } else if (statusResponse.status === 401) {
            alert("Email ou Senha errados.")
        } else {
            throw new Error("Erro ao enviar dados: " + statusResponse.status);
        }

    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

// Função para mandar dados do recuperar senha para o backend
async function sendEmailData(email) {
    const URL = "http://localhost:8080/resetPassword";

    const data = {
        email: email
    };

    try {
        
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
            credentials: "include"
        });

        if (statusResponse.status === 200) {
                window.location.href = "/frontend/templates/token.html"
            } else if (statusResponse.status === 401) {
                alert("Esse email não está no nosso banco de dados.")
            } else {
                throw new Error("Erro ao enviar dados: " + statusResponse.status);
            }

    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

// Função para enviar Token para o backend

async function sendTokenData(token) {
    const URL = "http://localhost:8080/token";

    const data = {
        token: token
    }

    try {
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
            credentials: "include"
        })
        
        if (statusResponse.status === 200) {
            window.location.href = "/frontend/templates/newPassword.html"
        } else if (statusResponse.status === 401) {
            const message = await statusResponse.text();
            alert(message)
        } else {
            throw new Error("Erro ao enviar dados: " + statusResponse.status);
        }

    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function sendNewPasswordData(newPassword) {
    const URL = "http://localhost:8080/newPassword";

    const data = {
        newPassword: newPassword
    }
    console.log(newPassword)

    try {
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data),
            credentials: "include"
        })
        
        if (statusResponse.status === 200) {
            window.location.href = "/frontend/templates/login.html"
            document.cookie = "userId=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        } else if (statusResponse.status === 500) {
            const message = await statusResponse.text();
            alert(message)
        } else {
            throw new Error("Erro ao enviar dados: " + statusResponse.status);
        }

    } catch (error) {
        alert("ERRO: " + error.message);
    }
}