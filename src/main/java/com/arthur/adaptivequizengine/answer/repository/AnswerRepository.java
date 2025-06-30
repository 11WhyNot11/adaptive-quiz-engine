package com.arthur.adaptivequizengine.answer.repository;

import com.arthur.adaptivequizengine.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
