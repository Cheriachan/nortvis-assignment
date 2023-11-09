package com.nortvis.app.controller.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class LoginRequest {

  @JsonProperty("username")
  private String username;

  @JsonProperty("password")
  private String password;
}
