package com.rockseven.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.repository.RaceRepository;
import com.rockseven.test.repository.model.Greeting;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import com.rockseven.test.service.RaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RaceService raceService;

    @GetMapping("/loadFile")
    public String loadFile() throws IOException {
        this.raceRepository.deleteAll();
        ObjectMapper objectMapper = new ObjectMapper();
        RaceData raceData = objectMapper.readValue(new File("src/main/resources/inputData/positions.json"), RaceData.class);
        this.raceRepository.save(raceData);
        return "File loaded successfully";
    }

    @GetMapping("/visibleVesselsForMoment")
    public List<TeamsItem> getVisibleVesselsForMoment(@RequestParam(value = "teamName")String teamName, @RequestParam(value = "moment")String moment) throws InvalidDataException {
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        List<TeamsItem> teamsFilteredByMoment = raceService.getFilteredTeamDataByTimeMoment(moment, raceData);
        log.info("Team data filtered by moment: {}", teamsFilteredByMoment);
        return raceService.getFilteredTeamWithinFiveKilometers(teamsFilteredByMoment, teamName);
    }
}
