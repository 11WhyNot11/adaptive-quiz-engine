package com.arthur.adaptivequizengine.question.repository;

import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
}
