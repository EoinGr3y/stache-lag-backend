package com.rockseven.test.data;

import com.rockseven.test.data.model.RaceData;
import com.rockseven.test.data.model.TeamsItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RaceRepository extends MongoRepository<RaceData, String> {

  TeamsItem findByRaceUrl(String raceUrl);

}