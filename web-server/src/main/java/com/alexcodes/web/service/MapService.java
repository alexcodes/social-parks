package com.alexcodes.web.service;

import com.alexcodes.common.logic.TimeService;
import com.alexcodes.web.dto.MapDTO;
import com.alexcodes.web.dto.OpenDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;

@Service
public class MapService {

    private final CoordinatesService coordinatesService;
    private final OpenDataService openDataService;
    private final TimeService timeService;

    @Autowired
    public MapService(
            CoordinatesService coordinatesService,
            OpenDataService openDataService,
            TimeService timeService) {
        Assert.notNull(coordinatesService, "Cannot be null");
        Assert.notNull(openDataService, "");
        Assert.notNull(timeService, "");

        this.coordinatesService = coordinatesService;
        this.openDataService = openDataService;
        this.timeService = timeService;
    }

    public MapDTO getMap() {
        MapDTO dto = new MapDTO();

        Instant dateTo = timeService.now();
        Instant dateFrom = timeService.lowerBound(dateTo);
        dto.coordinates = coordinatesService.findCoordinates(dateFrom, dateTo);

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
