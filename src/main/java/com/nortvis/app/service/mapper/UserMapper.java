package com.nortvis.app.service.mapper;

import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DateTimeUtils.class, ImageMapper.class})
public interface UserMapper {
  UserEntity userRequestToUserEntity(UserRequest userRequest);

  @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "longToLdt")
  @Mapping(source = "images", target = "images", qualifiedByName = "toImageVOList")
  UserVO userEntityToUserVO(UserEntity userentity);
}
