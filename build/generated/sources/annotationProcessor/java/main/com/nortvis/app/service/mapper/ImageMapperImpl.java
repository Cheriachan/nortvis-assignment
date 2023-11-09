package com.nortvis.app.service.mapper;

import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.persistence.entity.ImageEntity;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-09T15:13:12+0530",
    comments = "version: 1.6.0.Beta1, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.4.jar, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class ImageMapperImpl implements ImageMapper {

    @Autowired
    private DateTimeUtils dateTimeUtils;

    @Override
    public ImageVO toImageVO(ImageEntity imageEntity) {
        if ( imageEntity == null ) {
            return null;
        }

        ImageVO.ImageVOBuilder imageVO = ImageVO.builder();

        imageVO.createdAt( dateTimeUtils.convertEpochsToLdt( imageEntity.getCreatedAt() ) );
        imageVO.id( imageEntity.getId() );
        imageVO.imgurId( imageEntity.getImgurId() );
        imageVO.url( imageEntity.getUrl() );
        imageVO.deleteHash( imageEntity.getDeleteHash() );

        return imageVO.build();
    }

    @Override
    public List<ImageVO> toImageVOList(List<ImageEntity> imageEntities) {
        if ( imageEntities == null ) {
            return null;
        }

        List<ImageVO> list = new ArrayList<ImageVO>( imageEntities.size() );
        for ( ImageEntity imageEntity : imageEntities ) {
            list.add( toImageVO( imageEntity ) );
        }

        return list;
    }
}
