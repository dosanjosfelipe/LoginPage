package me.LoginPage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import me.LoginPage.model.UserDB;

// Acha elementos no banco de dados
public interface UserRepository extends JpaRepository<UserDB, Long> {
    
    Optional<UserDB> findByEmail(String email);

    Optional<UserDB> findByEmailAndPassword(String email, String password);
}

