package com.arthur.adaptivequizengine.exception.handler;

public class SessionNotFoundException extends RuntimeException {
  public SessionNotFoundException(Long id) {
    super("Session not found with id " + id);
  }
}
