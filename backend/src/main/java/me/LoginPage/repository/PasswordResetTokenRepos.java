package me.LoginPage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import me.LoginPage.model.PasswordResetToken;

public interface PasswordResetTokenRepos extends JpaRepository<PasswordResetToken, Long>{

    Optional<PasswordResetToken> findByToken(String token);
}
