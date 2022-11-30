/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import lombok.Data;

// 지난 주 대비 프로젝트별 Cpu,Memory,Instance 사용률
@Data
public class OstProjectAggregatedVo {

    private String id;
    private long timestamp;
    private String projectId;
    private String projectName;
    private String domain;
    private String dateRange;

    private Double cpuQuotaAvg;
    private Double cpuUsageAvg;
    private Double cpuAvgMin;
    private Double ramQuotaAvg;
    private Double ramUsageAvg;
    private Double ramAvgMin;
    private Double instanceQuotaAvg;
    private Double instanceUsageAvg;
    private Double instanceAvgMin;

    private Double cpuUsageCompareAvg;
    private Double ramUsageCompareAvg;
    private Double instanceUsageCompareAvg;


}
