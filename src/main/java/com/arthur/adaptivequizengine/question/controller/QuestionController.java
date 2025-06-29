package com.arthur.adaptivequizengine.question.controller;

import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.service.QuestionService;
import com.arthur.adaptivequizengine.user.entity.User;
import com.arthur.adaptivequizengine.user.service.UserService;
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
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(@RequestBody @Valid QuestionRequestDto dto,
                                                              @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionService.createQuestion(dto, currentUser));
    }

    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestions() {
        return ResponseEntity.ok(questionService.getAllQuestions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> updateQuestion(@PathVariable Long id,
                                                              @RequestBody @Valid QuestionRequestDto dto,
                                                              @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(questionService.updateQuestion(id, dto, currentUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id,
                                               @AuthenticationPrincipal User currentUser) {
        questionService.deleteQuestion(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}
