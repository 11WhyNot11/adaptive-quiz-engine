package com.arthur.adaptivequizengine.quizSession.controller;

import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionRequestDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionResponseDto;
import com.arthur.adaptivequizengine.quizSession.service.QuizSessionService;
import com.arthur.adaptivequizengine.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class QuizSessionController {
    private final QuizSessionService quizSessionService;

    @PostMapping
    public ResponseEntity<QuizSessionResponseDto> createSession(@Valid @RequestBody QuizSessionRequestDto dto,
                                                                @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizSessionService.createSession(dto, currentUser));

    }
}
