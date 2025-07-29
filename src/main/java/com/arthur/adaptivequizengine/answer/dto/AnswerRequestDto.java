package com.arthur.adaptivequizengine.answer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerRequestDto {
    @NotNull(message = "questionId must not be null")
    private Long questionId;

    @NotNull(message = "answerOptionId must not be null")
    private Long answerOptionId;

    @NotNull(message = "quizSessionId must not be null")
    private Long quizSessionId;

    public AnswerRequestDto(Long questionId, Long answerOptionId) {
        this.questionId = questionId;
        this.answerOptionId = answerOptionId;
    }
}
