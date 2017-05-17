package com.alexcodes.twitter.service;

import com.alexcodes.twitter.logic.TweetProcessor;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TwitterListener implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(TwitterListener.class);

    private static final int QUEUE_SIZE = 1000;

    @Value("${twitter.consumerKey}")
    private String consumerKey;

    @Value("${twitter.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter.token}")
    private String token;

    @Value("${twitter.tokenSecret}")
    private String tokenSecret;

    @Value("${twitter.client.name}")
    private String clientName;

    private final Location location;
    private final TweetProcessor tweetProcessor;

    @Autowired
    public TwitterListener(Location location, TweetProcessor tweetProcessor) {
        Assert.notNull(location, "Location cannot be null");
        Assert.notNull(tweetProcessor, "Cannot be null");

        this.location = location;
        this.tweetProcessor = tweetProcessor;
    }

    public void run(String... strings) throws Exception {
        // Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

        // Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth)
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        // Optional: set up some followings and track terms
        List<Location> locations = Collections.singletonList(location);
        hosebirdEndpoint.locations(locations);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name(clientName)
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);

        Client hosebirdClient = builder.build();
        hosebirdClient.connect();

        int i = 0;
        long time = System.currentTimeMillis();
        // on a different thread, or multiple different threads....
        while (!hosebirdClient.isDone()) {
            String message = msgQueue.take();
//            log.debug("Receive: {}", message);
            tweetProcessor.process(message);

            i++;

//            if (System.currentTimeMillis() - time > 60_000L) break;
        }
        log.info("{} TPS", i);

        hosebirdClient.stop();
    }
}
