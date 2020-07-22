package com.rockseven.test.stachelag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.data.CustomerRepository;
import com.rockseven.test.data.RaceRepository;
import com.rockseven.test.data.model.Customer;

import com.rockseven.test.data.model.RaceData;
import com.rockseven.test.data.model.TeamsItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.rockseven.test.data")
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

        RaceData raceData = objectMapper.readValue(new File("src/main/resources/inputData/positions-edit.json"), RaceData.class);
        repository.save(raceData);

        for (RaceData raceDataEntry : repository.findByRaceUrl("arc2017")) {
            System.out.println("Race Data Entry for arc2017:");
            System.out.println(raceDataEntry);
            System.out.println("Number of teams: " + raceDataEntry.getTeams().size());
        }
    }
}
