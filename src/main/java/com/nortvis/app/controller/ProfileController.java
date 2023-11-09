package com.nortvis.app.controller;

import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.controller.response.common.ResponseVO;
import com.nortvis.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Profile Controller")
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

  @Autowired private UserService userService;

  @GetMapping("/info")
  @Operation(description = "Fetch the current user details and associated images")
  public ResponseEntity<ResponseVO<UserVO>> fetchUser() {
    log.info("Fetching current user info and associated images");
    var userVO = userService.getUser();
    log.info("User profile fetched:\nUserVO: {}", userVO);
    return new ResponseEntity<>(new ResponseVO<>(List.of(userVO)), HttpStatus.OK);
  }
}
