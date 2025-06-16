package me.LoginPage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import jakarta.transaction.Transactional;
import me.LoginPage.model.PasswordResetToken;

public interface PasswordResetTokenRepos extends JpaRepository<PasswordResetToken, Long>{

    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken t WHERE t.expirationDate < CURRENT_TIMESTAMP")
    void deleteAllExpiredTokens();
}
