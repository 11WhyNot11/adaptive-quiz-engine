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
public class TestResultDto {

    private long correctCount;
    private int totalCount;
    private int score;
    private long durationSeconds;
    private QuizSessionStatus status;
}
