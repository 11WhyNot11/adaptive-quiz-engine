package com.arthur.adaptivequizengine.quizSession.mapper;

import ch.qos.logback.core.model.ComponentModel;
import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionSummaryDto;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionRequestDto;
import com.arthur.adaptivequizengine.quizSession.dto.QuizSessionResponseDto;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuizSessionMapper {

    @Mapping(source = "user.id", target = "userId")
    QuizSessionResponseDto toDto(QuizSession session);

    List<QuizSessionResponseDto> toDtoList(List<QuizSession> sessions);

    @Mapping(source = "currentUser", target = "user")
    @Mapping(target = "initialDifficulty", expression = "java(mapInitialDifficulty(dto))")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    QuizSession toEntity(QuizSessionRequestDto dto, User currentUser);

    QuizSessionSummaryDto toSummaryDto(QuizSession session);
    List<QuizSessionSummaryDto> toSummaryDtoList(List<QuizSession> sessions);

    default Difficulty mapInitialDifficulty(QuizSessionRequestDto dto) {
        return dto.getInitialDifficulty() != null ? dto.getInitialDifficulty() : Difficulty.MEDIUM;
    }
}
