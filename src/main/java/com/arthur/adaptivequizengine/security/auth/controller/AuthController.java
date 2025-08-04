package com.arthur.adaptivequizengine.security.auth.controller;

import com.arthur.adaptivequizengine.security.auth.dto.AuthResponseDto;
import com.arthur.adaptivequizengine.security.auth.dto.LoginRequestDto;
import com.arthur.adaptivequizengine.security.auth.dto.RegisterRequestDto;
import com.arthur.adaptivequizengine.security.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "User registration and login")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user and returns a JWT token upon success"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Parameter(description = "User registration data", required = true)
            @RequestBody @Valid RegisterRequestDto request) {

        var response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Login an existing user",
            description = "Authenticates user credentials and returns a JWT token"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Parameter(description = "User login credentials", required = true)
            @RequestBody @Valid LoginRequestDto request) {

        var response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}

