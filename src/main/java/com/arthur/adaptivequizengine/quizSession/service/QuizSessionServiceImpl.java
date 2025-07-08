package com.arthur.adaptivequizengine.quizSession.service;

import com.arthur.adaptivequizengine.common.access.AccessValidator;
import com.arthur.adaptivequizengine.exception.handler.ConflictException;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionRequestDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionResponseDto;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.quizSession.mapper.QuizSessionMapper;
import com.arthur.adaptivequizengine.quizSession.repository.QuizSessionRepository;
import com.arthur.adaptivequizengine.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizSessionServiceImpl implements QuizSessionService {

    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionMapper quizSessionMapper;
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
}
