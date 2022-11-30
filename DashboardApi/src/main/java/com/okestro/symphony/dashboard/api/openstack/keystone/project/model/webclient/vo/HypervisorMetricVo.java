package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class HypervisorMetricVo {

    private List<Hypervisor> hypervisors;

    @Data
    public static class Hypervisor {
        private int id;
        @JsonProperty("hypervisor_hostname")
        private String hypervisorHostname;
        private String state;
        private String status;
        private int vcpus;
        @JsonProperty("memory_mb")
        private int memoryMb;
        @JsonProperty("vcpus_used")
        private int vcpusUsed;
        @JsonProperty("memory_mb_used")
        private int memoryMbUsed;
        @JsonProperty("local_gb_used")
        private int localGbUsed;
        @JsonProperty("hypervisor_type")
        private String hypervisorType;
        @JsonProperty("hypervisor_version")
        private int hypervisorVersion;
        @JsonProperty("free_ram_mb")
        private int freeRamMb;
        @JsonProperty("free_disk_gb")
        private int freeDiskGb;
        @JsonProperty("current_workload")
        private int currentWorkload;
        @JsonProperty("running_vms")
        private int runningVms;
        @JsonProperty("disk_available_least")
        private int diskAvailableLeast;
        @JsonProperty("host_ip")
        private String hostIp;
        private Service service;
        @JsonProperty("cpu_info")
        private String cpuInfo;


        @Getter
        @Setter
        @ToString
        public static class Service {

            private int id;
            private String host;
            @JsonProperty("disabled_reason")
            private String disabledReason;

        }
    }

}
