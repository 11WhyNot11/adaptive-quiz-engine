package com.arthur.adaptivequizengine.answer.controller;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.service.AnswerService;
import com.arthur.adaptivequizengine.user.entity.User;
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
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponseDto> submitAnswer(@RequestBody @Valid AnswerRequestDto dto,
                                       @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(answerService.submitAnswer(dto, currentUser));
    }
}
