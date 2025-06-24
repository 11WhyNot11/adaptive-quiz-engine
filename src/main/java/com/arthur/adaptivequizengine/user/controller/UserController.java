package com.arthur.adaptivequizengine.user.controller;

import com.arthur.adaptivequizengine.user.dto.UserRequestDto;
import com.arthur.adaptivequizengine.user.dto.UserResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.mapper.UserMapper;
import com.arthur.adaptivequizengine.user.service.UserService;
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
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserResponseDto> save(@RequestBody @Valid UserRequestDto dto,
                                                @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(dto, currentUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id,
                                                    @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.findById(id, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userService.findAll(currentUser));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me (@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(userMapper.toDto(currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal User currentUser) {
        userService.deleteById(id, currentUser);
        return ResponseEntity.noContent().build();
    }




}
