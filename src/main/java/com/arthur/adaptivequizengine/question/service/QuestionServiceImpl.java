package com.arthur.adaptivequizengine.question.service;

import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.InvalidQuestionException;
import com.arthur.adaptivequizengine.exception.handler.QuestionNotFoundException;
import com.arthur.adaptivequizengine.question.dto.AnswerOptionDto;
import com.arthur.adaptivequizengine.question.dto.QuestionFilterRequest;
import com.arthur.adaptivequizengine.question.dto.QuestionRequestDto;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.question.mapper.QuestionMapper;
import com.arthur.adaptivequizengine.question.repository.QuestionRepository;
import com.arthur.adaptivequizengine.question.specification.QuestionSpecification;
import com.arthur.adaptivequizengine.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements  QuestionService{

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final AccessValidator accessValidator;

    @Override
    public QuestionResponseDto createQuestion(QuestionRequestDto dto, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var question = questionMapper.toEntity(dto);

        boolean hasCorrect = dto.getOptions().stream().anyMatch(AnswerOptionDto::getIsCorrect);

        if(!hasCorrect) {
            throw new InvalidQuestionException("At least one option must be marked as correct");
        }

        var saved = questionRepository.save(question);

        log.info("Admin {} created question: \"{}\" [topic={}, difficulty={}]",
                currentUser.getId(),
                dto.getText(),
                dto.getTopic(),
                dto.getDifficulty());

        return questionMapper.toDto(saved);
    }

    @Override
    public List<QuestionResponseDto> getAllQuestions() {
        var questions = questionRepository.findAll();

        return questionMapper.toDtoList(questions);
    }

    @Override
    public QuestionResponseDto getQuestionById(Long id) {
        var question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        return questionMapper.toDto(question);
    }

    @Override
    public QuestionResponseDto updateQuestion(Long id, QuestionRequestDto dto, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        question.setDifficulty(dto.getDifficulty());
        question.setText(dto.getText());
        question.setTopic(dto.getTopic());

        List<AnswerOption> newOptions = dto.getOptions().stream()
                .map(optDto -> AnswerOption.builder()
                        .text(optDto.getText())
                        .isCorrect(optDto.getIsCorrect())
                        .question(question)
                        .build())
                .toList();

        question.setOptions(newOptions);

        var saved = questionRepository.save(question);

        log.info("Admin {} updated question {}: new text=\"{}\", topic={}, difficulty={}",
                currentUser.getId(), id, dto.getText(), dto.getTopic(), dto.getDifficulty());

        return questionMapper.toDto(saved);
    }

    @Override
    public void deleteQuestion(Long id, User currentUser) {
        accessValidator.validateIsAdmin(currentUser);

        var question = questionRepository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        questionRepository.delete(question);

        log.info("Admin {} deleted question {}", currentUser.getId(), id);
    }

    
    @Override
    public Page<QuestionResponseDto> getQuestionsWithFilter(QuestionFilterRequest filterRequest, Pageable pageable) {
        Specification<Question> spec = QuestionSpecification.withFilter(filterRequest);
        Page<Question> questionPage = questionRepository.findAll(spec, pageable);
        return questionPage.map(questionMapper::toDto);
    }
}
