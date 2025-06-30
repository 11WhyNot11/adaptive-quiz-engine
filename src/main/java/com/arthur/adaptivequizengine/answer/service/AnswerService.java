package com.arthur.adaptivequizengine.answer.service;

import com.arthur.adaptivequizengine.answer.dto.AnswerRequestDto;
import com.arthur.adaptivequizengine.answer.dto.AnswerResponseDto;
import com.arthur.adaptivequizengine.user.entity.User;

public interface AnswerService {

    AnswerResponseDto submitAnswer(AnswerRequestDto dto, User currentUser);
}
