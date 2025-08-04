package com.arthur.adaptivequizengine.answer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponseDto {
    private Long id;
    private Long questionId;
    private Long answerOptionId;
    private Long quizSessionId;
    private Boolean isCorrect;
    private LocalDateTime createdAt;
}
