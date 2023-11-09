package com.nortvis.app.service.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Convertors {
  public static LocalDateTime epochsToLdt(Long epochMillis) {
    return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of("UTC")).toLocalDateTime();
  }

  public static Long ldtToEpochs(LocalDateTime localDateTime) {
    return localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
  }
}
