package com.rockseven.test.repository.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceData {

    @Id
    @JsonProperty("raceUrl")
    private String raceUrl;

    @JsonProperty("teams")
    private List<TeamsItem> teams;
}