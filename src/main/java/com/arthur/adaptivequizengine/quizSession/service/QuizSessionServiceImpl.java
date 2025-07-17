package com.arthur.adaptivequizengine.quizSession.service;

import com.arthur.adaptivequizengine.answer.entity.Answer;
import com.arthur.adaptivequizengine.answer.repository.AnswerRepository;
import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.ConflictException;
import com.arthur.adaptivequizengine.exception.handler.QuestionNotFoundException;
import com.arthur.adaptivequizengine.exception.handler.SessionNotFoundException;
import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.question.mapper.QuestionMapper;
import com.arthur.adaptivequizengine.question.repository.QuestionRepository;
import com.arthur.adaptivequizengine.quizSession.dto.*;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.quizSession.mapper.QuizSessionMapper;
import com.arthur.adaptivequizengine.quizSession.repository.QuizSessionRepository;
import com.arthur.adaptivequizengine.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class QuizSessionServiceImpl implements QuizSessionService {

    private final QuizSessionRepository quizSessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuizSessionMapper quizSessionMapper;
    private final QuestionMapper questionMapper;
    private final AccessValidator accessValidator;

    @Override
    public QuizSessionResponseDto createSession(QuizSessionRequestDto dto, User currentUser) {

        boolean hasActive = quizSessionRepository.existsByUserAndStatusIn(
                currentUser,
                List.of(QuizSessionStatus.NOT_STARTED, QuizSessionStatus.IN_PROGRESS)
        );

        if (hasActive) {
            throw new ConflictException("User already has an active quiz session");
        }

        QuizSession session = quizSessionMapper.toEntity(dto, currentUser);

        session.setStatus(QuizSessionStatus.NOT_STARTED);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(null);
        session.setScore(null);

        QuizSession savedSession = quizSessionRepository.save(session);

        return quizSessionMapper.toDto(savedSession);
    }

    @Override
    public QuestionResponseDto startSession(Long id, User currentUser) {
        QuizSession session = quizSessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));

        accessValidator.validateUserOwnsSession(currentUser, session);

        if (session.getStatus() != QuizSessionStatus.NOT_STARTED) {
            throw new ConflictException("Test cannot be started again or after the completion");
        }

        session.setStatus(QuizSessionStatus.IN_PROGRESS);
        session.setStartTime(LocalDateTime.now());

        Set<Long> answeredQuestionIds = answerRepository
                .findByQuizSession(session)
                .stream()
                .map(answer -> answer.getQuestion().getId())
                .collect(Collectors.toSet());

        Difficulty requested = session.getInitialDifficulty(); // задана або дефолтна

        List<Difficulty> fallbackOrder = Stream.of(Difficulty.values())
                .filter(diff -> diff != requested)
                .toList();

        Stream<Difficulty> searchOrder = Stream.concat(
                Stream.of(requested),
                fallbackOrder.stream()
        );

        Question question = searchOrder
                .map(diff -> questionRepository.findFirstByDifficultyAndIdNotIn(diff, answeredQuestionIds))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No available questions at any difficulty"));

        session.setInitialDifficulty(question.getDifficulty());
        quizSessionRepository.save(session);

        return questionMapper.toDto(question);
    }

    @Override
    public TestResultDto finishSession(Long id, User currentUser) {
        var session = quizSessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));

        accessValidator.validateUserOwnsSession(currentUser, session);

        if (session.getStatus() == QuizSessionStatus.NOT_STARTED) {
            throw new ConflictException("Session has not been started yet");
        }

        if (session.getStatus() == QuizSessionStatus.FINISHED) {
            throw new ConflictException("Session is already finished");
        }

        var answers = answerRepository.findByQuizSession(session);

        int totalCount = answers.size();
        long correctCount = answers.stream()
                .filter(Answer::getIsCorrect)
                .count();

        int score = totalCount == 0 ? 0 : (int) Math.round((correctCount * 100.0) / totalCount);

        LocalDateTime endTime = LocalDateTime.now();
        session.setStatus(QuizSessionStatus.FINISHED);
        session.setEndTime(endTime);
        session.setScore(score);
        quizSessionRepository.save(session);

        long durationSeconds = Duration.between(session.getStartTime(), endTime).getSeconds();

        return TestResultDto.builder()
                .totalCount(totalCount)
                .correctCount((int) correctCount)
                .score(score)
                .durationSeconds(durationSeconds)
                .status(QuizSessionStatus.FINISHED)
                .build();
    }

    @Override
    public QuestionResponseDto getNextQuestion(Long id, User currentUser) {
        var session = quizSessionRepository.findById(id)
                .orElseThrow(() -> new SessionNotFoundException(id));

        accessValidator.validateUserOwnsSession(currentUser, session);

        if (session.getStatus() == QuizSessionStatus.NOT_STARTED) {
            throw new ConflictException("Session has not been started yet");
        }

        if (session.getStatus() == QuizSessionStatus.FINISHED) {
            throw new ConflictException("Session is already finished");
        }

        if(isSessionExpired(session)) {
            return autoFinishSession(session);
        }

        List<Answer> answers = answerRepository.findByQuizSession(session);
        Set<Long> answeredQuestionIds = answers.stream()
                .map(answer -> answer.getQuestion().getId())
                .collect(Collectors.toSet());


        Difficulty nextDifficulty;
        if(answers.isEmpty()){
            nextDifficulty = session.getInitialDifficulty();
        } else {
            Answer lastAnswer = answers.get(answers.size() - 1);
            Difficulty current = lastAnswer.getQuestion().getDifficulty();
            boolean correct = lastAnswer.getIsCorrect();

            if (correct) {
                if (current == Difficulty.EASY) nextDifficulty = Difficulty.MEDIUM;
                else if (current == Difficulty.MEDIUM) nextDifficulty = Difficulty.HARD;
                else nextDifficulty = Difficulty.HARD;
            } else {
                if (current == Difficulty.HARD) nextDifficulty = Difficulty.MEDIUM;
                else if (current == Difficulty.MEDIUM) nextDifficulty = Difficulty.EASY;
                else nextDifficulty = Difficulty.EASY;
            }
        }


        var questionOpt = questionRepository.findFirstByDifficultyAndIdNotIn(nextDifficulty, answeredQuestionIds);


        if(questionOpt.isEmpty()) {
            for (Difficulty diff : Difficulty.values()) {
                if(diff != nextDifficulty) {
                    questionOpt = questionRepository.findFirstByDifficultyAndIdNotIn(diff, answeredQuestionIds);
                    if (questionOpt.isPresent()) break;
                }
            }
        }

        if (questionOpt.isEmpty()) {
            return autoFinishSession(session);
        }


        return questionMapper.toDto(questionOpt.get());

    }

    @Override
    public List<QuizSessionSummaryDto> getAllSessionsByUsers(User user) {
        var sessions = quizSessionRepository.findAllByUser(user);
        return quizSessionMapper.toSummaryDtoList(sessions);
    }

    @Override
    public List<AnswerAnalyticsDto> getAnalyticsBySession(Long sessionId, User currentUser) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        accessValidator.validateUserOwnsSession(currentUser, session);

        if (session.getStatus() != QuizSessionStatus.FINISHED) {
            throw new ConflictException("Analytics is available only after session is finished");
        }

        List<Answer> answers = answerRepository.findByQuizSession(session);

        return answers.stream()
                .collect(Collectors.groupingBy(
                        answer -> Map.entry(
                                answer.getQuestion().getTopic(),
                                answer.getQuestion().getDifficulty()
                        )
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    String topic = entry.getKey().getKey();
                    Difficulty difficulty = entry.getKey().getValue();
                    List<Answer> groupAnswers = entry.getValue();

                    long total = groupAnswers.size();
                    long correct = groupAnswers.stream().filter(Answer::getIsCorrect).count();
                    double accuracy = total == 0 ? 0.0 : (correct * 100.0) / total;

                    return AnswerAnalyticsDto.builder()
                            .topic(topic)
                            .accuracyPercent(accuracy)
                            .total(total)
                            .difficulty(difficulty)
                            .correct(correct)
                            .build();
                })
                .sorted(Comparator.comparing(AnswerAnalyticsDto::getTopic)
                        .thenComparing(AnswerAnalyticsDto::getDifficulty))
                .toList();

    }

    private QuestionResponseDto autoFinishSession(QuizSession session) {
        var answers = answerRepository.findByQuizSession(session);

        var correctAnswers = answers.stream().filter(Answer::getIsCorrect).count();
        var score = answers.isEmpty() ? 0 : (int) ((100.0 * correctAnswers) / answers.size());

        session.setStatus(QuizSessionStatus.FINISHED);
        session.setEndTime(LocalDateTime.now());
        session.setScore(score);
        quizSessionRepository.save(session);

        return null;
    }

    private boolean isSessionExpired(QuizSession quizSession) {
        if(quizSession.getStartTime() == null || quizSession.getDurationMinutes() == null) {
            return false;
        }

        return Duration.between(quizSession.getStartTime(), LocalDateTime.now()).toMinutes() >= quizSession.getDurationMinutes();
    }


}
