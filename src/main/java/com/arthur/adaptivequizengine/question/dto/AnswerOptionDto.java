package com.arthur.adaptivequizengine.question.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnswerOptionDto {
    @NotBlank(message = "text must not be blank")
    private String text;

    @NotNull(message = "correctness must be mentioned")
    private Boolean isCorrect;
}
