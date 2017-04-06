package com.alexcodes.twitter.logic;

import com.alexcodes.common.domain.GeoPost;
import com.alexcodes.common.domain.SourceType;
import com.alexcodes.common.logic.PostConverter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
public class TweetConverter implements PostConverter {
    private static final String TEXT = "text";
    private static final String CREATED_AT = "created_at";

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("E MMM dd HH:mm:ss Z yyyy");

    private final LocationExtractor locationExtractor;

    @Autowired
    public TweetConverter(LocationExtractor locationExtractor) {
        this.locationExtractor = locationExtractor;
    }

    @Override
    public GeoPost convert(String message) {
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(message).getAsJsonObject();

        GeoPost post = new GeoPost();
        post.sourceType = SourceType.TWITTER;
        post.text = getText(root);
        post.location = locationExtractor.getLocation(root);
        post.timestamp = getTimestamp(root);
        return post;
    }

    private String getText(JsonObject root) {
        return root.getAsJsonPrimitive(TEXT).getAsString();
    }

    private Instant getTimestamp(JsonObject root) {
        JsonElement createdAt = root.get(CREATED_AT);
        if (createdAt == null) return null;

        String timestamp = createdAt.getAsString();
        return formatter.parse(timestamp).query(Instant::from);
    }
}
