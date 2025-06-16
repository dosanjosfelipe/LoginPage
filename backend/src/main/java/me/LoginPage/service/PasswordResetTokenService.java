package me.LoginPage.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.LoginPage.model.UserDB;
import me.LoginPage.dto.ResetPasswordDTO;
import me.LoginPage.model.PasswordResetToken;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.repository.PasswordResetTokenRepos;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class PasswordResetTokenService {
    
    @Autowired
    private PasswordResetTokenRepos tokenRepository;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(ResetPasswordDTO dto) {
    
    tokenRepository.deleteAllExpiredTokens();

    UserDB user = userRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("Usuário com e-mail " + dto.getEmail() + " não encontrado."));

    SecureRandom random = new SecureRandom();
    StringBuilder token = new StringBuilder();

    for (int i = 0; i < 6; i++) {
        token.append(random.nextInt(10));
    }

    String finalToken = token.toString();

    PasswordResetToken resetToken = new PasswordResetToken();
    resetToken.setToken(finalToken);
    resetToken.setUser(user);
    resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(10));
    
    tokenRepository.save(resetToken);
    return finalToken;
    }
}
