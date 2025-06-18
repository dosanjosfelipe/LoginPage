package me.LoginPage.dto.auth;

// Transforma o Json com os dados do usuario em variaveis para serem convertidas em dados do banco de dados
public class RegisterUserDTO {
    private String name;
    private String email;
    private String password;

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

}
