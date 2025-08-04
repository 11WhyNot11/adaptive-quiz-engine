package com.arthur.adaptivequizengine.exception.handler;

public class InvalidAnswerException extends RuntimeException {
    public InvalidAnswerException(String message) {
        super(message);
    }
}
