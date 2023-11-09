package com.nortvis.app.service.verifier;

import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.dao.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerifierImpl implements UserVerifier {
  private final UserDao usersDao;

  @Override
  public UserDetailsService userDetailsService() {
    return username -> {
      var userEntity = usersDao.findUserByName(username);
      if (userEntity == null) {
        var errorMessage = "User with username " + username + " not found in the database";
        log.error(errorMessage);
        throw new AppException(
            errorMessage,
            HttpStatus.NOT_FOUND,
            "404",
            "User not found",
            "User does not exist in the database");
      }
      log.info("User successfully fetched from DB");
      return userEntity;
    };
  }
}
