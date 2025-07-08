package com.arthur.adaptivequizengine;

import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.mapper.UserMapper;
import com.arthur.adaptivequizengine.user.repository.UserRepository;
import com.arthur.adaptivequizengine.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AdaptiveQuizEngineApplication {

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));

        SpringApplication.run(AdaptiveQuizEngineApplication.class, args);

    }

}
