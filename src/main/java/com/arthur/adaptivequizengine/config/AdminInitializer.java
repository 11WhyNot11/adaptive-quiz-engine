package com.arthur.adaptivequizengine.config;

import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("docker") // Працює лише при активному профілі docker
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "admin@example.com";
        String password = "123456";

        // Перевіряємо, чи вже існує адмін
        if (userRepository.findByEmail(email).isEmpty()) {
            User admin = User.builder()
                    .email(email) // 🔧 ОБОВ’ЯЗКОВО
                    .password(passwordEncoder.encode(password))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println("Admin user created: " + email);
        } else {
            System.out.println("ℹAdmin already exists");
        }
    }
}
