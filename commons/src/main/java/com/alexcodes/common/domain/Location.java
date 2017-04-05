package com.alexcodes.common.domain;

import com.google.common.base.MoreObjects;

public class Location {
    public double longitude;
    public double latitude;

    public Location() {}

    public Location(double longitude, double latitude) {
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
}
