package com.arthur.adaptivequizengine.quizSession.controller;

import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.quizSession.dto.*;
import com.arthur.adaptivequizengine.quizSession.service.QuizSessionService;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-sessions")
@RequiredArgsConstructor
public class QuizSessionController {
    private final QuizSessionService quizSessionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<QuizSessionResponseDto> createSession(@Valid @RequestBody QuizSessionRequestDto dto,
                                                                @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(quizSessionService.createSession(dto, currentUser));
    }

    @PutMapping("/{sessionId}/start")
    public ResponseEntity<QuestionResponseDto> startSession(@PathVariable Long sessionId,
                                                            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(quizSessionService.startSession(sessionId, currentUser));
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<TestResultDto> finishSession(@PathVariable Long id,
                                                       @AuthenticationPrincipal User currentUser){
        return ResponseEntity.ok(quizSessionService.finishSession(id, currentUser));
    }

    @PutMapping("/{sessionId}/next-question")
    public ResponseEntity<QuestionResponseDto> getNextQuestion(@PathVariable Long sessionId,
                                                               @AuthenticationPrincipal User currentUser) {

        var nextQuestion = quizSessionService.getNextQuestion(sessionId, currentUser);

        if(nextQuestion == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(nextQuestion);
    }

    @GetMapping("/my")
    public ResponseEntity<List<QuizSessionSummaryDto>> getAllSessionsByUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(quizSessionService.getAllSessionsByUsers(currentUser));

    }

    @GetMapping("/{sessionId}/analytics")
    public ResponseEntity<List<AnswerAnalyticsDto>> getAnalyticsBySession(@PathVariable Long sessionId,
                                                                          @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(quizSessionService.getAnalyticsBySession(sessionId,currentUser));
    }
}
