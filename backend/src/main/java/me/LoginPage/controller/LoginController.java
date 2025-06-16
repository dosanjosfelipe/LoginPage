package me.LoginPage.controller;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import me.LoginPage.dto.LoginDTO;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.CookieService;
import me.LoginPage.service.UserService;
import me.LoginPage.util.JwtToken;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    final UserService userService;
    final CookieService cookieService;
    final UserRepository userRepository;
    final JwtToken jwtToken;

    @Autowired
    public LoginController(UserService userService, CookieService cookieService, UserRepository userRepository, JwtToken jwtToken) {
        this.userService = userService;
        this.cookieService = cookieService;
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
    }

    // Chama a função de verificar o usuário e criar token e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDTO dto, HttpServletResponse response) 
        throws UnsupportedEncodingException {
        Boolean result = userService.verifyLoginUser(dto);
        Boolean rememberMe = dto.getRememberMe();

        Optional<UserDB> user = userRepository.findByEmail(dto.getEmail());

        if (rememberMe) {
            String token = jwtToken.generateToken(dto.getEmail());
            CookieService.setCookie(response, "jwt_token", token, 7889280);
        }
        
        if (result) {
            CookieService.setCookie(response, "user_name", user.get().getName(), 7889280);
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
