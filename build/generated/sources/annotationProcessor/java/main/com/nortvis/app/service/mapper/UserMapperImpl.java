package com.nortvis.app.service.mapper;

import com.nortvis.app.controller.requests.UserRequest;
import com.nortvis.app.controller.response.UserVO;
import com.nortvis.app.persistence.entity.UserEntity;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-09T15:13:12+0530",
    comments = "version: 1.6.0.Beta1, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private DateTimeUtils dateTimeUtils;
    @Autowired
    private ImageMapper imageMapper;

    @Override
    public UserEntity userRequestToUserEntity(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.firstName( userRequest.getFirstName() );
        userEntity.lastName( userRequest.getLastName() );
        userEntity.username( userRequest.getUsername() );
        userEntity.phone( userRequest.getPhone() );
        userEntity.email( userRequest.getEmail() );
        userEntity.password( userRequest.getPassword() );

        return userEntity.build();
    }

    @Override
    public UserVO userEntityToUserVO(UserEntity userentity) {
        if ( userentity == null ) {
            return null;
        }

        UserVO.UserVOBuilder userVO = UserVO.builder();

        userVO.createdAt( dateTimeUtils.convertEpochsToLdt( userentity.getCreatedAt() ) );
        userVO.images( imageMapper.toImageVOList( userentity.getImages() ) );
        userVO.id( userentity.getId() );
        userVO.username( userentity.getUsername() );
        userVO.firstName( userentity.getFirstName() );
        userVO.lastName( userentity.getLastName() );
        userVO.phone( userentity.getPhone() );
        userVO.email( userentity.getEmail() );

        return userVO.build();
    }
}
