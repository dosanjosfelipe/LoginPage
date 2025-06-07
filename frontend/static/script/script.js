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

    sendNewPasswordData(email);
    })
}

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
async function sendNewPasswordData(email) {
    const URL = "http://localhost:8080/ResetPassword";

    const data = {
        email: email
    };

    try {
        
        const statusResponse = await fetch(URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
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