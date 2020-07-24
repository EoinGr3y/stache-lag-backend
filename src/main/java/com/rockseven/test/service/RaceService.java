package com.rockseven.test.service;

import com.rockseven.test.controller.InvalidDataException;
import com.rockseven.test.repository.model.RaceData;
import com.rockseven.test.repository.model.TeamsItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RaceService {

    List<TeamsItem> getFilteredTeamDataByTimeMoment(String timeMoment, RaceData raceData) throws InvalidDataException;

    List<TeamsItem> getFilteredTeamWithinFiveKilometers(List<TeamsItem> teamsFilteredByMoment, String teamName) throws InvalidDataException;

}
