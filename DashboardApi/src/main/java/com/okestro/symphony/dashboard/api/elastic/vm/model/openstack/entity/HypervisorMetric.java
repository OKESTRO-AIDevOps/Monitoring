/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "ost-metric-hypervisors-#{@elasticIndex.getIndexDate()}")
//@Document(indexName = "#{@elasticIndex.getIndex()}")
// @Document(indexName = "ost-metric-hypervisor-2021-04-14")
public class HypervisorMetric implements Serializable {

    @Id
    private String id;

    @Field(name = "@timestamp")
    private long timestamp;

    @Field(name = "hypervisor")
    private Hypervisors hypervisor;


    @Builder
    public HypervisorMetric(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }


    @Getter
    @Setter
    @ToString
    public static class Hypervisors {
        @Field(name = "hypervisor_id")
        private String hypervisorId;
        @Field(name = "hypervisor_hostname")
        private String hypervisorHostname;
        private String type;
        private String state;
        private String status;
        private int vcpus;
        @Field(name = "memory_mb")
        private int memoryMb;
        @Field(name = "local_gb")
        private int localGb;
        @Field(name = "vcpus_used")
        private int vcpusUsed;
        @Field(name = "memory_mb_used")
        private int memoryMbUsed;
        @Field(name = "local_gb_used")
        private int localGbUsed;
        @Field(name = "hypervisor_version")
        private int hypervisorVersion;
        @Field(name = "free_ram_mb")
        private int freeRamMb;
        @Field(name = "free_disk_gb")
        private int freeDiskGb;
        @Field(name = "current_workload")
        private int currentWorkload;
        @Field(name = "running_vms")
        private int runningVms;
        @Field(name = "disk_available_least")
        private int disAvailableLeast;
        @Field(name = "host_ip")
        private String hostIp;

        private HypervisorMetric.Hypervisors.Service service;
        @Field(name = "cpu_info")
        private String cpuInfo;

        @Getter
        @Setter
        @ToString
        public class Service {
            private String id;
            private String host;
            @Field(name = "disabled_reason")
            private String disabledReason;
        }
    }

}
