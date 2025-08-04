package com.arthur.adaptivequizengine.quizSession.dto;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerAnalyticsDto {

    String topic;
    Difficulty difficulty;
    long total;
    long correct;
    double accuracyPercent;
}
