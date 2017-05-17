package com.alexcodes.web.service;

import com.alexcodes.web.dto.CoordinateDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Profile("test")
public class DummyCoordinatesService implements CoordinatesService {
    private static final Random random = new Random(0L);

    @Override
    public List<List<Double>> findCoordinates() {
        return getDummy1();
    }

    private List<List<Double>> getTestData() {
        int size = 250;
        double longitude = 37.617861;
        double latitude = 55.751588;
        double delta = 0.1;

        List<CoordinateDTO> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            CoordinateDTO dto = new CoordinateDTO(
                    longitude + delta * Math.sin((double) i / 2),
                    latitude + delta * Math.sin((double) i / 2));
            result.add(dto);
        }

        return result.stream()
                .map(dto -> Arrays.asList(dto.latitude, dto.longitude))
                .collect(Collectors.toList());
    }

    private List<List<Double>> getDummy1() {
        // low hill
        int size = 45;
        double longitude = 37.515326;
        double latitude = 55.734519;
        double k = 0.04;

        List<List<Double>> result = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            result.add(Arrays.asList(
                    latitude + k * Math.pow(random.nextDouble() - 0.5, 3),
                    longitude + 2 * k * Math.pow(random.nextDouble() - 0.5, 3)));
        }

        return result;
    }
}
