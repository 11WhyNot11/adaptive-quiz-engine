package com.arthur.adaptivequizengine.user.repository;

import com.arthur.adaptivequizengine.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
