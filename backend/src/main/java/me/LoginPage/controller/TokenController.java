package me.LoginPage.controller;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import me.LoginPage.dto.ResetPasswordTokenDTO;
import me.LoginPage.model.PasswordResetToken;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.PasswordResetTokenRepos;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.CookieService;
import me.LoginPage.service.UserService;


@RestController
@RequestMapping("/token") 
public class TokenController {

    final CookieService cookieService;
    final UserService userService;
    final PasswordResetTokenRepos passwordResetTokenRepos;
    final UserRepository userRepository;

    @Autowired
    public TokenController(UserService userService, PasswordResetTokenRepos passwordResetTokenRepos, 
    UserRepository userRepository, CookieService cookieService) {
        this.userService = userService;
        this.passwordResetTokenRepos = passwordResetTokenRepos;
        this.userRepository = userRepository;
        this.cookieService = cookieService;
    }

    @PostMapping
    public ResponseEntity<?> verifyToken(@RequestBody ResetPasswordTokenDTO dto, HttpServletResponse response) 
    throws UnsupportedEncodingException {

        Optional<PasswordResetToken> tokenOpt = passwordResetTokenRepos.findByToken(dto.getToken());

        if (tokenOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token inválido ou não encontrado."));
        }

        PasswordResetToken token = tokenOpt.get();

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepos.delete(token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token expirado."));
        }

        Optional<UserDB> tokenUser = userRepository.findById(token.getUser().getId());
        if (tokenUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuário do token não encontrado."));
        }

        passwordResetTokenRepos.delete(token);

        Long userIdLong = token.getUser().getId();
        String userId = String.valueOf(userIdLong);
        CookieService.setCookie(response, "UserId", userId, 600);

        return ResponseEntity.ok(Map.of(
            "message", "Token validado com sucesso.",
            "redirectTo", "/frontend/templates/newPassword.html"
        ));
    }
}

