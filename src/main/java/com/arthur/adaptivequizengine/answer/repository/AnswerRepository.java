package com.arthur.adaptivequizengine.answer.repository;

import com.arthur.adaptivequizengine.answer.entity.Answer;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuizSession(QuizSession quizSession);

    List<Answer> findAllByQuizSession_Id(Long sessionId);
}


