package com.rockseven.test.data.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
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