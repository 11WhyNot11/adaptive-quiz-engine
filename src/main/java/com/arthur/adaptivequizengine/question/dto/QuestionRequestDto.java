package com.arthur.adaptivequizengine.question.dto;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class QuestionRequestDto {
    @NotBlank(message = "text must not be blank")
    private String text;

    @NotNull(message = "difficulty must not be null")
    private Difficulty difficulty;

    @NotBlank(message = "topic must not be blank")
    private String topic;

    private List<AnswerOptionDto> options = new ArrayList<>();
}
