package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ComputeVmPK implements Serializable {
    private String vmId;
    private String providerId;
    private Date collectDt;
}
