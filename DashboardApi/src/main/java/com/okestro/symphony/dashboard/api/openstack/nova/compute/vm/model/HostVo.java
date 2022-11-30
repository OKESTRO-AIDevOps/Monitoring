package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;

@Data
public class HostVo {
    private String hostId;
    private String hostName;
    private String service;
    private String zone;
}