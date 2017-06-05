package com.alexcodes.web.service;

import com.alexcodes.common.dao.OpenDataObjectRepository;
import com.alexcodes.common.domain.OpenDataObject;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class SimpleOpenDataService implements OpenDataService {

    private final OpenDataObjectRepository openDataObjectRepository;

    public SimpleOpenDataService(OpenDataObjectRepository openDataObjectRepository) {
        Assert.notNull(openDataObjectRepository, "");
        this.openDataObjectRepository = openDataObjectRepository;
    }

    @Override
    public List<OpenDataObject> findLeisureObjects() {
        return openDataObjectRepository.findAllByType(OpenDataObject.Type.LEISURE);
    }

    @Override
    public List<OpenDataObject> findCultureObjects() {
        return openDataObjectRepository.findAllByType(OpenDataObject.Type.CULTURE);
    }

    @Override
    public List<OpenDataObject> findCateringObjects() {
        return openDataObjectRepository.findAllByType(OpenDataObject.Type.CATERING);
    }
}
