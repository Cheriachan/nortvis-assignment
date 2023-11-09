package com.nortvis.app.persistence.repository;

import com.nortvis.app.persistence.entity.ImageEntity;
import com.nortvis.app.persistence.entity.UserEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, UUID> {
  List<ImageEntity> findByUser(UserEntity user);
}
