package com.alexcodes.opendata.logic;

import com.alexcodes.common.domain.OpenDataObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class OpenDataConverterTest {
    private static final double ZERO_DELTA = 0.0;

    private OpenDataConverter openDataConverter;

    @Before
    public void init() {
        LocationExtractor locationExtractor = new LocationExtractor();
        openDataConverter = new OpenDataConverter(locationExtractor);
    }

    @Test
    public void shouldConvert() {
        // Given
        String message = "[{\"global_id\":4331737,\"Number\":1,\"Cells\":{\"global_id\":4331737,\"CommonName\":\"ШО 2 Парк Усадьба Трубецких в Хамовниках\",\"geoData\":{\"type\":\"Polygon\",\"coordinates\":[[[37.0,55.0],[37.2,55.2]]]}}}]";

        // When
        List<OpenDataObject> result = openDataConverter.convert(message, OpenDataObject.Type.LEISURE);

        // Then
        assertEquals(1, result.size());

        OpenDataObject object = result.get(0);
        assertEquals(4331737, object.id.intValue());
        assertEquals("ШО 2 Парк Усадьба Трубецких в Хамовниках", object.name);
        assertEquals(37.1, object.location.longitude, ZERO_DELTA);
        assertEquals(55.1, object.location.latitude, ZERO_DELTA);
        assertEquals(OpenDataObject.Type.LEISURE, object.type);
    }
}
