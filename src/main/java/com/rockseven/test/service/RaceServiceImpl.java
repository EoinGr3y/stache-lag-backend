package com.rockseven.test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.repository.RaceRepository;
import com.rockseven.test.repository.model.PositionsItem;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.info("Getting filtered team data for moment: {}", timeMoment);
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

    @Override
    public List<TeamsItem> getFilteredTeamWithinFiveKilometers(List<TeamsItem> teamsFilteredByMoment, String teamName) {
        log.info("Getting teams within 5 kilometers of team: {}", teamName);
        TeamsItem currentTeam = teamsFilteredByMoment.stream().filter(teamsItem -> teamsItem.getName().equals(teamName)).findFirst().orElse(null);
        log.info("Current team: {}", currentTeam);
        List<TeamsItem> teamsWithinFiveKilometers = teamsFilteredByMoment.stream().filter(teamsItem -> {
            double distanceFromCurrentVessel = calculateDistance(currentTeam.getPositions().get(0).getLatitude(), currentTeam.getPositions().get(0).getLongitude(),
                    teamsItem.getPositions().get(0).getLatitude(), teamsItem.getPositions().get(0).getLongitude());
            return ((distanceFromCurrentVessel > 0) && (distanceFromCurrentVessel <= 5.0));
        }).collect(Collectors.toList());
        log.info("Teams within 5 kilometers: {}", teamsWithinFiveKilometers);
        return teamsWithinFiveKilometers;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return dist;
        }
    }
}
