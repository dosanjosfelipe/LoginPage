package me.LoginPage.controller.auth;

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
import me.LoginPage.dto.auth.LoginDTO;
import me.LoginPage.model.Users;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.cookie.CookieService;
import me.LoginPage.service.user.UserService;
import me.LoginPage.util.jwt.JwtToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/login")
public class LoginController {
    
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final CookieService cookieService;
    final UserRepository userRepository;
    final JwtToken jwtToken;

    @Autowired
    public LoginController(UserService userService, CookieService cookieService, UserRepository userRepository, 
    JwtToken jwtToken, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.cookieService = cookieService;
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
        this.passwordEncoder = passwordEncoder;
    }

    // Chama a função de verificar o usuário e criar token e retorna uma resposta para o frontend
    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO dto, HttpServletResponse response) 
        throws UnsupportedEncodingException {

        Optional<Users> userOpt = userRepository.findByEmail(dto.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }

        Users user = userOpt.get();
        String hashedPassword = user.getPassword();

        if (!passwordEncoder.matches(dto.getPassword(), hashedPassword)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha incorretos.");
        }

        String token = jwtToken.generateToken(dto.getEmail());
        
        boolean rememberMe = dto.getRememberMe() ? true : false;
        int maxAge = dto.getRememberMe() ? 7889280 : -1;

        CookieService.setCookie(response, "user_name", user.getName(), maxAge);

        return ResponseEntity.ok(Map.of(
            "rememberMeActive", rememberMe,
            "jwt_auth_token", token
        ));
    }
}
