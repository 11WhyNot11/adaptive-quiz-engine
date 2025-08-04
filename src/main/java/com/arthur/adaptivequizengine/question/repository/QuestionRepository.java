package com.arthur.adaptivequizengine.question.repository;

import com.arthur.adaptivequizengine.question.entity.Difficulty;
import com.arthur.adaptivequizengine.question.entity.Question;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {

    @Query("""
        SELECT q FROM Question q
        WHERE q.difficulty = :difficulty
          AND q.id NOT IN :excludedIds
    """)
    @EntityGraph(attributePaths = "options")
    List<Question> findByDifficultyAndIdNotIn(@Param("difficulty") Difficulty difficulty,
                                              @Param("excludedIds") Set<Long> excludedIds);

    Optional<Question> findByText(String text);
}
