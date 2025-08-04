package com.arthur.adaptivequizengine.question.dto;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionFilterRequest {
    private String topic;
    private Difficulty difficulty;
}
