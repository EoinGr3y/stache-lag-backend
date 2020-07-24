package com.rockseven.test.stachelag.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.controller.InvalidDataException;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import com.rockseven.test.service.RaceService;
import com.rockseven.test.service.RaceServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class RaceServiceImplTest {

    @TestConfiguration
    static class RaceServiceImplTestContextConfiguration {

        @Bean
        public RaceService employeeService() {
            return new RaceServiceImpl();
        }
    }

    @Autowired
    private RaceService raceService;

    private RaceData raceData;

    @Before
    public void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.raceData = objectMapper.readValue(new File("src/test/resources/inputData/positions-test.json"), RaceData.class);
    }

    @Test
    public void whenValidTimeMoment_thenFilterTeamDataByTimeMoment() throws InvalidDataException {
        List<TeamsItem> filteredTeams = this.raceService.getFilteredTeamDataByTimeMoment("2017-11-21T08:00:04Z", this.raceData);
        assertEquals(2, filteredTeams.size());
    }

    @Test(expected = InvalidDataException.class)
    public void whenInvalidMoment_thenInvalidDataException() throws InvalidDataException {
        this.raceService.getFilteredTeamDataByTimeMoment("incorrectTimeMoment", this.raceData);
    }

    @Test
    public void whenValidTimeMomentAndTeamName_thenFindTeamsWithinFiveKilometers() throws InvalidDataException {
        List<TeamsItem> filteredTeams = this.raceService.getFilteredTeamDataByTimeMoment("2017-11-21T08:00:04Z", this.raceData);
        List<TeamsItem> teamsWithinFiveKilometers = this.raceService.getFilteredTeamWithinFiveKilometers(filteredTeams, "Infinity");
        assertEquals(1, teamsWithinFiveKilometers.size());
    }

    @Test(expected = InvalidDataException.class)
    public void whenInvalidTeamName_thenInvalidDataException() throws InvalidDataException {
        List<TeamsItem> filteredTeams = this.raceService.getFilteredTeamDataByTimeMoment("2017-11-21T08:00:04Z", this.raceData);
        this.raceService.getFilteredTeamWithinFiveKilometers(filteredTeams, "fakeTeam");
    }
}
