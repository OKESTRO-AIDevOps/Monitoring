package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VVmLastedVo {
    private String id;
    private String name;
    private String vmStatus;
    private String pwStatus;
    private String powerStatus;
    private String providerId;
    private String providerName;
    private String projectId;
    private String projectName;
    private String collectDt;
    private String hostId;
    private String hostName;
    private int cpu;
    private int ram;
    private int disk;
    private String avZoneNm;
    private String createDt;
    private String launched;
}
