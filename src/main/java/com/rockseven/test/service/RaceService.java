package com.rockseven.test.service;

import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface RaceService {

    void writeFileDataToDatabase(String filepath) throws IOException;

    RaceData getRaceDataByName(String name);

    List<TeamsItem> getFilteredTeamDataByTimeMoment(String timeMoment, RaceData raceData);
}
