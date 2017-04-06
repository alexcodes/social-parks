package com.alexcodes.twitter.logic;

import com.alexcodes.common.domain.GeoPost;
import com.alexcodes.common.logic.PostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TweetProcessor implements PostProcessor {
    private static final Logger log = LoggerFactory.getLogger(TweetProcessor.class);

    private final TweetConverter tweetConverter;

    public TweetProcessor(TweetConverter tweetConverter) {
        this.tweetConverter = tweetConverter;
    }

    @Override
    public void process(String message) {
        try {
            GeoPost post = tweetConverter.convert(message);
            log.debug("Tweet: {}", post);
        } catch (RuntimeException e) {
            log.error("Exception during tweet processing:", e);
        }
    }
}
