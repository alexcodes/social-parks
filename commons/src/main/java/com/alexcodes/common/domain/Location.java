package com.alexcodes.common.domain;

import com.google.common.base.MoreObjects;

public class Location {
    public Type type;
    public double longitude;
    public double latitude;

    public Location() {}

    public Location(Type type, double longitude, double latitude) {
        this.type = type;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .toString();
    }

    public enum Type {
        POINT, CITY
    }
}
