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
            const data = await res.json(); 
            const rememberMeActive2 = data.rememberMeActive;
            const token = data.jwt_auth_token;

            if (!rememberMeActive2) {
                sessionStorage.setItem('token', token);
            } else {
                localStorage.setItem('token', token);
            }

            window.location.href = "/frontend/templates/dashboard.html";
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


async function sendJwtTokenData() {
    const URL = "http://localhost:8080/autoLogin";

    try {
        const res = await fetch(URL, {
            method: "GET",
            headers: { 
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json" 
            },
            credentials: "include"
        });

        if (res.status === 200) {
            window.location.href = "/frontend/templates/dashboard.html";
        } else if (res.status === 401) {
            console.log("ERRO 401")
        } else {
            console.log("ERRO:" + res.error)
        }
    } catch (error) {
        alert("ERRO: " + error.message);
    }
}

async function getUserData() {
    const URL = "http://localhost:8080/userData";

    try {
        const res = await fetch(URL, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem("token")}`,
                'Content-Type': 'application/json'
            }
        });

        if (res.status === 201) {
            const obj = await res.json();

            const userData1 = document.getElementById("userData1");
            const userData2 = document.getElementById("userData2");
            const userData3 = document.getElementById("userData3");

            userData1.textContent = "Nome: " + getCookie("user_name");
            userData2.textContent = "Email: " + obj.email;
            userData3.textContent = "Data de registro: " + obj.date;
        } else {
            const msg = await res.text();
            alert(`Erro ${res.status}: ${msg}`);
        }

    } catch (error) {
        alert("ERRO: Falha ao receber dados. " + error.message);
    }
}