package com.alexcodes.twitter.logic;

import com.alexcodes.common.domain.GeoPost;
import com.alexcodes.common.domain.SourceType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TweetConverterTest {
    private static final double ZERO_DELTA = 0.0;

    private TweetConverter tweetConverter;

    @Before
    public void init() {
        tweetConverter = new TweetConverter();
    }

    @Test
    public void shouldConvert() {
        // Given
        String message = "{\"text\":\"Hello world!\"," +
                " \"coordinates\":{\"coordinates\":[1.2,3.4]}," +
                " \"created_at\":\"Wed Apr 05 17:28:48 +0000 2017\"}";

        // When
        GeoPost post = tweetConverter.convert(message);

        // Then
        assertEquals(SourceType.TWITTER, post.sourceType);
        assertEquals("Hello world!", post.text);
        assertEquals(1.2, post.location.longitude, ZERO_DELTA);
        assertEquals(3.4, post.location.latitude, ZERO_DELTA);
        assertEquals("2017-04-05T17:28:48Z", post.timestamp.toString());
    }

    @Test
    public void shouldConvertWithNullCoordinates() {
        // Given
        String message = "{\"text\":\"Hello world!\", \"coordinates\":null}";

        // When
        GeoPost post = tweetConverter.convert(message);

        // Then
        assertNull(post.location);
    }

    @Test
    public void shouldConvertWithoutCoordinates() {
        // Given
        String message = "{\"text\":\"Hello world!\"}";

        // When
        GeoPost post = tweetConverter.convert(message);

        // Then
        assertNull(post.location);
    }
}
