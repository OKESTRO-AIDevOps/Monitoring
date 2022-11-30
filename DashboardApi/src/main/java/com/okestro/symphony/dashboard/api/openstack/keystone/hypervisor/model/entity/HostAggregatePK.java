package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HostAggregatePK implements Serializable {
    private String hostAggregateId;
    private String providerId;
    private Date collectDt;
}
