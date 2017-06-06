package com.alexcodes.common.dao;

import com.alexcodes.common.domain.GeoPost;
import org.springframework.data.repository.CrudRepository;

public interface GeoPostRepository extends CrudRepository<GeoPost, String>, GeoPostRepositoryCustom {
}
