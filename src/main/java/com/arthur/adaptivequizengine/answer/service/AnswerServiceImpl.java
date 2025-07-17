package com.arthur.adaptivequizengine.answer.service;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.answer.entity.Answer;
import com.arthur.adaptivequizengine.answer.mapper.AnswerMapper;
import com.arthur.adaptivequizengine.answer.repository.AnswerRepository;
import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.AnswerOptionNotFoundException;
import com.arthur.adaptivequizengine.exception.handler.ConflictException;
import com.arthur.adaptivequizengine.exception.handler.QuestionNotFoundException;
import com.arthur.adaptivequizengine.exception.handler.SessionNotFoundException;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.question.repository.AnswerOptionRepository;
import com.arthur.adaptivequizengine.question.repository.QuestionRepository;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.quizSession.repository.QuizSessionRepository;
import com.arthur.adaptivequizengine.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService{

    private final AccessValidator accessValidator;
    private final AnswerMapper answerMapper;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    @Override
    public AnswerResponseDto submitAnswer(AnswerRequestDto dto, User currentUser) {
        var question = questionRepository.findById(dto.getQuestionId())
                .orElseThrow(() -> new QuestionNotFoundException(dto.getQuestionId()));

        var answerOption = answerOptionRepository.findById(dto.getAnswerOptionId())
                .orElseThrow(() -> new AnswerOptionNotFoundException(dto.getAnswerOptionId()));

        var session = quizSessionRepository.findById(dto.getQuizSessionId())
                .orElseThrow(() -> new SessionNotFoundException(dto.getQuizSessionId()));

        accessValidator.validateAnswerOptionBelongsToQuestion(answerOption, question);
        accessValidator.validateUserOwnsSession(currentUser, session);

        if(session.getStatus() != QuizSessionStatus.IN_PROGRESS) {
            throw new ConflictException("You can submit answers only when session is active");
        }

        var now = LocalDateTime.now();
        var endTime = session.getStartTime().plusMinutes(session.getDurationMinutes());

        if (now.isAfter(endTime)) {
            session.setStatus(QuizSessionStatus.FINISHED);
            session.setEndTime(now);

            var allAnswers = answerRepository.findByQuizSession(session);
            var total = allAnswers.size();
            var correct = allAnswers.stream().filter(Answer::getIsCorrect).count();
            int score = total == 0 ? 0 : (int) ((correct * 100.0) / total);

            session.setScore(score);
            quizSessionRepository.save(session);

            throw new ConflictException("Session expired");
        }

        var isCorrect = answerOption.getIsCorrect();

        var answer = Answer.builder()
                .answerOption(answerOption)
                .question(question)
                .quizSession(session)
                .user(currentUser)
                .isCorrect(isCorrect)
                .build();

        var savedAnswer = answerRepository.save(answer);

        log.info("User {} submitted answer to question {}. Correct: {}",
                currentUser.getId(), question.getId(), isCorrect);

        return answerMapper.toDto(savedAnswer);

    }
}
