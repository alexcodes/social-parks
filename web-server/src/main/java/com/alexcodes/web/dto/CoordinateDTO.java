package com.alexcodes.web.dto;

public class CoordinateDTO {
    public double longitude;
    public double latitude;

    public CoordinateDTO() {
    }

    public CoordinateDTO(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
