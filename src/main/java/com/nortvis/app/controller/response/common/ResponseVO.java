package com.nortvis.app.controller.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {
  @JsonProperty("status")
  private int status;

  @JsonProperty("message")
  private String message;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("data")
  private List<T> data;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("total_count")
  private long totalCount;

  @JsonInclude(Include.NON_NULL)
  @JsonProperty("error")
  private ErrorVO error;

  public ResponseVO() {
    super();
    this.data = new ArrayList<>();
    this.status = 200;
    this.message = "SUCCESS";
  }

  public ResponseVO(List<T> data) {
    super();
    this.data = data;
    this.status = 200;
    this.message = "SUCCESS";
    this.totalCount = data.size();
  }
}
