package com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class VHostLastedPK implements Serializable {
    private String hostId;
    private String providerId;
    private Date collectDt;
}
