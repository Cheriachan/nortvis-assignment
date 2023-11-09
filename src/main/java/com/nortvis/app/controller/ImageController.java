package com.nortvis.app.controller;

import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.controller.response.common.ResponseVO;
import com.nortvis.app.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Tag(name = "Image Controller")
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

  @Autowired private ImageService imageService;

  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(description = "Upload an image to the repository")
  public ResponseEntity<ResponseVO<ImageVO>> addImage(@RequestParam("file") MultipartFile file) {
    log.info("Upload image request for {}", file.getOriginalFilename());
    var imageVO = imageService.addImage(file);
    log.info("Image successfully uploaded to the client and DB:\nImageVO: {}", imageVO);
    return new ResponseEntity<>(new ResponseVO<>(List.of(imageVO)), HttpStatus.OK);
  }

  @GetMapping("/list")
  @Operation(description = "List images of current logged in user")
  public ResponseEntity<ResponseVO<ImageVO>> getImages() {
    log.info("Fetching images associated with the current user");
    var imageVOList = imageService.fetchImages();
    log.info("Images fetched:\nImageVOList: {}", imageVOList);
    return new ResponseEntity<>(new ResponseVO<>(imageVOList), HttpStatus.OK);
  }

  @DeleteMapping("/{image_id}/delete")
  @Operation(description = "Delete the image from the application and Imgur client")
  public ResponseEntity<ResponseVO<ImageVO>> deleteImage(@PathVariable("image_id") UUID imageId) {
    log.info("Deleting image {}", imageId);
    imageService.deleteImage(imageId);
    log.info("Deletion successful");
    return new ResponseEntity<>(new ResponseVO<>(), HttpStatus.OK);
  }
}
