package com.nortvis.app.persistence.dao;

import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.persistence.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserDao {

  @Autowired private UserRepository userRepository;

  public UserEntity saveUser(UserEntity userEntity) {
    return userRepository.save(userEntity);
  }

  public UserEntity findUserById(UUID userId) {
    var jpaResponse = userRepository.findById(userId);
    return jpaResponse.orElse(null);
  }

  public UserEntity findUserByName(String username) {
    var jpaResponse = userRepository.findByUsername(username);
    return jpaResponse.orElse(null);
  }
}
