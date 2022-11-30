package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;

@Getter
@Setter
public class HostVo {
    private String id;
    private String timestamp;
    private String name;
    private String type;
    private String status;
    private String state;
    private String hostIp;
    @Field(name = "current_workload")
    private int currentWorkload;
    @Field(name = "least_disk_available")
    private int leastDiskAvailable;
    @Field(name = "hypervisor_version")
    private int hypervisorVersion;
    @Field(name = "running_vms")
    private int runningVms;
    @Field(name = "vcpus")
    private int vcpus;
    @Field(name = "vcpus_used")
    private int vcpusUsed;
    @Field(name = "local_gb")
    private int localGb;
    @Field(name = "local_gb_used")
    private int localGbUsed;
    @Field(name = "free_disk_gb")
    private int freeDiskGb;
    @Field(name = "memory_mb")
    private int memoryMb;
    @Field(name = "memory_mb_used")
    private int memoryMbUsed;
    @Field(name = "free_ram")
    private int freeRam;

//    private CpuInfoVo cpuInfo;

    private String providerName;

//    private String retrievedDt;     //o

}
