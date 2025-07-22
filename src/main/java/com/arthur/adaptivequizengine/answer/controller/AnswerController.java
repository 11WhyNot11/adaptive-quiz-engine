package com.arthur.adaptivequizengine.answer.controller;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.service.AnswerService;
import com.arthur.adaptivequizengine.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answers")
@Tag(name = "Answers", description = "Submit answers to quiz questions")
public class AnswerController {

    private final AnswerService answerService;

    @Operation(
            summary = "Submit an answer to a quiz question",
            description = "Submit an answer for a specific question within an active quiz session."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Answer submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or bad request"),
            @ApiResponse(responseCode = "409", description = "Session already finished or expired"),
            @ApiResponse(responseCode = "404", description = "Question, answer option or session not found")
    })
    @PostMapping
    public ResponseEntity<AnswerResponseDto> submitAnswer(
            @Parameter(description = "Answer request with questionId, answerOptionId, quizSessionId", required = true)
            @RequestBody @Valid AnswerRequestDto dto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(answerService.submitAnswer(dto, currentUser));
    }
}
