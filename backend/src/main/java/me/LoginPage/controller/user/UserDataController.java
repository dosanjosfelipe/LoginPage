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
import me.LoginPage.model.Users;
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
        String token = authorizationHeader.substring(7);

        //Pega a autentificação criada no JwtAuthentificatorFilter
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {

            String email = jwtToken.getEmailFromToken(token);

            Optional<Users> userOpt = userRepository.findByEmail(email);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado");
            }

            Users user = userOpt.get();
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "email", user.getEmail(),
                "date", user.getDate()));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
