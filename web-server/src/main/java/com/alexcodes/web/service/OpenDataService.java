package com.alexcodes.web.service;

import com.alexcodes.common.domain.OpenDataObject;

import java.util.List;

public interface OpenDataService {
    List<OpenDataObject> findLeisureObjects();
    List<OpenDataObject> findCultureObjects();
    List<OpenDataObject> findCateringObjects();
}
