package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VHostLastedVo {
    private String id;
    private String name;
    private String collectDt;
    private String status;
    private String state;
    private String hostIp;
    private int runningVM;
    private int virtualCPU;
    private int virtualUsedCPU;
    private int localDisk;
    private int localDiskUsed;
    private int freeDisk;
    private int localMemory;
    private int localMemoryUsed;
    private int freeRam;

    private String providerId;
    private String providerNm;

}
