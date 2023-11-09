package com.nortvis.app.service.utils;

import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.entity.UserEntity;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class UserSession {

  private UserSession() {
    throw new IllegalStateException("Utility class");
  }

  public static UUID getUserId() {
    var authentication = getAuthentication();
    if (authentication.getPrincipal() instanceof String userId) {
      log.info("User id is: {}", userId);
      return UUID.fromString(userId);
    }
    var userId = ((UserEntity) authentication.getPrincipal()).getId();
    log.info("User id is: {}", userId);
    return userId;
  }

  public static UserEntity getCurrentUser() {
    return (UserEntity) getAuthentication().getPrincipal();
  }

  private static Authentication getAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AppException("User is not authenticated", HttpStatus.FORBIDDEN, "403");
    }
    return authentication;
  }
}
