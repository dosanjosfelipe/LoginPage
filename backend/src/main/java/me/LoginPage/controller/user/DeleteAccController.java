package me.LoginPage.controller.user;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import me.LoginPage.model.Users;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.util.jwt.JwtToken;

@RestController
@RequestMapping("/deleteAcc")
public class DeleteAccController {
    final JwtToken jwtToken;
    final UserRepository userRepository;
    
    @Autowired
    public DeleteAccController(JwtToken jwtToken, UserRepository userRepository) {
        this.jwtToken = jwtToken;
        this.userRepository = userRepository;
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserAcc(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String email = jwtToken.getEmailFromToken(token);

            Optional<Users> OptUser = userRepository.findByEmail(email);
            Users user = OptUser.get();

            userRepository.deleteById(user.getId());

            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                "messege", "Usuario deletado."
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
            "messege", "Você não tem permição para deletar essa conta."
        ));
    }
}
