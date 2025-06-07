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
import me.LoginPage.service.EmailService;
import me.LoginPage.service.UserService;

@RestController
@RequestMapping("/register")
public class RegisterController {

    final UserService userService;
    final EmailService emailService;

    @Autowired
    public RegisterController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // Chama a função de salvar o usuário do DB, envia email e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<String> sendDatasToDB(@RequestBody RegisterUserDTO dto) throws MessagingException, IOException {
        Boolean result = userService.verifyUser(dto.getEmail());
        if (!result) {
            userService.saveUser(dto);
            emailService.sendEmail(dto.getEmail(), dto.getName(), "email/registerEmail.html", 
            "Boas vindas ao LoginPage", "register", "Null");

            return ResponseEntity 
            .status(HttpStatus.CREATED)
            .header("Location", "/frontend/templates/login.html")
            .body("Cadastrado com sucesso!");
        } else {
            return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body("Esse email já está cadastrado.");
        } 
    }
}
