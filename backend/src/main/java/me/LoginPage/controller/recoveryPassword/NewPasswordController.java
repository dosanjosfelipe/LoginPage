package me.LoginPage.controller.recoveryPassword;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import me.LoginPage.dto.recoveryPassword.NewPasswordDTO;
import me.LoginPage.model.Users;
import me.LoginPage.repository.PasswordResetTokenRepos;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.cookie.CookieService;
import me.LoginPage.service.user.UserService;

@RestController
@RequestMapping("/newPassword")
public class NewPasswordController {
    
    final UserService userService;
    final UserRepository userRepository;
    final CookieService cookieService;
    final PasswordResetTokenRepos passwordResetTokenRepos;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public NewPasswordController(UserService userService,UserRepository userRepository,CookieService cookieService,
    PasswordResetTokenRepos passwordResetTokenRepos, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.cookieService = cookieService;
        this.passwordResetTokenRepos = passwordResetTokenRepos;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> setNewPassword(@RequestBody NewPasswordDTO dto, HttpServletRequest request) 
            throws UnsupportedEncodingException {

        String userIdString = CookieService.getCookie(request, "UserId");

        if (userIdString == null || userIdString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Cookie 'UserId' está ausente ou vazio"));
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Valor do cookie 'UserId' inválido"));
        }

        Optional<Users> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Usuário não encontrado."));
        }

        try {
            String hashedPassword = passwordEncoder.encode(dto.getNewPassword());

            userRepository.updatePassword(userId, hashedPassword);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar a senha no banco de dados."));
        }

        return ResponseEntity.ok(Map.of(
            "message", "Senha atualizada com sucesso."
        ));
    }
}

