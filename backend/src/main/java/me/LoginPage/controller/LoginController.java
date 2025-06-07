package me.LoginPage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.LoginPage.dto.LoginDTO;
import me.LoginPage.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    // Chama a função de verificar o usuário e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO dto) {
        Boolean result = userService.verifyLoginUser(dto);

        if (result) {
            return ResponseEntity
            .status(HttpStatus.OK)
            .header("Location", "/frontend/templates/index.html")
            .body("Usuário está no banco de dados!");
        } else {
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Usuário não está no banco de dados.");
        }
    }
}
