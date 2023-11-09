package com.nortvis.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.nortvis.app.client.ImgurClient;
import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.dao.ImageDao;
import com.nortvis.app.persistence.dao.UserDao;
import com.nortvis.app.persistence.entity.ImageEntity;
import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.service.mapper.DateTimeUtils;
import com.nortvis.app.service.mapper.ImageMapper;
import com.nortvis.app.service.utils.UserSession;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

  @Mock private ImageDao imageDao;
  @Mock private UserDao userDao;
  @Mock private ImageMapper imageMapper;
  @Mock private DateTimeUtils dateTimeUtils;
  @Mock private ImgurClient imgurClient;
  @InjectMocks private ImageService imageService;

  private UserEntity userEntity;
  private ImageEntity imageEntity;
  private ImageVO imageVO;
  private MultipartFile file;
  private MockedStatic<UserSession> userSessionMockedStatic;

  @BeforeEach
  public void setUp() {
    var userId = UUID.randomUUID();
    userEntity = new UserEntity();
    userEntity.setId(userId);
    userEntity.setUsername("testUser");

    imageEntity = new ImageEntity();
    imageEntity.setId(userId);
    imageEntity.setDeleteHash("hash-value");
    imageEntity.setUser(userEntity);

    imageVO = ImageVO.builder().id(UUID.randomUUID()).build();

    file = new MockMultipartFile("file", "image.jpeg", "image/jpeg", "some-image".getBytes());
    userSessionMockedStatic = mockStatic(UserSession.class);
    when(UserSession.getUserId()).thenReturn(userId);
    when(UserSession.getCurrentUser()).thenReturn(userEntity);
  }

  @AfterEach
  public void close() {
    userSessionMockedStatic.close();
  }

  @Test
  public void testAddImage() {
    LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();
    responseMap.put("link", "http://image.link");
    responseMap.put("id", "imageId");
    responseMap.put("deletehash", "deleteHash");

    when(imgurClient.uploadImage(file)).thenReturn(responseMap);
    when(userDao.findUserById(any(UUID.class))).thenReturn(userEntity);
    when(dateTimeUtils.getCurrentEpochTime()).thenReturn(System.currentTimeMillis());
    when(imageDao.saveImage(any(ImageEntity.class))).thenReturn(imageEntity);
    when(imageMapper.toImageVO(any(ImageEntity.class))).thenReturn(imageVO);

    ImageVO result = imageService.addImage(file);

    assertThat(result).isEqualTo(imageVO);
    verify(imgurClient, times(1)).uploadImage(file);
    verify(imageDao, times(1)).saveImage(any(ImageEntity.class));
  }

  @Test
  public void testFetchImages() {
    List<ImageEntity> imageEntities = Collections.singletonList(imageEntity);
    List<ImageVO> imageVOs = Collections.singletonList(imageVO);

    when(imageDao.fetchUserImages(any(UserEntity.class))).thenReturn(imageEntities);
    when(imageMapper.toImageVOList(anyList())).thenReturn(imageVOs);

    List<ImageVO> result = imageService.fetchImages();

    assertThat(result).isEqualTo(imageVOs);
    verify(imageDao, times(1)).fetchUserImages(any(UserEntity.class));
  }

  @Test
  public void testEmptyImages() {
    when(imageDao.fetchUserImages(any(UserEntity.class))).thenReturn(Collections.emptyList());

    List<ImageVO> result = imageService.fetchImages();

    assertThat(result).isEmpty();
    verify(imageDao, times(1)).fetchUserImages(any(UserEntity.class));
  }

  @Test
  public void testDeleteImage() {
    when(imageDao.findImageById(any(UUID.class))).thenReturn(imageEntity);

    imageService.deleteImage(imageEntity.getId());

    verify(imgurClient, times(1)).deleteImage(anyString());
    verify(imageDao, times(1)).deleteImage(any(ImageEntity.class));
  }

  @Test
  public void testNoImageCase() {
    when(imageDao.findImageById(any(UUID.class))).thenReturn(null);

    assertThatThrownBy(() -> imageService.deleteImage(UUID.randomUUID()))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("Image with id");
  }

  @Test
  public void testDeleteOnUserMismatch() {
    UserEntity anotherUser = new UserEntity();
    anotherUser.setId(UUID.randomUUID());
    anotherUser.setUsername("anotherUser");

    imageEntity.setUser(anotherUser);

    when(imageDao.findImageById(any(UUID.class))).thenReturn(imageEntity);

    assertThatThrownBy(() -> imageService.deleteImage(UUID.randomUUID()))
        .isInstanceOf(AppException.class)
        .hasMessageContaining("The image is not associated with the user");
  }
}
