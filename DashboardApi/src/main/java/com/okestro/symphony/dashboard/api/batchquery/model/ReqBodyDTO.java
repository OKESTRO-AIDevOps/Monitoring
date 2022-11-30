package com.okestro.symphony.dashboard.api.batchquery.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReqBodyDTO {
    private String query;

    @JsonProperty("display_type")
    private String displayType;

    private String aggregator;

    private Integer numSeries;

    private String order;

    private Time time;

    private Opt opt;

    @Data
    public class Time {
        @JsonProperty("live_span")
        private String liveSpan = "1h";
    }

    @Data
    public class Opt {
        private String provider = "";
    }
}
