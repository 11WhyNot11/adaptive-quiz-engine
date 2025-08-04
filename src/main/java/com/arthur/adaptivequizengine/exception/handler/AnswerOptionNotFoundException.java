package com.arthur.adaptivequizengine.exception.handler;

public class AnswerOptionNotFoundException extends RuntimeException {
    public AnswerOptionNotFoundException(Long id) {
        super("Answer option not found with id " + id);
    }
}
