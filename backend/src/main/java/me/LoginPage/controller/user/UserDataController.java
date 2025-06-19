package me.LoginPage.controller.user;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.util.jwt.JwtToken;

@RestController
@RequestMapping("/userData")
public class UserDataController {
    
    final JwtToken jwtToken;
    final UserRepository userRepository;
    
    @Autowired
    public UserDataController(JwtToken jwtToken, UserRepository userRepository) {
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> sendUserData(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente ou mal formatado");
        }

        String token = authorizationHeader.substring(7);

        if (!jwtToken.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou expirado");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            String email = jwtToken.getEmailFromToken(token);

            Optional<UserDB> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
            }

            UserDB user = userOpt.get();
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                        "email", user.getEmail(),
                        "date", user.getDate()));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
