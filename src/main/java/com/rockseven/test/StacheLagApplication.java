package com.rockseven.test;

import com.rockseven.test.repository.model.TeamsItem;
import com.rockseven.test.service.RaceService;

import com.rockseven.test.repository.model.RaceData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@Slf4j
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rockseven.test")
public class StacheLagApplication implements CommandLineRunner {

    @Autowired
    private RaceService raceService;

    public static void main(String[] args) {
        SpringApplication.run(StacheLagApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        raceService.writeFileDataToDatabase("src/main/resources/inputData/positions.json");
        RaceData raceData = raceService.getRaceDataByName("arc2017");

        log.debug("Race Data Entry for arc2017: {}", raceData);
        log.info("Number of teams: {}", raceData.getTeams().size());

        List<TeamsItem> filteredTeams = raceService.getFilteredTeamDataByTimeMoment("2017-12-07T12:00:00Z", raceData);

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
