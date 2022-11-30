/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class HypervisorMetricVo {

    private String id;
    private long timestamp;
//    private Hypervisor hypervisor;
    private String hypervisorId;
    private String name;
    private String type;
    private String state;
    private String status;
    private int vcpus;
    private int memoryMb;
    private int localGb;
    private int vcpusUsed;
    private int memoryMbUsed;
    private int localGbUsed;
    private int hypervisorVersion;
    private int freeRamMb;
    private int freeDiskGb;
    private int currentWorkload;
    private int runningVms;
    private int disAvailableLeast;
    private String hostIp;
    private Service service;
    private String cpuInfo;

        @Data
        public class Service {
            private String id;
            private String host;
            private String disabledReason;
        }


}
