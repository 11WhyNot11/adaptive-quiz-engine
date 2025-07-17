package com.arthur.adaptivequizengine.quizSession.repository;

import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface QuizSessionRepository extends JpaRepository<QuizSession, Long> {
    boolean existsByUserAndStatusIn(User user, Collection<QuizSessionStatus> statuses);

    List<QuizSession> findAllByUser(User user);
}
