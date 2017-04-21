package com.alexcodes.web.service;

import com.alexcodes.web.dto.MapDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MapService {

    private final CoordinatesService coordinatesService;

    @Autowired
    public MapService(CoordinatesService coordinatesService) {
        Assert.notNull(coordinatesService, "Cannot be null");
        this.coordinatesService = coordinatesService;
    }

    public MapDTO getMap() {
        MapDTO dto = new MapDTO();
        dto.coordinates = coordinatesService.findCoordinates();

        return dto;
    }
}
