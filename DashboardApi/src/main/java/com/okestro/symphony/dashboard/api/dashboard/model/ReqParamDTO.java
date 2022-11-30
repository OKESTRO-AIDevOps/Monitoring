package com.okestro.symphony.dashboard.api.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReqParamDTO {
    @JsonProperty("dashboard_type")
    private String dashboardType;

    @JsonProperty("provider_id")
    private String prvdId;

}
