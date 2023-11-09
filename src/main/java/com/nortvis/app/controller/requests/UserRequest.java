package com.nortvis.app.controller.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class UserRequest {

  @NotBlank(message = "First name is required")
  @JsonProperty("first_name")
  private String firstName;

  @NotBlank(message = "Last name is required")
  @JsonProperty("last_name")
  private String lastName;

  @NotBlank(message = "Username is required")
  @Size(min = 8, max = 20, message = "The username must be from 8 to 20 characters.")
  @JsonProperty("username")
  private String username;

  @NotBlank(message = "The password is required.")
  @Pattern(
      regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,}$",
      message =
          "Password must be 8 characters long and combination of uppercase letters, lowercase letters, numbers, special characters.")
  @JsonProperty("password")
  private String password;

  @NotEmpty(message = "The email is required.")
  @Email(message = "The email is not a valid email.")
  @JsonProperty("email")
  private String email;

  @Range(min = 1000000000L, max = 9999999999L, message = "Phone number should be 10 digits")
  @JsonProperty("phone")
  private Long phone;
}
