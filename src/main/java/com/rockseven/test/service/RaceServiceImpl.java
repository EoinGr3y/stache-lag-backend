package com.rockseven.test.service;

import com.rockseven.test.controller.InvalidDataException;
import com.rockseven.test.repository.model.PositionsItem;
import com.rockseven.test.repository.model.TeamsItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RaceServiceImpl implements RaceService {

    @Override
    public List<TeamsItem> getFilteredTeamDataByTimeMoment(List<TeamsItem> teamsItems, String moment) throws InvalidDataException {
        log.info("Getting filtered team data for moment: {}", moment);
        List<TeamsItem> filteredTeams = teamsItems.stream()
                .filter(teamsItem -> teamsItem.getPositions().stream()
                        .anyMatch(positionsItem -> positionsItem.getGpsAt().equals(moment)))
                .peek(teamsItem -> {
                    List<PositionsItem> filteredPositionItems = teamsItem.getPositions().stream()
                            .filter(positionsItem -> positionsItem.getGpsAt().equals(moment)).collect(Collectors.toList());
                    teamsItem.setPositions(filteredPositionItems);
                })
                .collect(Collectors.toList());
        if (!(filteredTeams.size() >= 1)) {
            throw new InvalidDataException("No team data for given moment.");
        }
        return filteredTeams;
    }

    @Override
    public List<TeamsItem> getTeamsWithinFiveKilometersAtMoment(List<TeamsItem> teamsItems, String moment, String teamName) throws InvalidDataException {
        log.info("Getting teams within 5 kilometers of team: {}", teamName);
        List<TeamsItem> teamsFilteredOnMoment = getFilteredTeamDataByTimeMoment(teamsItems, moment);
        if (teamsFilteredOnMoment.stream().noneMatch(teamsItem -> teamsItem.getName().equals(teamName))) {
            throw new InvalidDataException("No data for team name and moment selected");
        }
        TeamsItem currentTeam = teamsItems.stream().filter(teamsItem -> teamsItem.getName().equals(teamName)).findFirst().orElse(null);
        log.info("Current team: {}", currentTeam);
        List<TeamsItem> teamsWithinFiveKilometers = teamsItems.stream().filter(teamsItem -> {
            double distanceFromCurrentVessel = calculateDistance(currentTeam.getPositions().get(0).getLatitude(), currentTeam.getPositions().get(0).getLongitude(),
                    teamsItem.getPositions().get(0).getLatitude(), teamsItem.getPositions().get(0).getLongitude());
            return ((distanceFromCurrentVessel > 0) && (distanceFromCurrentVessel <= 5.0));
        }).collect(Collectors.toList());
        log.info("Teams within 5 kilometers: {}", teamsWithinFiveKilometers);
        return teamsWithinFiveKilometers;
    }

    @Override
    public Map<String, List<TeamsItem>> getAverageNumberOfSightingsPerDay(List<TeamsItem> teamsItems, String day) throws InvalidDataException {
        log.info("Getting average number of sightings on day: {}", day);
        List<TeamsItem> teamsFilteredOnDay = getFilteredTeamDataByDay(teamsItems, day);
        Map<String, List<TeamsItem>> numberOfSightingsPerTeam = teamsFilteredOnDay.stream()
                .peek(t -> log.info("Team name: {}", t.getName()))
                .collect(Collectors.toMap(teamsItem -> teamsItem.getName() + "-" + teamsItem.getSerial(), teamsItem -> getAverageNumberOfSightingsPerDayForTeam(teamsFilteredOnDay, teamsItem, day)));
        log.info("Map result: {}", numberOfSightingsPerTeam);
        return numberOfSightingsPerTeam;
    }

    private List<TeamsItem> getAverageNumberOfSightingsPerDayForTeam(List<TeamsItem> teamsFilteredOnDay, TeamsItem teamsItem, String day) {
        log.info("Getting average number of sightings for team {} on day {}", teamsItem.getName(), day);
        List<TeamsItem> totalSightingsPerDay = new ArrayList<>();
        teamsItem.getPositions().forEach(positionsItem -> {
            try {
                List<TeamsItem> teamsWithinFiveKilometersAtMoment = getTeamsWithinFiveKilometersAtMoment(teamsFilteredOnDay, positionsItem.getGpsAt(), teamsItem.getName());
                totalSightingsPerDay.addAll(teamsWithinFiveKilometersAtMoment);
            } catch (InvalidDataException e) {
                e.printStackTrace();
            }
        });
        List<TeamsItem> uniqueTotalSightingsPerDay = totalSightingsPerDay.stream().filter(distinctByKey(TeamsItem::getName)).collect(Collectors.toList());
        return uniqueTotalSightingsPerDay;
    }

    private List<TeamsItem> getFilteredTeamDataByDay(List<TeamsItem> teamsItems, String day) throws InvalidDataException {
        log.info("Getting filtered team data for day: {}", day);
        List<TeamsItem> filteredTeams = teamsItems.stream()
                .filter(teamsItem -> teamsItem.getPositions().stream()
                        .anyMatch(positionsItem -> positionsItem.getGpsAt().startsWith(day)))
                .peek(teamsItem -> {
                    List<PositionsItem> filteredPositionItems = teamsItem.getPositions().stream()
                            .filter(positionsItem -> positionsItem.getGpsAt().startsWith(day)).collect(Collectors.toList());
                    teamsItem.setPositions(filteredPositionItems);
                })
                .collect(Collectors.toList());
        if (!(filteredTeams.size() >= 1)) {
            throw new InvalidDataException("No team data for given day.");
        }
        return filteredTeams;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
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
