package me.LoginPage.model;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Future;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class PasswordResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private Users user;

    @Future
    private LocalDateTime expirationDate;

    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}

    public Users getUser() {return user;}
    public void setUser(Users user) {this.user = user;}

    public LocalDateTime getExpirationDate() {return expirationDate;}
    public void setExpirationDate(LocalDateTime expirationDate) {this.expirationDate = expirationDate;}
}
