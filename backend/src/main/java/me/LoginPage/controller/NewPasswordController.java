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
import jakarta.servlet.http.HttpServletRequest;
import me.LoginPage.dto.NewPasswordDTO;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.PasswordResetTokenRepos;
import me.LoginPage.repository.UserRepository;
import me.LoginPage.service.UserService;
import me.LoginPage.service.CookieService;

@RestController
@RequestMapping("/newPassword")
public class NewPasswordController {
    
    final UserService userService;
    final UserRepository userRepository;
    final CookieService cookieService;
    final PasswordResetTokenRepos passwordResetTokenRepos;

    @Autowired
    public NewPasswordController(UserService userService, UserRepository userRepository, CookieService cookieService,
     PasswordResetTokenRepos passwordResetTokenRepos) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.cookieService = cookieService;
        this.passwordResetTokenRepos = passwordResetTokenRepos;
        
    }

    @PostMapping
    public ResponseEntity<String> setNewPassword(@RequestBody NewPasswordDTO dto, HttpServletRequest request) 
    throws UnsupportedEncodingException {

        String userIdString = CookieService.getCookie(request, "UserId");

        if (userIdString == null || userIdString.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cookie 'userId' está ausente ou vazio");
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Valor do cookie 'userId' inválido");
        }

        Optional<UserDB> userOPT = userRepository.findById(userId);
        UserDB user = userOPT.get();

        try {
            userRepository.updatePassword(user.getId(), dto.getNewPassword());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar a senha no banco de dados.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Location", "/frontend/templates/login.html")
                .body("Senha alterada.");
    }
}
