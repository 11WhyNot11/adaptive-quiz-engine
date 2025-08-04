package com.arthur.adaptivequizengine.quizSession.service;

import com.arthur.adaptivequizengine.question.dto.QuestionResponseDto;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.quizSession.dto.*;
import com.arthur.adaptivequizengine.user.entity.User;

import java.util.List;

public interface QuizSessionService {

    QuizSessionResponseDto createSession(QuizSessionRequestDto dto, User currentUser);
    QuestionResponseDto startSession(Long id, User currentUser);
    TestResultDto finishSession(Long id, User currentUser);
    QuestionResponseDto getNextQuestion(Long id, User currentUser);
    List<QuizSessionSummaryDto> getAllSessionsByUsers(User user);
    List<AnswerAnalyticsDto> getAnalyticsBySession(Long sessionId, User currentUser);


}
