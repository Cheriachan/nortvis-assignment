package com.nortvis.app.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nortvis.app.persistence.entity.ImageEntity;
import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.persistence.repository.ImageRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageDaoTest {

  @Mock private ImageRepository imageRepository;

  @InjectMocks private ImageDao imageDao;

  private final UUID imageId = UUID.randomUUID();

  @Test
  public void testSaveImage() {
    var imageEntityRequest = ImageEntity.builder().build();
    var imageEntity = getImageEntity();
    when(imageRepository.save(Mockito.any(ImageEntity.class))).thenReturn(imageEntity);

    ImageEntity savedImage = imageDao.saveImage(imageEntityRequest);

    assertEquals(imageEntity.getImgurId(), savedImage.getImgurId());
    assertEquals(imageEntity.getUrl(), savedImage.getUrl());
    assertEquals(imageEntity.getCreatedAt(), savedImage.getCreatedAt());
    assertEquals(imageEntity, savedImage);
    verify(imageRepository, times(1)).save(imageEntityRequest);
  }

  @Test
  public void testFindImageById() {
    var imageEntity = getImageEntity();
    when(imageRepository.findById(imageId)).thenReturn(Optional.of(imageEntity));

    ImageEntity foundImage = imageDao.findImageById(imageId);

    assertEquals(imageEntity, foundImage);
    verify(imageRepository, times(1)).findById(imageId);
  }

  @Test
  public void testFetchUserImages() {
    var userEntity = UserEntity.builder().build();
    List<ImageEntity> imageEntities = Collections.singletonList(getImageEntity());
    when(imageRepository.findByUser(Mockito.any(UserEntity.class))).thenReturn(imageEntities);

    List<ImageEntity> fetchedImages = imageDao.fetchUserImages(userEntity);

    assertEquals(imageEntities, fetchedImages);
    verify(imageRepository, times(1)).findByUser(userEntity);
  }

  @Test
  public void testImageDeletion() {
    var imageEntity = getImageEntity();
    doNothing().when(imageRepository).delete(Mockito.any(ImageEntity.class));

    imageDao.deleteImage(imageEntity);

    verify(imageRepository, times(1)).delete(imageEntity);
  }

  private ImageEntity getImageEntity() {
    return ImageEntity.builder()
        .id(imageId)
        .url("http://image")
        .imgurId("qwerty")
        .deleteHash("delete-hash")
        .createdAt(1699504494858L)
        .build();
  }
}
