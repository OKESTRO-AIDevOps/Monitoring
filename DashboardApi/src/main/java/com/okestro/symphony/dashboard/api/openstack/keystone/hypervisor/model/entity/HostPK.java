package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HostPK implements Serializable {
    private String hostId;
    private String providerId;
    private Date collectDt;

}
