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
import me.LoginPage.dto.RegisterUserDTO;
import me.LoginPage.service.EmailService;
import me.LoginPage.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/register")
public class RegisterController {

    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final EmailService emailService;

    @Autowired
    public RegisterController(UserService userService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> sendDatasToDB(@RequestBody RegisterUserDTO dto) 
            throws MessagingException, IOException {

        boolean emailExists = userService.verifyUser(dto.getEmail());

        if (emailExists) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Este e-mail já está cadastrado."));
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        userService.saveUser(dto);

        emailService.sendEmail(
            dto.getEmail(),                     
            dto.getName(),                      
            "email/registerEmail.html",         
            "Boas-vindas ao LoginPage",         
            "register",                         
            null                                
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                    "message", "Cadastro realizado com sucesso.",
                    "redirectTo", "/frontend/templates/login.html"
                ));
    }
}

