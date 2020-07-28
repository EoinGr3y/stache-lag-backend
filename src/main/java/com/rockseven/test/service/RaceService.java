package com.rockseven.test.service;

import com.rockseven.test.repository.model.InvalidDataException;
import com.rockseven.test.repository.model.TeamsItem;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface RaceService {

    List<TeamsItem> getFilteredTeamDataByTimeMoment(List<TeamsItem> teamsItems, String moment) throws InvalidDataException;

    List<TeamsItem> getTeamsWithinFiveKilometersAtMoment(List<TeamsItem> teamsFilteredByMoment, String moment, String teamName) throws InvalidDataException;

    Map<String, List<TeamsItem>> getAverageNumberOfSightingsPerDay(List<TeamsItem> teamsItems, String day) throws InvalidDataException;
}
