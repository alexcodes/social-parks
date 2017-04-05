package com.alexcodes.common.domain;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;

import java.time.Instant;

public class GeoPost {
    @Id
    public String id;

    public SourceType sourceType;
    public String text;
    public Location location;
    public Instant timestamp;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sourceType", sourceType)
                .add("text", text)
                .add("location", location)
                .add("timestamp", timestamp)
                .toString();
    }
}
