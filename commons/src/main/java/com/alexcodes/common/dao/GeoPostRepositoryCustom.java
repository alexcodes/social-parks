package com.alexcodes.common.dao;

import com.alexcodes.common.domain.GeoPost;

import java.time.Instant;
import java.util.List;

public interface GeoPostRepositoryCustom {
    List<GeoPost> findBetweenTimestamps(Instant dateFrom, Instant dateTo);
}
