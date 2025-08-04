package com.arthur.adaptivequizengine.quizSession.dto;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
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
public class QuizSessionRequestDto {
    @NotNull
    private Integer durationMinutes;

    private Difficulty initialDifficulty;
}
