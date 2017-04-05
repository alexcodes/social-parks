package com.alexcodes.common.logic;

import com.alexcodes.common.domain.GeoPost;

public interface PostConverter {
    GeoPost convert(String message);
}
