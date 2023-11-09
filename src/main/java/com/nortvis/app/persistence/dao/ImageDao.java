package com.nortvis.app.persistence.dao;

import com.nortvis.app.persistence.entity.ImageEntity;
import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.persistence.repository.ImageRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ImageDao {

  @Autowired private ImageRepository imageRepository;

  public ImageEntity saveImage(ImageEntity imageEntity) {
    return imageRepository.save(imageEntity);
  }

  public ImageEntity findImageById(UUID imageId) {
    var jpaResponse = imageRepository.findById(imageId);
    return jpaResponse.orElse(null);
  }

  public List<ImageEntity> fetchUserImages(UserEntity userEntity) {
    return imageRepository.findByUser(userEntity);
  }

  public void deleteImage(ImageEntity imageEntity) {
    imageRepository.delete(imageEntity);
  }
}
