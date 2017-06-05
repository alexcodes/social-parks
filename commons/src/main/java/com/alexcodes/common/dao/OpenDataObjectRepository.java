package com.alexcodes.common.dao;

import com.alexcodes.common.domain.OpenDataObject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OpenDataObjectRepository extends CrudRepository<OpenDataObject, Integer> {
    List<OpenDataObject> findAllByType(OpenDataObject.Type type);
}
