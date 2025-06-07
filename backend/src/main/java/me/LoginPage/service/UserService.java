package me.LoginPage.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.LoginPage.dto.LoginDTO;
import me.LoginPage.dto.RegisterUserDTO;
import me.LoginPage.dto.ResetPasswordDTO;
import me.LoginPage.mapper.RegisterUserMapper;
import me.LoginPage.model.UserDB;
import me.LoginPage.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private RegisterUserMapper registerUserMapper;

    @Autowired
    public UserService(UserRepository userRepository, RegisterUserMapper registerUserMapper) {
        this.registerUserMapper = registerUserMapper;
        this.userRepository = userRepository;
    }

    // Salvar usuário no banco de dados
    public void saveUser(RegisterUserDTO dto) {
        UserDB user = registerUserMapper.dtoToEntity(dto);
        userRepository.save(user);
    }

    // Verificar se o usuário está no banco de dados (Para não repetir email no registro)
    public Boolean verifyUser(String userEmail) {

        Optional<UserDB> hasEmail = userRepository.findByEmail(userEmail);
        
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

        Optional<UserDB> hasUser = userRepository.findByEmailAndPassword(userEmail, userPassword);

        if (hasUser.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    // Verificar se o email do usuario está no banco de dados (Para a recuperação de senha)
    public Boolean verifyEmail(ResetPasswordDTO dto) {

        Optional<UserDB> hasEmail = userRepository.findByEmail(dto.getEmail());
        
        if (hasEmail.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
