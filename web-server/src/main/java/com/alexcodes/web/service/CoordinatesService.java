package com.alexcodes.web.service;

import java.time.Instant;
import java.util.List;

public interface CoordinatesService {
    List<List<Double>> findCoordinates(Instant dateFrom, Instant dateTo);
}
