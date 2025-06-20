package me.LoginPage.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import me.LoginPage.model.Users;

// Acha elementos no banco de dados
public interface UserRepository extends JpaRepository<Users, Long> {
    
    Optional<Users> findByEmail(String email);

    Optional<Users> findByEmailAndPassword(String email, String password);

    @SuppressWarnings("null")
    Optional<Users> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.password = :password WHERE u.id = :id")
    void updatePassword(@Param("id") Long id, @Param("password") String senha);
}

