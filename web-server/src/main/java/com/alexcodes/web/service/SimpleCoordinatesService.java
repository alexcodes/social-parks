package com.alexcodes.web.service;

import com.alexcodes.common.dao.GeoPostRepository;
import com.alexcodes.common.domain.GeoPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Profile("default")
public class SimpleCoordinatesService implements CoordinatesService {

    private final GeoPostRepository geoPostRepository;

    @Autowired
    public SimpleCoordinatesService(GeoPostRepository geoPostRepository) {
        this.geoPostRepository = geoPostRepository;
    }

    @Override
    public List<List<Double>> findCoordinates(Instant dateFrom, Instant dateTo) {
        List<GeoPost> posts = geoPostRepository.findBetweenTimestamps(dateFrom, dateTo);

        return posts.stream()
                .map(post -> post.location)
                .filter(Objects::nonNull)
                .map(location -> Arrays.asList(location.latitude, location.longitude))
                .collect(Collectors.toList());
    }
}
