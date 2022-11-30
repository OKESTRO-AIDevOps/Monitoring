/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import lombok.Data;

// 물리 서버별 메모리 사용률
@Data
public class OstHypervisorAggregatedVo {

    private String id;
    private long timestamp;
    private String hypervisorId;
    private String hypervisorName;
    private String type;
    private String curStatus;

    private Double vcpuQuota;
    private Double vcpuUsage;
    private Double vcpuUsagePercent;
    private Double memoryQuota;
    private Double memoryUsage;
    private Double memoryUsagePercent;
    private Integer runningvms;


}
