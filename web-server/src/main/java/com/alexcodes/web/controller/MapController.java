package com.alexcodes.web.controller;

import com.alexcodes.web.dto.MapDTO;
import com.alexcodes.web.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapController {

    private final MapService mapService;

    @Autowired
    public MapController(MapService mapService) {
        Assert.notNull(mapService, "Cannot be null");
        this.mapService = mapService;
    }

    @RequestMapping(value = "/coordinates",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MapDTO getCoordinates() {
        return mapService.getMap();
    }
}
