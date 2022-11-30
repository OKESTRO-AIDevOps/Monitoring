package com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VVmLastedPK implements Serializable {
    private String vmId;
    private String providerId;
    private Date collectDt;
}
