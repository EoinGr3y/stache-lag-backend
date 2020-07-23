package com.rockseven.test.repository;

import com.rockseven.test.repository.model.TeamsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomRaceRepositoryImpl implements CustomRaceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<TeamsItem> customGetItems() {
        return null;
    }
}
