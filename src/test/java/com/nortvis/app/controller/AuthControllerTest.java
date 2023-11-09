package com.nortvis.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nortvis.app.controller.requests.LoginRequest;
import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.controller.response.common.ResponseVO;
import com.nortvis.app.exception.GlobalExceptionHandler;
import com.nortvis.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

  @Mock private UserService userService;

  private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Autowired private MockMvc mockMvc;

  @InjectMocks private AuthController authController;

  @BeforeEach
  public void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
  }

  @Test
  void registerUserTest() throws Exception {
    Mockito.when(userService.registerUser(Mockito.any())).thenReturn(UserVO.builder().build());
    var resultActions =
        mockMvc
            .perform(
                post("/auth/register")
                    .content(
                        OBJECT_MAPPER.writeValueAsString(
                            UserRequest.builder()
                                .firstName("Cherian")
                                .lastName("Roy")
                                .email("cherianthakidiyel@gmail.com")
                                .password("Assignment#1")
                                .username("cherian_roy")
                                .phone(9999999999L)
                                .build()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    var response =
        OBJECT_MAPPER.readValue(
            resultActions.andReturn().getResponse().getContentAsString(), ResponseVO.class);
    assertEquals(200, response.getStatus());
    assertEquals("SUCCESS", response.getMessage());
  }

  @Test
  void testRegisterUser() throws Exception {
    var resultActions =
        mockMvc
            .perform(
                post("/auth/register")
                    .content(OBJECT_MAPPER.writeValueAsString(UserRequest.builder().build()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    var response =
        OBJECT_MAPPER.readValue(
            resultActions.andReturn().getResponse().getContentAsString(), ResponseVO.class);
    assertEquals(400, response.getStatus());
    assertEquals("Bad Request", response.getMessage());
  }

  @Test
  void testLoginUser() throws Exception {
    Mockito.when(userService.loginUser(Mockito.any())).thenReturn(UserVO.builder().build());
    var resultActions =
        mockMvc
            .perform(
                post("/auth/login")
                    .content(
                        OBJECT_MAPPER.writeValueAsString(
                            LoginRequest.builder()
                                .username("cherian_roy")
                                .password("password")
                                .build()))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print());
    var response =
        OBJECT_MAPPER.readValue(
            resultActions.andReturn().getResponse().getContentAsString(), ResponseVO.class);
    assertEquals(200, response.getStatus());
    assertEquals("SUCCESS", response.getMessage());
  }
}
