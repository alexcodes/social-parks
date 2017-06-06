package com.alexcodes.opendata.logic;

import com.alexcodes.common.domain.Location;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationExtractor {
    private static final Logger log = LoggerFactory.getLogger(LocationExtractor.class);

    private static final String TYPE = "type";
    private static final String POLYGON = "Polygon";
    private static final String MULTI_POLYGON = "MultiPolygon";
    private static final String POINT = "Point";
    private static final String MULTI_POINT = "MultiPoint";
    private static final String COORDINATES = "coordinates";

    public Location parse(JsonObject root) {
        String type = root.getAsJsonPrimitive(TYPE).getAsString();

        switch (type) {
            case POLYGON: {
                JsonElement coordinates = root.getAsJsonArray(COORDINATES);
                Point point = parsePolygon(coordinates);
                return new Location(Location.Type.POINT, point.longitude, point.latitude);
            }
            case MULTI_POLYGON: {
                JsonElement coordinates = root.getAsJsonArray(COORDINATES);
                Point point = parseMultiPolygon(coordinates);
                return new Location(Location.Type.POINT, point.longitude, point.latitude);
            }
            case POINT: {
                JsonElement coordinates = root.getAsJsonArray(COORDINATES);
                Point point = parsePoint(coordinates);
                return new Location(Location.Type.POINT, point.longitude, point.latitude);
            }
            case MULTI_POINT: {
                JsonElement coordinates = root.getAsJsonArray(COORDINATES);
                Point point = parseMultiPoint(coordinates);
                return new Location(Location.Type.POINT, point.longitude, point.latitude);
            }
            default:
                log.debug("Source: {}", root);
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    private Point parsePoint(JsonElement element) {
        Assert.isTrue(!element.isJsonNull(), "");

        JsonArray array = element.getAsJsonArray();
        double longitude = array.get(0).getAsJsonPrimitive().getAsDouble();
        double latitude = array.get(1).getAsJsonPrimitive().getAsDouble();
        return new Point(longitude, latitude);
    }

    private Point parseMultiPoint(JsonElement element) {
        Assert.isTrue(!element.isJsonNull(), "");

        JsonArray array = element.getAsJsonArray().get(0).getAsJsonArray();
        double longitude = array.get(0).getAsJsonPrimitive().getAsDouble();
        double latitude = array.get(1).getAsJsonPrimitive().getAsDouble();
        return new Point(longitude, latitude);
    }

    private Point parseMultiPolygon(JsonElement element) {
        Assert.isTrue(!element.isJsonNull(), "");

        JsonArray array = element.getAsJsonArray().get(0).getAsJsonArray().get(0).getAsJsonArray();
        return getCenteredPoint(array);
    }

    private Point parsePolygon(JsonElement element) {
        Assert.isTrue(!element.isJsonNull(), "");

        JsonArray array = element.getAsJsonArray().get(0).getAsJsonArray();
        return getCenteredPoint(array);
    }

    private Point getCenteredPoint(JsonArray array) {
        List<Double> longs = new ArrayList<>(array.size());
        List<Double> lats = new ArrayList<>(array.size());
        for (JsonElement coordinate : array) {
            JsonArray jsonArray = coordinate.getAsJsonArray();
            longs.add(jsonArray.get(0).getAsJsonPrimitive().getAsDouble());
            lats.add(jsonArray.get(1).getAsJsonPrimitive().getAsDouble());
        }

        double avgLongitude = avg(longs);
        double avgLatitude = avg(lats);

        return new Point(avgLongitude, avgLatitude);
    }

    private double avg(List<Double> list) {
        return list.stream()
                .mapToDouble(value -> value)
                .average()
                .getAsDouble();
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
