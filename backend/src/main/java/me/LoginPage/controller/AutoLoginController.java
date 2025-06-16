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
import me.LoginPage.dto.AutoLoginDTO;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.CookieService;
import me.LoginPage.util.JwtToken;

@RestController
@RequestMapping("/autoLogin")
public class AutoLoginController {

    final UserRepository userRepository;
    final JwtToken jwtToken;
    final CookieService cookieService;

    @Autowired
    public AutoLoginController(JwtToken jwtToken, UserRepository userRepository, CookieService cookieService) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
        this.cookieService = cookieService;
    }
    
    @PostMapping
    public ResponseEntity<String> login(@RequestBody AutoLoginDTO dto, HttpServletResponse response) throws UnsupportedEncodingException {

        System.out.println(dto.getJwtToken()); 
        if (jwtToken.isTokenValid(dto.getJwtToken())) {
            String userEmail = jwtToken.getEmailFromToken(dto.getJwtToken());

            Optional<UserDB> user = userRepository.findByEmail(userEmail);

            if (user.isPresent()) {
                CookieService.setCookie(response, "user_name", user.get().getName(), 7889280);

                return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", "/frontend/templates/index.html")
                .body("Token validado.");
            } else {
                return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Usuario do token não encontrado.");
            }

        } else {
            return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body("Token não validado.");
        }
    }
}