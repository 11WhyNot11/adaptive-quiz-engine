package com.arthur.adaptivequizengine.answer.service;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.entity.Answer;
import com.arthur.adaptivequizengine.answer.mapper.AnswerMapper;
import com.arthur.adaptivequizengine.answer.repository.AnswerRepository;
import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.AnswerOptionNotFoundException;
import com.arthur.adaptivequizengine.exception.handler.QuestionNotFoundException;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.question.repository.AnswerOptionRepository;
import com.arthur.adaptivequizengine.question.repository.QuestionRepository;
import com.arthur.adaptivequizengine.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final AccessValidator accessValidator;
    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Override
    public AnswerResponseDto submitAnswer(AnswerRequestDto dto, User currentUser) {
        var question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(dto.getQuestionId()));

        var answerOption = answerOptionRepository.findById(dto.getAnswerOptionId())
                .orElseThrow(() -> new AnswerOptionNotFoundException(dto.getAnswerOptionId()));

        accessValidator.validateAnswerOptionBelongsToQuestion(answerOption, question);

        var isCorrect = answerOption.getIsCorrect();

        var answer = Answer.builder()
                .answerOption(answerOption)
                .question(question)
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .isCorrect(isCorrect)
                .build();

        var savedAnswer = answerRepository.save(answer);

        return answerMapper.toDto(savedAnswer);

    }
}
