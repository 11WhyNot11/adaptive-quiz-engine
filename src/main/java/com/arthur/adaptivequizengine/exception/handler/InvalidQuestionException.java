package com.arthur.adaptivequizengine.exception.handler;

public class InvalidQuestionException extends RuntimeException {
    public InvalidQuestionException(String message) {
        super(message);
    }
}
