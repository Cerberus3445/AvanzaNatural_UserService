package com.cerberus.userservice.exception;

public class NotFoundException extends RuntimeException {

  private final static String MESSAGE_ID = "User with %d id not found.";

  private final static String MESSAGE_EMAIL = "User with %s email not found.";

  public NotFoundException(Long id) {
    super(MESSAGE_ID.formatted(id));
  }

  public NotFoundException(String email) {
    super(MESSAGE_EMAIL.formatted(email));
  }
}
