package com.alexcodes.common.domain;

import com.google.common.base.MoreObjects;
import org.springframework.data.annotation.Id;

public class OpenDataObject {
    @Id
    public Integer id;
    public Type type;
    public String name;
    public Location location;

    public enum Type {
        LEISURE, CULTURE, CATERING
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .add("name", name)
                .add("location", location)
                .toString();
    }
}
