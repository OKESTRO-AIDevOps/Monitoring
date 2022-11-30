package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiVmPK implements Serializable{
    private String vmId;
    private String providerId;
    private Date collectDt;
}