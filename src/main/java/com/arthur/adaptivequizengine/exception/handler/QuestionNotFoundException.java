package com.arthur.adaptivequizengine.exception.handler;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(Long id) {
        super("Question not found with id " + id);
    }
}
