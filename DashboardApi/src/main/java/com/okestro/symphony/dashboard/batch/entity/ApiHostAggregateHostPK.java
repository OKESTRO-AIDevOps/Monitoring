package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiHostAggregateHostPK implements Serializable {
    private String hostAggregateId;
    private String hostId;
    private String providerId;
    private Date collectDt;
}
