package com.alexcodes.web.service;

import com.alexcodes.web.dto.MapDTO;
import com.alexcodes.web.dto.OpenDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MapService {

    private final CoordinatesService coordinatesService;
    private final OpenDataService openDataService;

    @Autowired
    public MapService(
            CoordinatesService coordinatesService,
            OpenDataService openDataService) {
        Assert.notNull(coordinatesService, "Cannot be null");
        Assert.notNull(openDataService, "");

        this.coordinatesService = coordinatesService;
        this.openDataService = openDataService;
    }

    public MapDTO getMap() {
        MapDTO dto = new MapDTO();
        dto.coordinates = coordinatesService.findCoordinates();

        return dto;
    }

    public OpenDataDTO getOpenDataObjects() {
        OpenDataDTO dto = new OpenDataDTO();
        dto.leisure = openDataService.findLeisureObjects();
        dto.culture = openDataService.findCultureObjects();
        dto.catering = openDataService.findCateringObjects();
        return dto;
    }
}
