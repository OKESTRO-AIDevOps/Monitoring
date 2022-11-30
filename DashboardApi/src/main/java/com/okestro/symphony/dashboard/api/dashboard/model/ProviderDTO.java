package com.okestro.symphony.dashboard.api.dashboard.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProviderDTO {
    private String type;

    @JsonProperty("provider_id")
    private String prvdId;
}
