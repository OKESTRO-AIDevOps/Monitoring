/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;


@Data
public class ComputeMetricVo {

    private String id;
    @JsonProperty("@timestamp")
    private long timestamp;
    private String novaName;

    private Double cpuQuota;
    private Double cpuUsage;
    private Double networkQuota;
    private Double networkUsage;
    private Double instanceQuota;
    private Double instanceUsage;

}
