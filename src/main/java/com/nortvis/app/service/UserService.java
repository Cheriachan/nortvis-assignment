package com.nortvis.app.service;

import com.nortvis.app.config.JwtService;
import com.nortvis.app.controller.requests.LoginRequest;
import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.dao.UserDao;
import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.service.mapper.UserMapper;
import com.nortvis.app.service.utils.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private AuthenticationManager authenticationManager;
  @Autowired private JwtService jwtService;
  @Autowired private UserDao userDao;
  @Autowired private UserMapper userMapper;

  public UserVO registerUser(UserRequest userRequest) {
    if (userDao.findUserByName(userRequest.getUsername()) != null) {
      log.error("A user already exists with username {}", userRequest.getUsername());
      throw new AppException(
          "Username taken",
          HttpStatus.CONFLICT,
          "33",
          "User exists",
          "User already exists with name " + userRequest.getUsername());
    }
    userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
    var newUserEntity = userMapper.userRequestToUserEntity(userRequest);
    var createdUserEntity = userDao.saveUser(newUserEntity);
    log.info(
        "User with username {} persisted with id {}",
        userRequest.getUsername(),
        createdUserEntity.getId());
    return userMapper.userEntityToUserVO(createdUserEntity);
  }

  public UserVO loginUser(LoginRequest loginRequest) {
    Authentication authentication;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));
      log.info("Authentication successful for user {}", loginRequest.getUsername());
    } catch (InternalAuthenticationServiceException exception) {
      log.error("Internal authentication service exception: {}", exception.getMessage());
      throw new AppException(
          "Username invalid",
          HttpStatus.UNAUTHORIZED,
          "401",
          "Invalid username exception",
          "Please check the login username and try again");
    } catch (Exception exception) {
      log.error("Password exception: {}", exception.getMessage());
      throw new AppException(
          "Password invalid",
          HttpStatus.UNAUTHORIZED,
          "401",
          exception.getMessage(),
          "Please check the login password and try again");
    }
    var userEntity = (UserEntity) authentication.getPrincipal();
    var userVO = userMapper.userEntityToUserVO(userEntity);
    userVO.setAccessToken(jwtService.generateToken(userEntity));
    log.info(
        "Verification successful and access token generated for user {}",
        loginRequest.getUsername());
    return userVO;
  }

  public UserVO getUser() {
    var userId = UserSession.getUserId();
    var userEntity = userDao.findUserById(userId);
    if (userEntity == null) {
      var errorMessage = "User with id " + userId + " does not exist";
      log.error(errorMessage);
      throw new AppException(
          errorMessage,
          HttpStatus.NOT_FOUND,
          HttpStatus.NOT_FOUND.name(),
          "User not found",
          errorMessage);
    }
    log.info("User session verified and retrieved successfully");
    return userMapper.userEntityToUserVO(userEntity);
  }
}
