package com.arthur.adaptivequizengine.question.repository;

import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestion_Id(Long questionId);
}

