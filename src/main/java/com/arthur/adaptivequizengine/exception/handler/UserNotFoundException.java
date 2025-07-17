package com.arthur.adaptivequizengine.exception.handler;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
    public UserNotFoundException(String email) {
        super("User not found with id: " + email);
    }
}
