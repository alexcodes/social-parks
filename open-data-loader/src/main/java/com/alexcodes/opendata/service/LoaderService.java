package com.alexcodes.opendata.service;

import com.alexcodes.common.dao.OpenDataObjectRepository;
import com.alexcodes.common.domain.OpenDataObject;
import com.alexcodes.opendata.logic.OpenDataConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    @Value("${open-data-loader.datasetId}")
    private String datasetId;

    @Value("${open-data-loader.type}")
    private OpenDataObject.Type type;

    private final RestTemplate restTemplate;
    private final OpenDataConverter openDataConverter;
    private final OpenDataObjectRepository openDataObjectRepository;

    @Autowired
    public LoaderService(
            RestTemplate restTemplate,
            OpenDataConverter openDataConverter,
            OpenDataObjectRepository openDataObjectRepository) {
        Assert.notNull(restTemplate, "RestTemplate cannot be null");
        Assert.notNull(openDataConverter, "OpenDataConverter cannot be null");
        Assert.notNull(openDataObjectRepository, "OpenDataObjectRepository cannot be null");

        this.restTemplate = restTemplate;
        this.openDataConverter = openDataConverter;
        this.openDataObjectRepository = openDataObjectRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Assert.isTrue(!StringUtils.isEmpty(datasetId), "DatasetId must be defined");
        Assert.notNull(type, "Type must be defined");

        List<OpenDataObject> objects = load(datasetId, type);
        openDataObjectRepository.save(objects);
        log.debug("Saved {} open data objects [{}]", objects.size(), type);
    }

    private List<OpenDataObject> load(String datasetId, OpenDataObject.Type type) {
        log.debug("Loading open data set #{}...", datasetId);
        int count = getCount(datasetId);
        log.debug("Rows: {}", count);
        if (count == 0) return Collections.emptyList();

        List<OpenDataObject> result = new ArrayList<>(count);

        for (int skip = 0; skip < count; skip += rowLimit) {
            log.debug("loaded {}/{}", skip, count);
            List<OpenDataObject> list = loadPart(datasetId, type, skip);
            result.addAll(list);
        }

        return result;
    }

    private int getCount(String datasetId) {
        UriComponents uriComponents = getUriCount(datasetId);
        log.debug("{}", uriComponents);

        ResponseEntity<Integer> response = restTemplate.getForEntity(uriComponents.toUri(), Integer.class);
        return response.getBody();
    }

    private List<OpenDataObject> loadPart(String datasetId, OpenDataObject.Type type, int skip) {
        UriComponents uriComponents = getUriData(datasetId, skip, rowLimit);

        List<String> requestBody = Arrays.asList(GLOBAL_ID, COMMON_NAME, GEO_DATA);
        ResponseEntity<String> response =
                restTemplate.postForEntity(uriComponents.toUri(), requestBody, String.class);
        String message = response.getBody();
        return openDataConverter.convert(message, type);
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
