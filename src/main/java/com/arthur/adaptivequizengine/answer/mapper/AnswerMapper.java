package com.arthur.adaptivequizengine.answer.mapper;

import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(source = "question.id", target = "questionId")
    @Mapping(source = "answerOption.id", target = "answerOptionId")
    @Mapping(source = "quizSession.id", target = "quizSessionId")
    AnswerResponseDto toDto(Answer answer);

    List<AnswerResponseDto> toDtoList(List<Answer> answers);
}
