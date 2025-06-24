package com.arthur.adaptivequizengine.security.auth.controller;

import com.arthur.adaptivequizengine.security.auth.dto.AuthResponseDto;
import com.arthur.adaptivequizengine.security.auth.dto.LoginRequestDto;
import com.arthur.adaptivequizengine.security.auth.dto.RegisterRequestDto;
import com.arthur.adaptivequizengine.security.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        var response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        var response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
