package com.arthur.adaptivequizengine.question.service;

import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;
import org.mapstruct.control.MappingControl;

import java.util.List;

public interface QuestionService {

    QuestionResponseDto createQuestion(QuestionRequestDto dto, User currentUser);
    List<QuestionResponseDto> getAllQuestions();
    QuestionResponseDto getQuestionById(Long id);
    QuestionResponseDto updateQuestion(Long id, QuestionRequestDto dto, User currentUser);
    void deleteQuestion(Long id, User currentUser);

}
