/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class OstProjectMetricVo {

    private String id;
    @JsonProperty("@timestamp")
    private long timestamp;
    private String projectId;
    private String projectName;
    private String domain;
    private String userId;
    private String groupName;

    private int cpuQuota;
    private int cpuUsage;
    private int memoryQuota;
    private int memoryUsage;
    private int networkQuota;
    private int networkUsage;
    private int instanceQuota;
    private int instanceUsage;
    private Double volumeQuota;
    private Double volumeUsage;



}
