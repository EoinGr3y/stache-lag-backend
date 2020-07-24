package com.rockseven.test.controller;

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

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RaceService raceService;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/visibleVesselsForMoment")
    public List<TeamsItem> getVisibleVesselsForMoment(@RequestParam(value = "teamName")String teamName, @RequestParam(value = "moment")String moment) throws InvalidDataException {
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        List<TeamsItem> teamsFilteredByMoment = raceService.getFilteredTeamDataByTimeMoment(moment, raceData);
        log.info("Team data filtered by moment: {}", teamsFilteredByMoment);
        if(teamsFilteredByMoment.stream().noneMatch(teamsItem -> teamsItem.getName().equals(teamName))) {
            throw new InvalidDataException("No data for team name and moment selected");
        }
        List<TeamsItem> teamsWithinFiveKilometers = raceService.getFilteredTeamWithinFiveKilometers(teamsFilteredByMoment, teamName);
        return teamsWithinFiveKilometers;
    }
}
