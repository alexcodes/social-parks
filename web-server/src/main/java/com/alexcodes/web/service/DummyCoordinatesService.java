package com.alexcodes.web.service;

import com.alexcodes.web.dto.CoordinateDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DummyCoordinatesService implements CoordinatesService {
    private static final int COORDINATES_SIZE = 250;
    private static final double LONGITUDE = 37.617861;
    private static final double LATITUDE = 55.751588;
    private static final double DELTA = 0.1;

    @Override
    public List<List<Double>> findCoordinates() {
        List<CoordinateDTO> result = new ArrayList<>(COORDINATES_SIZE);

        for (int i = 0; i < COORDINATES_SIZE; i++) {
            CoordinateDTO dto = new CoordinateDTO(getLongitude(i), getLatitude(i));
            result.add(dto);
        }

        return result.stream()
                .map(dto -> Arrays.asList(dto.latitude, dto.longitude))
                .collect(Collectors.toList());
    }

    private double getLongitude(int i) {
        return LONGITUDE + DELTA * Math.sin((double) i / 2);
    }

    private double getLatitude(int i) {
        return LATITUDE + DELTA * Math.sin((double) i / 2);
    }
}
