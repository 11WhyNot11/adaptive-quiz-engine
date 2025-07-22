package com.arthur.adaptivequizengine.question.controller;

import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.service.QuestionService;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Tag(name = "Questions", description = "Manage quiz questions and their answer options")
public class QuestionController {

    private final QuestionService questionService;

    @Operation(
            summary = "Create a new question",
            description = "Create a quiz question with its answer options. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Question created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(
            @Parameter(description = "Question data to create", required = true)
            @RequestBody @Valid QuestionRequestDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.createQuestion(dto, currentUser));
    }

    @Operation(
            summary = "Get all questions",
            description = "Retrieve a list of all quiz questions"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of questions returned successfully")
    })
    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @Operation(
            summary = "Get question by ID",
            description = "Retrieve a single quiz question by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question returned successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(
            @Parameter(description = "ID of the question to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @Operation(
            summary = "Update an existing question",
            description = "Update a quiz question and its options. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Question updated successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(
            @Parameter(description = "ID of the question to update") @PathVariable Long id,
            @Parameter(description = "Updated question data") @RequestBody @Valid QuestionRequestDto dto,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        return ResponseEntity.ok(questionService.updateQuestion(id, dto, currentUser));
    }

    @Operation(
            summary = "Delete a question",
            description = "Delete a quiz question by its ID. Requires authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Question deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Question not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @Parameter(description = "ID of the question to delete") @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal User currentUser) {

        questionService.deleteQuestion(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}

