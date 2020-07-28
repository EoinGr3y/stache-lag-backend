package com.rockseven.test.stachelag.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockseven.test.exception.InvalidDataException;
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
import java.util.Map;

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
    public void whenValidTimeMomentAndTeamName_thenGetTeamsWithinFiveKilometers() throws InvalidDataException {
        List<TeamsItem> teamsWithinFiveKilometers = this.raceService.getTeamsWithinFiveKilometersAtMoment(this.raceData.getTeams(), "2017-11-19T19:00:00Z", "Vahine");
        assertEquals(2, teamsWithinFiveKilometers.size());
    }

    @Test(expected = InvalidDataException.class)
    public void whenInvalidTeamName_thenInvalidDataException() throws InvalidDataException {
        this.raceService.getTeamsWithinFiveKilometersAtMoment(this.raceData.getTeams(), "2017-11-21T08:00:04Z", "fakeTeam");
    }

    @Test
    public void whenValidDay_thenGetAverageSightings() throws InvalidDataException {
        Map<String, List<TeamsItem>> averageNumberOfSightingsPerDay = this.raceService.getAverageNumberOfSightingsPerDay(this.raceData.getTeams(), "2017-11-19");
        assertEquals(5, averageNumberOfSightingsPerDay.size());
        assertEquals(4, averageNumberOfSightingsPerDay.get("Clare-3192").size());
    }
}
