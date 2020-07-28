package com.rockseven.test.controller;

import com.rockseven.test.repository.RaceRepository;
import com.rockseven.test.repository.model.InvalidDataException;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import com.rockseven.test.service.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class UiController {

    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private RaceService raceService;

    @GetMapping("/visibleVesselsForMomentUi")
    public String getVisibleVesselsForMoment(@RequestParam(value = "teamName") String teamName,
                                             @RequestParam(value = "moment") String moment, Model model) throws InvalidDataException {
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        List<TeamsItem> visibleTeamsAtMoment = raceService.getTeamsWithinFiveKilometersAtMoment(raceData.getTeams(), moment, teamName);
        model.addAttribute("teamName", teamName);
        model.addAttribute("moment", moment);
        model.addAttribute("visibleTeams", visibleTeamsAtMoment);
        return "visibleTeamsAtMoment";
    }

    @GetMapping("/getAverageSightingsPerDayUi")
    public Map<String, List<TeamsItem>> getAverageSightingsPerDay(@RequestParam(value = "moment") String day) throws InvalidDataException {
        RaceData raceData = raceRepository.findByRaceUrl("arc2017").get(0);
        return raceService.getAverageNumberOfSightingsPerDay(raceData.getTeams(), day);
    }
}
