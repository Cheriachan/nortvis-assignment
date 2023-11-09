package com.nortvis.app.persistence.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.nortvis.app.persistence.entity.UserEntity;
import com.nortvis.app.persistence.repository.UserRepository;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserDao userDao;

  private final UUID userId = UUID.randomUUID();
  private final String username = "test-user";

  @Test
  public void testSaveUser() {
    var createUserEntity = UserEntity.builder().build();
    var userEntity = getUserEntity();
    when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

    UserEntity result = userDao.saveUser(createUserEntity);

    assertEquals(userEntity.getFirstName(), result.getFirstName());
    assertEquals(userEntity.getLastName(), result.getLastName());
    assertEquals(userEntity.getPhone(), result.getPhone());
    assertEquals(userEntity.getEmail(), result.getEmail());
    assertEquals(userEntity.getPassword(), result.getPassword());
    assertEquals(userEntity.getImages(), result.getImages());
    assertEquals(userEntity, result);
    verify(userRepository, times(1)).save(createUserEntity);
  }

  @Test
  public void testFindUserById() {
    var userEntity = getUserEntity();
    when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(userEntity));

    UserEntity result = userDao.findUserById(userId);

    assertEquals(userEntity, result);
    verify(userRepository, times(1)).findById(userId);
  }

  @Test
  public void testFindUserByName() {
    var userEntity = getUserEntity();
    when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(userEntity));

    UserEntity result = userDao.findUserByName(username);

    assertEquals(userEntity, result);
    verify(userRepository, times(1)).findByUsername(username);
  }

  private UserEntity getUserEntity() {
    return UserEntity.builder()
        .id(userId)
        .username(username)
        .firstName("first-name")
        .lastName("last-name")
        .email("user@email.com")
        .phone(9999999999L)
        .password("password")
        .images(new ArrayList<>())
        .createdAt(1699504494858L)
        .build();
  }
}
