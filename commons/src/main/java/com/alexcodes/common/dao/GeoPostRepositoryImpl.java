package com.alexcodes.common.dao;

import com.alexcodes.common.domain.GeoPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class GeoPostRepositoryImpl implements GeoPostRepositoryCustom {

    @Value("${repository.geopost.limit:1000}")
    private int limit;

    private final MongoOperations mongoOperations;

    @Autowired
    public GeoPostRepositoryImpl(MongoOperations mongoOperations) {
        Assert.notNull(mongoOperations, "");
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<GeoPost> findBetweenTimestamps(Instant dateFrom, Instant dateTo) {
        Query query = new Query(where("timestamp").gte(dateFrom).lte(dateTo)
                .and("location").ne(null)
                .and("location.latitude").ne(55.7547875)
                .and("location.longitude").ne(37.427642500000005));
        query.with(new Sort(Sort.Direction.DESC, "timestamp"));
        query.limit(limit);

        return mongoOperations.find(query, GeoPost.class);
    }
}
