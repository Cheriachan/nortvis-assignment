package com.nortvis.app.controller;

import static org.mockito.Mockito.*;

import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.exception.GlobalExceptionHandler;
import com.nortvis.app.service.UserService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ProfileControllerTest {

  @Autowired private MockMvc mockMvc;

  @Mock private UserService userService;

  @InjectMocks private ProfileController profileController;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(profileController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  public void testGettingProfileInfo() throws Exception {
    var userVO = UserVO.builder().id(UUID.randomUUID()).username("testUser").build();

    when(userService.getUser()).thenReturn(userVO);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/profile/info"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data[0].username").value(userVO.getUsername()));

    verify(userService, times(1)).getUser();
  }

  @Test
  public void testGettingProfileWithError() throws Exception {
    when(userService.getUser()).thenThrow(new RuntimeException("User not found"));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/profile/info"))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError());

    verify(userService, times(1)).getUser();
  }
}
