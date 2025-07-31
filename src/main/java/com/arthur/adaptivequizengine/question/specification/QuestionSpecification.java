package com.arthur.adaptivequizengine.question.specification;

import com.arthur.adaptivequizengine.question.dto.QuestionFilterRequest;
import com.arthur.adaptivequizengine.question.entity.Question;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class QuestionSpecification {

    public static Specification<Question> withFilter(QuestionFilterRequest filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getTopic() != null && !filter.getTopic().isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("topic"), filter.getTopic()));
            }

            if(filter.getDifficulty() != null) {
                predicates.add(criteriaBuilder.equal(root.get("difficulty"), filter.getDifficulty()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
