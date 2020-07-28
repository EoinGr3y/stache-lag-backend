package com.rockseven.test.controller;

import com.rockseven.test.repository.RaceRepository;
import com.rockseven.test.exception.InvalidDataException;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import com.rockseven.test.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class RaceControllerUI {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RaceService raceService;

    @GetMapping("/visibleVesselsForMomentSummary")
    public String getVisibleVesselsForMoment(@RequestParam(value = "teamName") String teamName,
                                             @RequestParam(value = "moment") String moment, Model model) throws InvalidDataException {
        if(moment.length() < 20) {
            throw new InvalidDataException("Invalid moment supplied, must be of format: YYYY-MM-DDTHH:mm:ssZ");
        }
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        List<TeamsItem> visibleTeamsAtMoment = raceService.getTeamsWithinFiveKilometersAtMoment(raceData.getTeams(), moment, teamName);
        model.addAttribute("teamName", teamName);
        model.addAttribute("moment", moment);
        model.addAttribute("visibleTeams", visibleTeamsAtMoment);
        return "visibleTeamsForMoment";
    }

    @GetMapping("/averageVesselsPerDaySummary")
    public String getAverageSightingsPerDay(@RequestParam(value = "day") String day, Model model) throws InvalidDataException {
        if(day.length() < 10) {
            throw new InvalidDataException("Invalid day supplied, must be of format: YYYY-MM-DD");
        }
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        Map<String, List<TeamsItem>> averageSightingsPerDay = raceService.getAverageNumberOfSightingsPerDay(raceData.getTeams(), day);
        float totalSightings = 0;
        for(List<TeamsItem> sightings: averageSightingsPerDay.values()) {
            totalSightings += sightings.size();
        }
        model.addAttribute("day", day);
        model.addAttribute("averageSightingsPerDay", averageSightingsPerDay);
        model.addAttribute("totalAverage", Math.round(totalSightings / averageSightingsPerDay.size()));
        return "averageSightingsPerDay";
    }
}
