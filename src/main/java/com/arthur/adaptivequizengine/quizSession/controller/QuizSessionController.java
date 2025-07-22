package com.arthur.adaptivequizengine.quizSession.controller;

import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.quizSession.dto.*;
import com.arthur.adaptivequizengine.quizSession.service.QuizSessionService;
import com.arthur.adaptivequizengine.user.entity.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-sessions")
@RequiredArgsConstructor
@Tag(name = "Quiz Sessions", description = "Manage quiz sessions, progress, and analytics")
public class QuizSessionController {

    private final QuizSessionService quizSessionService;
    private final UserService userService;

    @Operation(
            summary = "Create a new quiz session",
            description = "Start a new quiz session with selected duration and initial difficulty"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid session data")
    })
    @PostMapping
    public ResponseEntity<QuizSessionResponseDto> createSession(
            @Parameter(description = "Quiz session configuration", required = true)
            @Valid @RequestBody QuizSessionRequestDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED).body(quizSessionService.createSession(dto, currentUser));
    }

    @Operation(
            summary = "Start a quiz session",
            description = "Marks the session as started and returns the first question"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session started, question returned"),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "409", description = "Session already started or finished")
    })
    @PutMapping("/{sessionId}/start")
    public ResponseEntity<QuestionResponseDto> startSession(
            @Parameter(description = "ID of the session to start") @PathVariable Long sessionId,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(quizSessionService.startSession(sessionId, currentUser));
    }

    @Operation(
            summary = "Finish a quiz session",
            description = "Manually finishes the session and calculates the result"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session finished and result returned"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PutMapping("/{id}/finish")
    public ResponseEntity<TestResultDto> finishSession(
            @Parameter(description = "ID of the session to finish") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(quizSessionService.finishSession(id, currentUser));
    }

    @Operation(
            summary = "Get the next question in the session",
            description = "Returns the next question based on adaptive logic, or finishes the session if none left"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Next question returned"),
            @ApiResponse(responseCode = "204", description = "No more questions available"),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "409", description = "Session is not in progress or expired")
    })
    @PutMapping("/{sessionId}/next-question")
    public ResponseEntity<QuestionResponseDto> getNextQuestion(
            @Parameter(description = "ID of the session") @PathVariable Long sessionId,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        var nextQuestion = quizSessionService.getNextQuestion(sessionId, currentUser);
        if (nextQuestion == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(nextQuestion);
    }

    @Operation(
            summary = "Get all your quiz sessions",
            description = "Returns a list of quiz sessions created by the authenticated user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of quiz sessions returned")
    })
    @GetMapping("/my")
    public ResponseEntity<List<QuizSessionSummaryDto>> getAllSessionsByUser(
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(quizSessionService.getAllSessionsByUsers(currentUser));
    }

    @Operation(
            summary = "Get session analytics",
            description = "Returns accuracy breakdown per topic and difficulty for a finished session"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Analytics returned"),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "409", description = "Session not yet finished")
    })
    @GetMapping("/{sessionId}/analytics")
    public ResponseEntity<List<AnswerAnalyticsDto>> getAnalyticsBySession(
            @Parameter(description = "ID of the session to analyze") @PathVariable Long sessionId,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(quizSessionService.getAnalyticsBySession(sessionId, currentUser));
    }
}

