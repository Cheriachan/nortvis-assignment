package com.nortvis.app.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.nortvis.app.exception.AppException;
import java.io.IOException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(
    properties = {"client.imgur.base-url=http://localhost:8080", "client.imgur.client-id=qwerty"})
public class ImgurClientTest {

  private MockWebServer mockWebServer;
  @Autowired private ImgurClient imgurClient;
  @MockBean private AppException appException;

  @Value("${client.imgur.base-url}")
  private String baseUrl;

  @BeforeEach
  void setUp() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(8080);
    baseUrl = mockWebServer.url("/").toString();
  }

  @AfterEach
  void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  @Test
  void testUploadImage() throws InterruptedException {
    var mockResponse =
        "{\"data\":{\"link\":\"https://imgur.com/image\",\"deletehash\":\"delete-hash\"}}";
    mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(mockResponse));

    var result = imgurClient.uploadImage(getMultipartFile());
    var recordedRequest = mockWebServer.takeRequest();
    assertEquals("/image", recordedRequest.getPath());
    assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
    assertEquals("Client-ID qwerty", recordedRequest.getHeader("Authorization"));

    assertEquals("https://imgur.com/image", result.get("link"));
    assertEquals("delete-hash", result.get("deletehash"));
  }

  @Test
  void testUploadImageWithError() {
    mockWebServer.enqueue(new MockResponse().setResponseCode(500));
    assertThrows(AppException.class, () -> imgurClient.uploadImage(getMultipartFile()));
  }

  @Test
  void testDeleteImage() throws InterruptedException {
    var deleteHash = "delete-hash";
    mockWebServer.enqueue(new MockResponse().setResponseCode(200));

    imgurClient.deleteImage(deleteHash);

    var recordedRequest = mockWebServer.takeRequest();
    assertEquals("/image/" + deleteHash, recordedRequest.getPath());
    assertEquals("DELETE", recordedRequest.getMethod());
    assertEquals("Client-ID qwerty", recordedRequest.getHeader("Authorization"));
  }

  @Test
  void testDeleteImageWithError() {
    var deleteHash = "delete-hash";
    mockWebServer.enqueue(new MockResponse().setResponseCode(400));
    assertThrows(AppException.class, () -> imgurClient.deleteImage(deleteHash));
  }

  private MockMultipartFile getMultipartFile() {
    return new MockMultipartFile(
        "image", "image.jpeg", MediaType.IMAGE_JPEG_VALUE, "some-image".getBytes());
  }
}
