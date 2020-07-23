package com.rockseven.test.repository;

import com.rockseven.test.repository.model.RaceData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RaceRepository extends MongoRepository<RaceData, String>, CustomRaceRepository {

  List<RaceData> findByRaceUrl(String raceUrl);

  List<RaceData> getByTeamsName(String name);

  List<RaceData> findByTeams_Positions_GpsAt(String time);
}