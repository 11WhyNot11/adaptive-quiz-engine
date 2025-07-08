package com.arthur.adaptivequizengine.quizSession.service;

import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionRequestDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;

public interface QuizSessionService {

    QuizSessionResponseDto createSession(QuizSessionRequestDto dto, User currentUser);


}
