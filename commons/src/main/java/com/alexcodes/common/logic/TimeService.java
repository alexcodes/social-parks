package com.alexcodes.common.logic;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TimeService {

    @Value("${map.lastHours:4}")
    private int lastHours;

    public Instant now() {
        return Instant.now();
    }

    public Instant lowerBound(Instant date) {
        return date.minus(lastHours, ChronoUnit.HOURS);
    }
}
