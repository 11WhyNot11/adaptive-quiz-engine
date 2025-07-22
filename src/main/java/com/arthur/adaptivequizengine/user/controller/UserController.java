package com.arthur.adaptivequizengine.user.controller;

import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.dto.UserResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.mapper.UserMapper;
import com.arthur.adaptivequizengine.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create regular user", description = "Creates a new user. Only accessible by admins.")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping
    public ResponseEntity<UserResponseDto> save(
            @Parameter(description = "User creation DTO", required = true)
            @RequestBody @Valid UserRequestDto dto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(dto, currentUser));
    }

    @Operation(summary = "Create admin user", description = "Creates a new admin user. Only accessible by admins.")
    @ApiResponse(responseCode = "201", description = "Admin user created successfully")
    @PostMapping("/admins")
    public ResponseEntity<UserResponseDto> createAdmin(
            @Parameter(description = "Admin user creation DTO", required = true)
            @RequestBody @Valid UserRequestDto dto,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createAdmin(dto, currentUser));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID. Only accessible by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(userService.findById(id, currentUser));
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users. Only accessible by admins.")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(userService.findAll(currentUser));
    }

    @Operation(summary = "Get current user", description = "Returns information about the currently authenticated user.")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID. Only accessible by admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        userService.deleteById(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

