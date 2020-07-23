package com.rockseven.test.stachelag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.repository.RaceRepository;

import com.rockseven.test.repository.model.PositionsItem;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rockseven.test")
public class StacheLagApplication implements CommandLineRunner {

    @Autowired
    private RaceRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(StacheLagApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        repository.deleteAll();

        RaceData raceData = objectMapper.readValue(new File("src/main/resources/inputData/positions.json"), RaceData.class);
        repository.save(raceData);

        RaceData raceDataEntry = repository.findByRaceUrl("arc2017").get(0);
        log.info("Race Data Entry for arc2017: {}", raceDataEntry);
        log.info("Number of teams: {}", raceDataEntry.getTeams().size());

        List<TeamsItem> filteredTeams = raceDataEntry.getTeams().stream()
                .filter(teamsItem -> teamsItem.getPositions().stream()
                        .anyMatch(positionsItem -> positionsItem.getGpsAt().equals("2017-12-07T12:00:00Z")))
                .peek(teamsItem -> {
                    List<PositionsItem> filteredPositionItems = teamsItem.getPositions().stream()
                            .filter(positionsItem -> positionsItem.getGpsAt().equals("2017-12-07T12:00:00Z")).collect(Collectors.toList());
                    teamsItem.setPositions(filteredPositionItems);
                })
                .collect(Collectors.toList());

//        for (TeamsItem team: filteredTeams) {
//            team.setPositions(team.getPositions().stream().filter(positionsItem -> positionsItem.getGpsAt().equals("2017-12-07T12:00:00Z")).collect(Collectors.toList()));
//        }

        log.info("Number of filtered Teams: {}", filteredTeams.size());
        log.info("Number of filtered Teams positions: {}", filteredTeams.get(0).getPositions().size());
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
