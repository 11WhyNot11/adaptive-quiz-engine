package com.arthur.adaptivequizengine.question.dto;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponseDto {
    private Long id;
    private String text;
    private Difficulty difficulty;
    private String topic;
    private List<AnswerOptionDto> options = new ArrayList<>();
}
