package com.nortvis.app.service.mapper;

import com.nortvis.app.controller.response.ImageVO;
import com.nortvis.app.persistence.entity.ImageEntity;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {DateTimeUtils.class})
public interface ImageMapper {

  @Named("toImageVO")
  @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "longToLdt")
  ImageVO toImageVO(ImageEntity imageEntity);

  @Named("toImageVOList")
  @IterableMapping(qualifiedByName = "toImageVO")
  List<ImageVO> toImageVOList(List<ImageEntity> imageEntities);
}
