package com.nortvis.app.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageVO {

  @JsonProperty("id")
  private UUID id;

  @JsonProperty("imgur_id")
  private String imgurId;

  private String url;

  @JsonProperty("delete_hash")
  private String deleteHash;

  @JsonProperty("created_at")
  private LocalDateTime createdAt;
}
