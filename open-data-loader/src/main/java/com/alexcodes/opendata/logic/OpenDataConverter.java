package com.alexcodes.opendata.logic;

import com.alexcodes.common.domain.OpenDataObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenDataConverter {
    private static final Logger log = LoggerFactory.getLogger(OpenDataConverter.class);

    private static final String CELLS = "Cells";
    private static final String GLOBAL_ID = "global_id";
    private static final String COMMON_NAME = "CommonName";
    private static final String GEO_DATA = "geoData";

    private final LocationExtractor locationExtractor;

    @Autowired
    public OpenDataConverter(LocationExtractor locationExtractor) {
        Assert.notNull(locationExtractor, "LocationExtractor cannot be null");
        this.locationExtractor = locationExtractor;
    }

    public List<OpenDataObject> convert(String message, OpenDataObject.Type type) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(message).getAsJsonArray();

        List<OpenDataObject> result = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject root = jsonArray.get(i).getAsJsonObject();
            OpenDataObject openDataObject = convert(root);
            openDataObject.type = type;
            result.add(openDataObject);
        }

        return result;
    }

    private OpenDataObject convert(JsonObject root) {
        JsonObject object = root.getAsJsonObject(CELLS);

        OpenDataObject openDataObject = new OpenDataObject();
        openDataObject.id = getId(object.get(GLOBAL_ID));
        openDataObject.name = getName(object.get(COMMON_NAME));
        openDataObject.location = locationExtractor.parse(object.getAsJsonObject(GEO_DATA));

        return openDataObject;
    }

    private Integer getId(JsonElement element) {
        Assert.isTrue(element.isJsonPrimitive(), "");
        return element.getAsJsonPrimitive().getAsInt();
    }

    private String getName(JsonElement element) {
        Assert.isTrue(element.isJsonPrimitive(), "");
        return element.getAsJsonPrimitive().getAsString();
    }
}
