package me.LoginPage.mapper;

import org.springframework.stereotype.Component;
import me.LoginPage.dto.RegisterUserDTO;
import me.LoginPage.model.UserDB;

// Transforma o DTO em um usuário do banco de dados
@Component
public class RegisterUserMapper {
    
    public UserDB dtoToEntity(RegisterUserDTO dto) {
        UserDB user = new UserDB();
        
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
}
