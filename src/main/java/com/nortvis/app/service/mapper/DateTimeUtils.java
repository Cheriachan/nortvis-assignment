package com.nortvis.app.service.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DateTimeUtils {
  @Named("longToLdt")
  default LocalDateTime convertEpochsToLdt(Long epochMillis) {
    return epochMillis != null
        ? Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("UTC")).toLocalDateTime()
        : null;
  }

  @Named("ldtToLong")
  default Long convertLdtToEpochs(LocalDateTime localDateTime) {
    return localDateTime != null
        ? localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
        : null;
  }

  @Named("currentEpochTime")
  default Long getCurrentEpochTime() {
    return convertLdtToEpochs(LocalDateTime.now());
  }
}
