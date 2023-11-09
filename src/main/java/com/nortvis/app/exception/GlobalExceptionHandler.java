package com.nortvis.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nortvis.app.controller.response.common.ErrorVO;
import com.nortvis.app.controller.response.common.ResponseVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AppException.class)
  protected ResponseEntity<ResponseVO<Object>> handle(AppException exception) {
    ErrorVO errorVO = new ErrorVO();
    errorVO.getErrors().add(exception.getMessage());
    errorVO.setErrorCode(exception.getErrorCode());
    if (exception.getDisplayTitle() != null) {
      errorVO.setDisplayTitle(exception.getDisplayTitle());
    }
    if (exception.getDisplayMessage() != null) {
      errorVO.setDisplayMessage(exception.getDisplayMessage());
    }
    ResponseVO<Object> responseVO = new ResponseVO<>();
    responseVO.setStatus(exception.getStatus().value());
    responseVO.setMessage(exception.getStatus().getReasonPhrase());
    responseVO.setError(errorVO);

    return new ResponseEntity<>(responseVO, exception.getStatus());
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ResponseVO<Object>> handle(
      HttpServletRequest request, AccessDeniedException exception) {
    logError(request, exception);
    ErrorVO errorVO = new ErrorVO();
    errorVO.setErrorCode("403");
    errorVO.getErrors().add("Not authorized to access the requested resource");
    errorVO.getErrors().add(exception.getMessage());
    ResponseVO<Object> responseVO = new ResponseVO<>();
    responseVO.setStatus(HttpStatus.UNAUTHORIZED.value());
    responseVO.setMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    responseVO.setError(errorVO);

    return new ResponseEntity<>(responseVO, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseVO<Object>> handle(
      HttpServletRequest request, MethodArgumentNotValidException exception) {
    logError(request, exception);
    List<String> validationErrors = new ArrayList<>();
    for (FieldError fieldError : exception.getFieldErrors()) {
      if (fieldError.getDefaultMessage() != null) {
        validationErrors.add(
            this.convertToSnakeCase(fieldError.getField()) + " " + fieldError.getDefaultMessage());
      }
    }
    ErrorVO errorVO = new ErrorVO();
    errorVO.setErrorCode("400");
    errorVO.setDisplayTitle("Validation error");
    errorVO.setDisplayMessage("Request validation failed for the given fields");
    errorVO.setErrors(validationErrors);
    ResponseVO<Object> responseVO = new ResponseVO<>();
    responseVO.setStatus(HttpStatus.BAD_REQUEST.value());
    responseVO.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
    responseVO.setError(errorVO);

    return new ResponseEntity<>(responseVO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ResponseVO<Object>> handle(
      HttpServletRequest request, ConstraintViolationException exception) {
    logError(request, exception);

    ErrorVO errorVO = new ErrorVO();
    errorVO.setErrorCode("400");
    errorVO.getErrors().add(exception.getMessage());
    errorVO.setDisplayTitle("Bad Request");
    errorVO.setDisplayMessage("Constraint violated");
    ResponseVO<Object> responseVO = new ResponseVO<>();
    responseVO.setStatus(HttpStatus.BAD_REQUEST.value());
    responseVO.setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
    responseVO.setError(errorVO);

    return new ResponseEntity<>(responseVO, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseVO<Object>> handle(
      HttpServletRequest request, Exception exception) {
    logError(request, exception);
    ErrorVO errorVO = new ErrorVO();
    errorVO.getErrors().add(exception.getMessage());
    ResponseVO<Object> responseVO = new ResponseVO<>();
    errorVO.setErrorCode("500");
    responseVO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    responseVO.setMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    responseVO.setError(errorVO);

    return new ResponseEntity<>(responseVO, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static byte[] getUnauthorizedResponseForAuthFilterErrors(Exception exception) {
    ErrorVO errorVO = new ErrorVO();
    errorVO.getErrors().add(exception.getMessage());
    if (exception instanceof AppException appException) {
      errorVO.setErrorCode(appException.getErrorCode());
    } else {
      errorVO.setErrorCode("401");
    }
    ResponseVO<Object> responseVO = new ResponseVO<>();
    responseVO.setStatus(HttpStatus.UNAUTHORIZED.value());
    responseVO.setMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
    responseVO.setError(errorVO);
    String responseString = convertToJsonString(responseVO);
    return responseString.getBytes();
  }

  private static String convertToJsonString(ResponseVO<Object> response) {
    String responseString;
    try {
      responseString = new ObjectMapper().writeValueAsString(response);
    } catch (JsonProcessingException e) {
      responseString = response.toString();
    }
    return responseString;
  }

  private void logError(HttpServletRequest request, Exception exception) {
    log.error("Failed {} {} ", request.getMethod(), request.getRequestURI(), exception);
  }

  private String convertToSnakeCase(String camelCaseString) {
    return camelCaseString.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
  }
}
