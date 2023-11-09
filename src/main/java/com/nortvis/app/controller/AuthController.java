package com.nortvis.app.controller;

import com.nortvis.app.controller.requests.LoginRequest;
import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.controller.response.common.ResponseVO;
import com.nortvis.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "Auth Controller")
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  @Autowired private UserService userService;

  @PostMapping(value = "/register")
  @Operation(description = "Register a new user")
  public ResponseEntity<ResponseVO<UserVO>> registerUser(
      @Valid @RequestBody UserRequest userRequest) {
    log.info("Register user request: {}", userRequest);
    var userVO = userService.registerUser(userRequest);
    log.info("Registering user successful:\nUserVO: {}", userVO);
    return new ResponseEntity<>(new ResponseVO<>(List.of(userVO)), HttpStatus.OK);
  }

  @PostMapping("/login")
  @Operation(description = "Login user to generate jwt token")
  public ResponseEntity<ResponseVO<UserVO>> login(@RequestBody LoginRequest loginRequest) {
    log.info("Login request for username {}", loginRequest.getUsername());
    var userVO = userService.loginUser(loginRequest);
    log.info("User successfully logged in:\nUserVO: {}", userVO);
    return new ResponseEntity<>(new ResponseVO<>(List.of(userVO)), HttpStatus.OK);
  }
}
