package com.okestro.symphony.dashboard.api.meta.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HostDTO {
    private String id;
    private String name;
    private String hypervisorHostname;
    private String ipAddress;
    private String state;
    private String status;

    private String availabilityZone;
    private String project;
    private String machineModel;


    private int vmCount;
    private int totalVCpu;
    private int usedVCpu;
    private BigDecimal vCpuPercent;
    private long totalMemory;
    private int usedMemory;
    private BigDecimal memoryPercent;
    private int totalDisk;
    private int usedDisk;
    private BigDecimal diskPercent;

    private Boolean isCompute;
}
