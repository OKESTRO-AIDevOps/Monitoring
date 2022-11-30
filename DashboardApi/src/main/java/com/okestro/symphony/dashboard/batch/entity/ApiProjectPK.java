package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiProjectPK implements Serializable {
    private String projectId;
    private String providerId;
    private Date collectDt;
}
