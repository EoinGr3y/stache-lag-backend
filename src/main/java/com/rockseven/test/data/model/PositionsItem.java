package com.rockseven.test.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PositionsItem{

    @JsonProperty("sogKmph")
    private double sogKmph;

    @JsonProperty("altitude")
    private int altitude;

    @JsonProperty("txAt")
    private String txAt;

    @JsonProperty("gpsAtMillis")
    private long gpsAtMillis;

    @JsonProperty("dtfNm")
    private double dtfNm;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("type")
    private String type;

    @JsonProperty("battery")
    private int battery;

    @JsonProperty("dtfKm")
    private double dtfKm;

    @JsonProperty("sogKnots")
    private double sogKnots;

    @JsonProperty("alert")
    private boolean alert;

    @JsonProperty("cog")
    private int cog;

    @JsonProperty("id")
    private int id;

    @JsonProperty("gpsAt")
    private String gpsAt;

    @JsonProperty("longitude")
    private double longitude;
}