package com.nortvis.app.service;

import com.nortvis.app.client.ImgurClient;
import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.exception.AppException;
import com.nortvis.app.persistence.dao.ImageDao;
import com.nortvis.app.persistence.dao.UserDao;
import com.nortvis.app.persistence.entity.ImageEntity;
import com.nortvis.app.service.mapper.DateTimeUtils;
import com.nortvis.app.service.mapper.ImageMapper;
import com.nortvis.app.service.utils.UserSession;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

  @Autowired private ImageDao imageDao;
  @Autowired private UserDao userDao;
  @Autowired private ImageMapper imageMapper;
  @Autowired private DateTimeUtils dateTimeUtils;
  @Autowired private ImgurClient imgurClient;

  public ImageVO addImage(MultipartFile file) {
    var responseMap = imgurClient.uploadImage(file);
    log.info("{} successfully uploaded to Imgur client", file.getOriginalFilename());
    var newImageEntity =
        ImageEntity.builder()
            .url((String) responseMap.get("link"))
            .imgurId((String) responseMap.get("id"))
            .deleteHash((String) responseMap.get("deletehash"))
            .user(userDao.findUserById(UserSession.getUserId()))
            .createdAt(dateTimeUtils.getCurrentEpochTime())
            .build();
    var createdImageEntity = imageDao.saveImage(newImageEntity);
    log.info("Image persisted with id {}", createdImageEntity.getId());
    return imageMapper.toImageVO(createdImageEntity);
  }

  public List<ImageVO> fetchImages() {
    var currentUser = UserSession.getCurrentUser();
    log.info("Fetched user {} from session", currentUser.getUsername());
    var imageEntityList = imageDao.fetchUserImages(currentUser);
    log.info(
        "Fetched {} images associated with {}", imageEntityList.size(), currentUser.getUsername());
    return imageMapper.toImageVOList(imageEntityList);
  }

  public void deleteImage(UUID imageId) {
    var imageEntity = imageDao.findImageById(imageId);
    if (imageEntity == null) {
      log.error("Image not found in the database with id {}", imageId);
      throw new AppException(
          "Image with id " + imageId + " not found",
          HttpStatus.NOT_FOUND,
          "404",
          "Image not found",
          "Image does not exist in the database");
    }
    var currentUser = UserSession.getCurrentUser();
    log.info("User {} identified from the session", currentUser.getUsername());
    if (!imageEntity.getUser().equals(currentUser)) {
      var errorMessage = "The image is not associated with the user";
      log.error(errorMessage);
      throw new AppException(
          errorMessage,
          HttpStatus.NOT_ACCEPTABLE,
          "406",
          "User mismatch",
          "Image not associated with the user");
    }
    imgurClient.deleteImage(imageEntity.getDeleteHash());
    log.info("Image with id {} deleted from Imgur client", imageId);
    imageDao.deleteImage(imageEntity);
    log.info("ImageEntity with id {} deleted from the database", imageId);
  }
}
