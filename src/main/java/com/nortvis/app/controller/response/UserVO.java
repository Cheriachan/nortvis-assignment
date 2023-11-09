package com.nortvis.app.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {
  @JsonProperty("id")
  private UUID id;

  @JsonProperty("username")
  private String username;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("phone")
  private Long phone;

  @JsonProperty("email")
  private String email;

  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("images")
  private List<ImageVO> images;
}
