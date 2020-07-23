package com.rockseven.test.repository.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamsItem{

    @JsonProperty("serial")
    private int serial;

    @JsonProperty("marker")
    private int marker;

    @JsonProperty("name")
    private String name;

    @JsonProperty("positions")
    private List<PositionsItem> positions;
}