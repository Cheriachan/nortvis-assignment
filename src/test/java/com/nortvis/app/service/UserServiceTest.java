package com.nortvis.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.nortvis.app.config.JwtService;
import com.nortvis.app.controller.requests.LoginRequest;
import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.dao.UserDao;
import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.service.mapper.UserMapper;
import com.nortvis.app.service.utils.UserSession;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private AuthenticationManager authenticationManager;
  @Mock private JwtService jwtService;
  @Mock private UserDao userDao;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserService userService;

  private UserRequest userRequest;
  private LoginRequest loginRequest;
  private UserEntity userEntity;
  private UserVO userVO;
  private Authentication authentication;
  private UUID userId;

  private MockedStatic<UserSession> userSessionMockedStatic;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    userRequest =
        UserRequest.builder()
            .firstName("Cherian")
            .lastName("Roy")
            .email("cherian@gmail.com")
            .password("Assignment#1")
            .username("cherian_roy")
            .phone(9999999999L)
            .build();
    loginRequest = LoginRequest.builder().build();
    userEntity =
        UserEntity.builder()
            .id(userId)
            .images(new ArrayList<>())
            .phone(9999999999L)
            .username("cherian_roy")
            .email("cherian@email.com")
            .createdAt(1699504494858L)
            .build();
    userVO = UserVO.builder().build();
    authentication =
        new UsernamePasswordAuthenticationToken(
            loginRequest.getUsername(), loginRequest.getPassword());
    userSessionMockedStatic = mockStatic(UserSession.class);
    when(UserSession.getUserId()).thenReturn(userId);
  }

  @AfterEach
  public void close() {
    userSessionMockedStatic.close();
  }

  @Test
  @DisplayName("testRegisterUserWhenUserDoesNotExistThenUserRegistered")
  void testRegisterUserWhenUserDoesNotExistThenUserRegistered() {
    when(userDao.findUserByName(userRequest.getUsername())).thenReturn(null);
    when(userMapper.userRequestToUserEntity(userRequest)).thenReturn(userEntity);
    when(userDao.saveUser(userEntity)).thenReturn(userEntity);
    when(userMapper.userEntityToUserVO(userEntity)).thenReturn(userVO);

    UserVO result = userService.registerUser(userRequest);

    assertNotNull(result);
    assertEquals(userVO, result);
  }

  @Test
  @DisplayName("testRegisterUserWhenUserExistsThenAppExceptionThrown")
  void testRegisterUserWhenUserExistsThenAppExceptionThrown() {
    when(userDao.findUserByName(userRequest.getUsername())).thenReturn(userEntity);

    assertThrows(AppException.class, () -> userService.registerUser(userRequest));
  }

  @Test
  @DisplayName("testLoginUserWhenCredentialsAreValidThenUserLoggedIn")
  void testLoginUserWhenCredentialsAreValidThenUserLoggedIn() {

    ReflectionTestUtils.setField(authentication, "principal", userEntity);
    when(authenticationManager.authenticate(any())).thenReturn(authentication);
    when(userMapper.userEntityToUserVO(userEntity)).thenReturn(userVO);
    when(jwtService.generateToken(userEntity)).thenReturn("token");

    UserVO result = userService.loginUser(loginRequest);

    assertNotNull(result);
    assertEquals(userVO, result);
    assertEquals("token", result.getAccessToken());
  }

  @Test
  @DisplayName("testLoginUserWhenInvalidUsernameThenAppExceptionThrown")
  void testLoginUserWhenInvalidUsernameThenAppExceptionThrown() {
    when(authenticationManager.authenticate(any()))
        .thenThrow(InternalAuthenticationServiceException.class);

    assertThrows(AppException.class, () -> userService.loginUser(loginRequest));
  }

  @Test
  @DisplayName("testLoginUserWhenInvalidPasswordThenAppExceptionThrown")
  void testLoginUserWhenInvalidPasswordThenAppExceptionThrown() {
    when(authenticationManager.authenticate(any())).thenThrow(RuntimeException.class);

    assertThrows(AppException.class, () -> userService.loginUser(loginRequest));
  }

  @Test
  @DisplayName("testGetUserWhenUserExistsThenUserRetrieved")
  void testGetUserWhenUserExistsThenUserRetrieved() {
    when(userDao.findUserById(userId)).thenReturn(userEntity);
    when(userMapper.userEntityToUserVO(userEntity)).thenReturn(userVO);

    UserVO result = userService.getUser();

    assertNotNull(result);
    assertEquals(userVO, result);
  }

  @Test
  @DisplayName("testGetUserWhenUserDoesNotExistThenAppExceptionThrown")
  void testGetUserWhenUserDoesNotExistThenAppExceptionThrown() {
    when(userDao.findUserById(userId)).thenReturn(null);

    assertThrows(AppException.class, () -> userService.getUser());
  }

  @Test
  @DisplayName("testGetUserWhenUserIdNotFoundThenAppExceptionThrown")
  void testGetUserWhenUserIdNotFoundThenAppExceptionThrown() {
    when(UserSession.getUserId()).thenReturn(null);

    assertThrows(AppException.class, () -> userService.getUser());
  }
}
