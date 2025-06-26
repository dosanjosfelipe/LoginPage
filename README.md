# LoginPage

Sistema de autenticação de usuários com frontend em HTML/CSS/JavaScript e backend em Java utilizando Spring Boot.

## 📝 Descrição

Este projeto é um sistema simples de login, registro e gerenciamento de sessão de usuários. Ele foi desenvolvido com o objetivo de consolidar conhecimentos sobre autenticação, comunicação entre frontend e backend via API REST, segurança com JWT, e manipulação de cookies.  

Inclui funcionalidades como:
- Registro de novos usuários
- Login com autenticação via token JWT
- Armazenamento seguro de senhas com hashing
- Logout e visualização de dados.
- Implementação opcional de "Lembrar-me" com LocalStorage

## 💻 Tecnologias Utilizadas

### Backend
- **Java 21**
- **Spring Boot**
- **Spring Security**
- **JWT (JSON Web Token)**
- **PostgreSQL**
- **Maven**

### Frontend
- **HTML5**
- **CSS3**
- **JavaScript (Vanilla)**

## ⚙️ Funcionalidades

- [x] Cadastro de usuário com validação de campos
- [x] Autenticação de usuário e geração de JWT
- [x] Verificação automática de sessão com cookie
- [x] Logout e exclusão de cookie
- [x] Redirecionamento automático caso o usuário esteja logado
- [x] Recuperação de senha

## 🔐 Segurança

- As senhas são armazenadas no banco com criptografia (`BCrypt`)
- Sessões de usuário expiram ao fechar o navegador (ou após X tempo)
- O botão "Lembrar-me" cria LocalStorage persistente (opcional)

## 🚀 Como Executar

### Pré-requisitos
- Java 21
- Maven
- Git

### Passos

1. Clone o repositório
```bash
git clone https://github.com/dosanjosfelipe/LoginPage.git
cd LoginPage 
```

2. Configure o banco de dados, email e jwt no application.properties

3. Execute o backend:
```bash
./mvnw spring-boot:run
```
4. Abra frontend/templates/login.html no navegador
Para uma experiência ideal, é recomendável servir o frontend via um servidor estático ou integrá-lo ao Spring Boot.

📄 Licença
Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

👨‍💻 Autor:
Felipe dos Anjos
