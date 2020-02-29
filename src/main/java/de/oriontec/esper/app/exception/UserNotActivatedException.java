package de.oriontec.esper.app.exception;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

  public UserNotActivatedException(String msg) {
    super(msg);
  }
}
