package me.LoginPage.controller;

import java.io.IOException;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import me.LoginPage.dto.ResetPasswordDTO;
import me.LoginPage.service.CookieService;
import me.LoginPage.service.EmailService;
import me.LoginPage.service.PasswordResetTokenService;
import me.LoginPage.service.UserService;

@RestController
@RequestMapping("/resetPassword")
public class PasswordResetController {

    final UserService userService;
    final PasswordResetTokenService passwordResetTokenService;
    final EmailService emailService;
    final CookieService cookieService;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService, 
    EmailService emailService, CookieService cookieService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
        this.cookieService = cookieService;
    }

    @PostMapping
    public ResponseEntity<?> newPassword(@RequestBody ResetPasswordDTO dto, HttpServletResponse response) 
    throws MessagingException, IOException {

        boolean result = userService.verifyEmail(dto);

        if (!result) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuário não está no banco de dados."));
        }

        String token = passwordResetTokenService.generateToken(dto);

        emailService.sendEmail(
            dto.getEmail(),
            null,
            "email/resetPasswordEmail.html",
            "Redefinir Senha",
            "reset",
            token
        );

        CookieService.setCookie(response, "changing_password", "true", 900);
        CookieService.setCookie(response, "user_email", dto.getEmail(), 600);

        return ResponseEntity.ok(Map.of(
            "message", "E-mail de redefinição enviado com sucesso.",
            "redirectTo", "/frontend/templates/token.html"
        ));
    }
}
