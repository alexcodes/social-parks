package com.alexcodes.web.service;

import com.alexcodes.common.dao.GeoPostRepository;
import com.alexcodes.common.domain.GeoPost;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

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
    public List<List<Double>> findCoordinates() {
        List<GeoPost> posts = Lists.newArrayList(geoPostRepository.findAll());

        return posts.stream()
                .map(post -> post.location)
                .filter(Objects::nonNull)
                .filter(location -> location.latitude != 55.7547875 && location.longitude != 37.427642500000005)
                .map(location -> Arrays.asList(location.latitude, location.longitude))
                .collect(Collectors.toList());
    }
}
