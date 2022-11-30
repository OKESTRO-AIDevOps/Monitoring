package com.okestro.symphony.dashboard.api.meta.model;

import lombok.Data;

@Data
public class VmDTO {
    private String id;
    private String name;
    private String tenantId;
    private String userId;
    private String host;
    private String hypervisorHostName;
    private String instanceName;
    private String availabilityZone;
    private String state;
    private String status;

    private int vCpus;
    private int localMemory;
    private int localDisk;
}