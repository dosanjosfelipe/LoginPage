package me.LoginPage.service.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import me.LoginPage.dto.auth.LoginDTO;
import me.LoginPage.dto.auth.RegisterUserDTO;
import me.LoginPage.dto.recoveryPassword.ResetPasswordDTO;
import me.LoginPage.model.Users;
import me.LoginPage.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Salvar usuário no banco de dados
    public void saveUser(RegisterUserDTO dto) {
        Users user = new Users();
        
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setDate(dto.getDate());
        
        userRepository.save(user);
    }

    // Verificar se o usuário está no banco de dados (Para não repetir email no registro)
    public Boolean verifyUser(String userEmail) {

        Optional<Users> hasEmail = userRepository.findByEmail(userEmail);
        
        if (hasEmail.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // Verificar se o email e a senha do usuario estão no banco de dados (Para entrar na conta)
    public Boolean verifyLoginUser(LoginDTO dto) {

        String userEmail = dto.getEmail();
        String userPassword = dto.getPassword(); 

        Optional<Users> hasUser = userRepository.findByEmailAndPassword(userEmail, userPassword);

        if (hasUser.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // Verificar se o email do usuario está no banco de dados (Para a recuperação de senha)
    public Boolean verifyEmail(ResetPasswordDTO dto) {

        Optional<Users> hasEmail = userRepository.findByEmail(dto.getEmail());
        
        if (hasEmail.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
