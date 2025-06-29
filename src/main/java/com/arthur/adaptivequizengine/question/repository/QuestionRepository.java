package com.arthur.adaptivequizengine.question.repository;

import com.arthur.adaptivequizengine.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
