package me.LoginPage.controller;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.mail.MessagingException;
import me.LoginPage.dto.RegisterUserDTO;
import me.LoginPage.dto.ResetPasswordDTO;
import me.LoginPage.service.EmailService;
import me.LoginPage.service.PasswordResetTokenService;
import me.LoginPage.service.UserService;

@RestController
@RequestMapping("/resetPassword")
public class PasswordResetController {
    
    final UserService userService;
    final PasswordResetTokenService passwordResetTokenService;
    final EmailService emailService;

    @Autowired
    public PasswordResetController(UserService userService, PasswordResetTokenService passwordResetTokenService,
        EmailService emailService) {
        this.userService = userService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.emailService = emailService;
    }

    // Chama a função de verificar o email, gera um token, manda via email e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<String> newPassword(@RequestBody ResetPasswordDTO passwordDto, RegisterUserDTO RegisterDto) 
        throws MessagingException, IOException {

        Boolean result = userService.verifyEmail(passwordDto);

        if (result) {
            String token = passwordResetTokenService.generateToken(passwordDto);
            emailService.sendEmail(passwordDto.getEmail(), "null", "email/resetPasswordEmail.html",
                "Redefinir Senha", "reset", token);

            return ResponseEntity
            .status(HttpStatus.OK)
            .header("Location", "/frontend/templates/token.html")
            .body("Email enviado com sucesso.");
        } else {
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Usuário não está no banco de dados.");
        }
    }
}
