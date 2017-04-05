package com.alexcodes.twitter.config;

import com.twitter.hbc.core.endpoint.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterConfig {

    @Value("${twitter.location.southwest.longitude}")
    private double southWestLongitude;

    @Value("${twitter.location.southwest.latitude}")
    private double southWestLatitude;

    @Value("${twitter.location.northeast.longitude}")
    private double northEastLongitude;

    @Value("${twitter.location.northeast.latitude}")
    private double northEastLatitude;

    @Bean
    public Location location() {
        return new Location(
                new Location.Coordinate(southWestLongitude, southWestLatitude),
                new Location.Coordinate(northEastLongitude, northEastLatitude));
    }
}
