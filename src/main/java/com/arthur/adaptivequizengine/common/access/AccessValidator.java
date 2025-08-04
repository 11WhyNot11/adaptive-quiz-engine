package com.arthur.adaptivequizengine.common.access;

import com.arthur.adaptivequizengine.exception.handler.ConflictException;
import com.arthur.adaptivequizengine.exception.handler.InvalidAnswerException;
import com.arthur.adaptivequizengine.question.entity.AnswerOption;
import com.arthur.adaptivequizengine.question.entity.Question;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSession;
import com.arthur.adaptivequizengine.quizSession.entity.QuizSessionStatus;
import com.arthur.adaptivequizengine.user.entity.Role;
import com.arthur.adaptivequizengine.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AccessValidator {

    public void validateIsAdmin(User currentUser) {
        if(currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Only admin can access this resource");
        }
    }

    public void validateCanDeleteUser(User currentUser, Long targetUserId) {
        if(!currentUser.getId().equals(targetUserId) && currentUser.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You don't have permission to delete this user");
        }
    }

    public void validateAnswerOptionBelongsToQuestion(AnswerOption option, Question question) {
        if(!option.getQuestion().getId().equals(question.getId())) {
            throw new InvalidAnswerException("Answer option does not belong to the given question");
        }
    }

    public void validateUserOwnsSession(User currentUser, QuizSession session) {
        if(!currentUser.getId().equals(session.getUser().getId())) {
            throw new AccessDeniedException("Only user who created the session can access this resource");
        }
    }

    public void validateCanViewSession(QuizSession session, User user) {
        if (!user.getRole().equals(Role.ADMIN) && !user.getId().equals(session.getUser().getId())) {
            throw new AccessDeniedException("You can only view your own sessions");
        }

        if (!session.getStatus().equals(QuizSessionStatus.FINISHED)) {
            throw new ConflictException("Cannot view analytics for unfinished session");
        }
    }
}
