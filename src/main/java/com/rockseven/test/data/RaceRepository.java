package com.rockseven.test.data;

import com.rockseven.test.data.model.RaceData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RaceRepository extends MongoRepository<RaceData, String> {

  List<RaceData> findByRaceUrl(String raceUrl);

}