package com.nortvis.app.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.exception.GlobalExceptionHandler;
import com.nortvis.app.service.ImageService;
import java.util.Arrays;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ImageControllerTest {

  @Autowired private MockMvc mockMvc;

  @Mock private ImageService imageService;

  private ImageVO imageVO;

  @InjectMocks private ImageController imageController;

  @BeforeEach
  public void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(imageController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    imageVO =
        ImageVO.builder()
            .id(UUID.randomUUID())
            .imgurId("imgurId")
            .url("url")
            .deleteHash("deleteHash")
            .build();
  }

  @Test
  public void testImageUpload() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile("file", "image.jpeg", "image/jpeg", "some-image".getBytes());

    when(imageService.addImage(any())).thenReturn(imageVO);

    mockMvc
        .perform(multipart("/image/upload").file(file))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data[0].id").value(imageVO.getId().toString()));
  }

  @Test
  public void testImagesFetch() throws Exception {
    when(imageService.fetchImages()).thenReturn(Arrays.asList(imageVO));

    mockMvc
        .perform(get("/image/list"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data[0].id").value(imageVO.getId().toString()));
  }

  @Test
  public void testImagesDeletion() throws Exception {
    mockMvc
        .perform(delete("/image/{image_id}/delete", imageVO.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.data").isEmpty());
  }
}
