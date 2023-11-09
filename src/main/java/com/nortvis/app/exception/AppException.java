package com.nortvis.app.exception;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
  @Serial private static final long serialVersionUID = -8712045394717642025L;
  private final String errorCode;
  private final HttpStatus status;
  private final String displayTitle;
  private final String displayMessage;

  public AppException(String message, HttpStatus status, String errorCode) {
    super(message);
    this.status = status;
    this.errorCode = errorCode;
    this.displayTitle = "Some Error Occurred";
    this.displayMessage = "Please check with technical support for assistance!";
  }

  public AppException(
      String message,
      HttpStatus status,
      String errorCode,
      String displayTitle,
      String displayMessage) {
    super(message);
    this.status = status;
    this.displayTitle = displayTitle;
    this.displayMessage = displayMessage;
    this.errorCode = errorCode;
  }
}
