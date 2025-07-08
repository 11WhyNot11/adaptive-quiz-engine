package com.arthur.adaptivequizengine.quizSession.dto;

import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizSessionResponseDto {

    private Long id;
    private Long userId;
    private QuizSessionStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer durationMinutes;
    private Integer score;
}
