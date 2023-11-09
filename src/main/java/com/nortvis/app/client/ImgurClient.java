package com.nortvis.app.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nortvis.app.exception.AppException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ImgurClient {

  @Value("${client.imgur.base-url}")
  private String imgurBaseUrl;

  @Value("${client.imgur.client-id}")
  private String clientId;

  public LinkedHashMap<String, Object> uploadImage(MultipartFile file) {
    Map<String, Object> responseDataMap;
    try {
      var body =
          new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart(
                  "image",
                  file.getOriginalFilename(),
                  RequestBody.create(
                      file.getBytes(),
                      okhttp3.MediaType.parse(MediaType.APPLICATION_OCTET_STREAM_VALUE)))
              .build();
      var request =
          new Request.Builder()
              .url(imgurBaseUrl + "/image")
              .method(HttpMethod.POST.name(), body)
              .addHeader("Authorization", "Client-ID " + clientId)
              .build();
      var response = executeRequest(request);
      log.info("Image uploaded to Imgur client");
      responseDataMap =
          new ObjectMapper().readValue(response.body().string(), new TypeReference<>() {});
    } catch (IOException ioException) {
      var errorMessage = "Exception while trying to invoke client";
      log.error(errorMessage);
      throw new AppException(
          ioException.getMessage(),
          HttpStatus.EXPECTATION_FAILED,
          "417",
          "IOException",
          errorMessage);
    }
    return new ObjectMapper().convertValue(responseDataMap.get("data"), new TypeReference<>() {});
  }

  public void deleteImage(String deleteHash) {
    Request request =
        new Request.Builder()
            .url(imgurBaseUrl + "/image/" + deleteHash)
            .delete()
            .addHeader("Authorization", "Client-ID " + clientId)
            .build();
    executeRequest(request);
    log.info("Image deleted from Imgur client");
  }

  private Response executeRequest(Request request) {

    Response response;
    try {
      var client = new OkHttpClient();
      response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        var errorMessage = "Bad request to Imgur client";
        log.error(errorMessage);
        throw new AppException(
            errorMessage, HttpStatus.BAD_REQUEST, String.valueOf(response.code()));
      }
    } catch (IOException exception) {
      var errorMessage = "Failed to access Imgur client";
      log.error(errorMessage);
      throw new AppException("Failed to access Imgur client", HttpStatus.NOT_FOUND, "404");
    }
    return response;
  }
}
