package com.arthur.adaptivequizengine.question.mapper;

import com.arthur.adaptivequizengine.question.dto.AnswerOptionDto;
import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Question;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class QuestionMapper {


    @Mapping(source = "options", target = "options")
    public abstract QuestionResponseDto toDto(Question question);

    public abstract List<QuestionResponseDto> toDtoList(List<Question> questions);


    public Question toEntity(QuestionRequestDto dto) {
        if (dto == null) return null;

        Question question = Question.builder()
                .text(dto.getText())
                .difficulty(dto.getDifficulty())
                .topic(dto.getTopic())
                .build();

        List<AnswerOption> options = dto.getOptions().stream()
                .map(optDto -> AnswerOption.builder()
                        .text(optDto.getText())
                        .isCorrect(optDto.getIsCorrect())
                        .question(question) // важливо
                        .build())
                .collect(Collectors.toList());

        question.setOptions(options);
        return question;
    }

    protected abstract AnswerOptionDto toAnswerOptionDto(AnswerOption option);
}
