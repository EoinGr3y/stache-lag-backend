package com.rockseven.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.repository.RaceRepository;
import com.rockseven.test.repository.model.PositionsItem;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RaceServiceImpl implements RaceService {

    @Autowired
    private RaceRepository repository;

    @Override
    public void writeFileDataToDatabase(String filepath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        repository.deleteAll();

        RaceData raceData = objectMapper.readValue(new File(filepath), RaceData.class);
        repository.save(raceData);
    }

    @Override
    public RaceData getRaceDataByName(String name) {
        return repository.findByRaceUrl(name).get(0);
    }

    @Override
    public List<TeamsItem> getFilteredTeamDataByTimeMoment(String timeMoment, RaceData raceData) {
        return raceData.getTeams().stream()
                .filter(teamsItem -> teamsItem.getPositions().stream()
                        .anyMatch(positionsItem -> positionsItem.getGpsAt().equals(timeMoment)))
                .peek(teamsItem -> {
                    List<PositionsItem> filteredPositionItems = teamsItem.getPositions().stream()
                            .filter(positionsItem -> positionsItem.getGpsAt().equals(timeMoment)).collect(Collectors.toList());
                    teamsItem.setPositions(filteredPositionItems);
                })
                .collect(Collectors.toList());
    }
}
