package com.arthur.adaptivequizengine.security.auth.service;

import com.arthur.adaptivequizengine.security.auth.dto.AuthResponseDto;
import com.arthur.adaptivequizengine.security.auth.dto.LoginRequestDto;
import com.arthur.adaptivequizengine.security.auth.dto.RegisterRequestDto;
import com.arthur.adaptivequizengine.security.jwt.JwtService;
import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        var token = jwtService.generateToken(user);
        return new AuthResponseDto(token);
    }

    @Override
    public AuthResponseDto login(LoginRequestDto request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var token = jwtService.generateToken(user);
        return new AuthResponseDto(token);
    }
}
