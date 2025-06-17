package me.LoginPage.controller;

import java.io.UnsupportedEncodingException;
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
    public ResponseEntity<?> login(@RequestBody AutoLoginDTO dto, HttpServletResponse response) 
            throws UnsupportedEncodingException {

        String token = dto.getJwtToken();

        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Token JWT não fornecido."));
        }

        if (!jwtToken.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Token JWT inválido ou expirado."));
        }

        String userEmail = jwtToken.getEmailFromToken(token);
        Optional<UserDB> userOpt = userRepository.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Usuário do token não encontrado."));
        }
        UserDB user = userOpt.get();

        return ResponseEntity.ok(Map.of(
            "message", "Auto login realizado com sucesso.",
            "userName", user.getName()
        ));
    }
}