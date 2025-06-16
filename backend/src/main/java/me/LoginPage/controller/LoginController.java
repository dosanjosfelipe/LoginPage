package me.LoginPage.controller;

import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import me.LoginPage.dto.LoginDTO;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.CookieService;
import me.LoginPage.service.UserService;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    final UserService userService;
    final CookieService cookieService;
    final UserRepository userRepository;

    @Autowired
    public LoginController(UserService userService, CookieService cookieService, UserRepository userRepository) {
        this.userService = userService;
        this.cookieService = cookieService;
        this.userRepository = userRepository;
    }

    // Chama a função de verificar o usuário e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO dto, HttpServletResponse response) 
        throws UnsupportedEncodingException {
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
