package com.alexcodes.twitter.logic;

import com.alexcodes.common.dao.GeoPostRepository;
import com.alexcodes.common.domain.GeoPost;
import com.alexcodes.common.logic.PostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TweetProcessor implements PostProcessor {
    private static final Logger log = LoggerFactory.getLogger(TweetProcessor.class);

    private final TweetConverter tweetConverter;
    private final GeoPostRepository geoPostRepository;

    @Autowired
    public TweetProcessor(
            TweetConverter tweetConverter,
            GeoPostRepository geoPostRepository) {
        this.tweetConverter = tweetConverter;
        this.geoPostRepository = geoPostRepository;
    }

    @Override
    public void process(String message) {
        try {
            GeoPost post = tweetConverter.convert(message);
            log.debug("Tweet: {}", post);
            geoPostRepository.save(post);
        } catch (RuntimeException e) {
            log.error("Exception during tweet processing:", e);
        }
    }
}
