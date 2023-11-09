package com.nortvis.app.controller.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorVO {
  private String displayTitle;
  private String displayMessage;

  @JsonInclude(Include.NON_EMPTY)
  private String errorCode;

  @JsonInclude(Include.NON_EMPTY)
  private List<String> errors;

  public ErrorVO() {
    this.displayTitle = "Some Error Occurred";
    this.displayMessage = "Please check with technical support for assistance!";
    this.errors = new ArrayList<>();
  }
}
