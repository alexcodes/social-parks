package com.alexcodes.twitter.logic;

import com.alexcodes.common.domain.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationExtractor {
    private static final Logger log = LoggerFactory.getLogger(LocationExtractor.class);

    private static final String COORDINATES = "coordinates";
    private static final String PLACE = "place";
    private static final String PLACE_TYPE = "place_type";
    private static final String CITY = "city";
    private static final String BOUNDING_BOX = "bounding_box";
    private static final String TYPE = "type";
    private static final String POLYGON = "Polygon";

    public Location getLocation(JsonObject root) {
        JsonElement coordinates = root.get(COORDINATES);
        if (coordinates != null && !coordinates.isJsonNull())
            return extractCoordinates(coordinates);

        JsonElement place = root.get(PLACE);
        if (place != null && !place.isJsonNull())
            return extractPlace(place);

        log.error("Cannot extract location from {}", root);
        return null;
    }

    private Location extractCoordinates(JsonElement coordinates) {
        JsonArray array = coordinates.getAsJsonObject().getAsJsonArray(COORDINATES);
        double longitude = array.get(0).getAsDouble();
        double latitude = array.get(1).getAsDouble();
        return new Location(Location.Type.POINT, longitude, latitude);
    }

    private Location extractPlace(JsonElement place) {
        String placeType = place.getAsJsonObject().get(PLACE_TYPE).getAsString();
        JsonObject boundingBox = place.getAsJsonObject().getAsJsonObject(BOUNDING_BOX);

        if (!placeType.equals(CITY)) {
            log.warn("Unknown placeType: {}", placeType);
            return null;
        }

        Point point = extractBoundingBox(boundingBox);
        return new Location(Location.Type.CITY, point.longitude, point.latitude);
    }

    private Point extractBoundingBox(JsonObject boundingBox) {
        String type = boundingBox.getAsJsonPrimitive(TYPE).getAsString();
        switch (type) {
            case POLYGON: {
                JsonArray coordinates = boundingBox.getAsJsonArray(COORDINATES)
                        .get(0)
                        .getAsJsonArray();
                List<Double> longs = new ArrayList<>(coordinates.size());
                List<Double> lats = new ArrayList<>(coordinates.size());
                for (int i = 0; i < coordinates.size(); i++) {
                    JsonArray point = coordinates.get(i).getAsJsonArray();
                    longs.add(point.get(0).getAsDouble());
                    lats.add(point.get(1).getAsDouble());
                }
                double avgLong = longs.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .getAsDouble();
                double avgLat = lats.stream()
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .getAsDouble();
                return new Point(avgLong, avgLat);
            }
            default:
                log.error("Unknown format: {}", boundingBox);
                return new Point(0.0, 0.0);
        }
    }

    private static class Point {
        public double longitude;
        public double latitude;

        public Point(double longitude, double latitude) {
            this.longitude = longitude;
            this.latitude = latitude;
        }
    }
}
