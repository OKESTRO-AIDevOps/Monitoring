package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiHostPK implements Serializable {
    private String hostId;
    private String providerId;
    private Date collectDt;

}
