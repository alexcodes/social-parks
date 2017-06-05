package com.alexcodes.opendata.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class LoaderService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(LoaderService.class);

    private static final String DATASETS = "datasets";
    private static final String ROWS = "rows";
    private static final String COUNT = "count";

    private static final String GLOBAL_ID = "global_id";
    private static final String COMMON_NAME = "CommonName";
    private static final String GEO_DATA = "geoData";

    @Value("${open-data-loader.scheme}")
    private String scheme;

    @Value("${open-data-loader.host}")
    private String host;

    @Value("${open-data-loader.api.version}")
    private String apiVersion;

    @Value("${open-data-loader.api.rowLimit}")
    private int rowLimit;

    private final RestTemplate restTemplate;

    @Autowired
    public LoaderService(RestTemplate restTemplate) {
        Assert.notNull(restTemplate, "RestTemplate cannot be null");
        this.restTemplate = restTemplate;
    }

    @Override
    public void run(String... strings) throws Exception {
        load("1465");
    }

    private void load(String datasetId) {
        int count = getCount(datasetId);
        log.debug("count = {}", count);

        for (int skip = 0; skip < count; skip += rowLimit) {
            loadData(datasetId, skip);
            log.debug("loaded {}/{}", skip, count);
        }
    }

    private int getCount(String datasetId) {
        UriComponents uriComponents = getUriCount(datasetId);
        log.debug("{}", uriComponents);

        ResponseEntity<Integer> response = restTemplate.getForEntity(uriComponents.toUri(), Integer.class);
        return response.getBody();
    }

    private void loadData(String datasetId, int skip) {
        UriComponents uriComponents = getUriData(datasetId, skip, rowLimit);
        log.debug("{}", uriComponents);

        List<String> requestBody = Arrays.asList(GLOBAL_ID, COMMON_NAME, GEO_DATA);
        ResponseEntity<String> response =
                restTemplate.postForEntity(uriComponents.toUri(), requestBody, String.class);
        log.debug("{}", response.getBody());
    }

    private UriComponents getUriData(String datasetId, int skip, int top) {
        String path = apiVersion + "/" + DATASETS + "/" + datasetId + "/" + ROWS;
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .queryParam("$skip", skip)
                .queryParam("$top", top)
                .build()
                .encode();
    }

    private UriComponents getUriCount(String datasetId) {
        String path = apiVersion + "/" + DATASETS + "/" + datasetId + "/" + COUNT;
        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(host)
                .path(path)
                .build()
                .encode();
    }
}
