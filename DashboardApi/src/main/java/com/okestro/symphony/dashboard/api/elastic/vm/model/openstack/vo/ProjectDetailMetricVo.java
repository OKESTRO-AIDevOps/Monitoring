/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;


@Data
public class ProjectDetailMetricVo {

    private String id;
    @JsonProperty("@timestamp")
    private long timestamp;
    private String projectId;
    private String projectName;
    private String domain;
    private String userId;
    private String groupName;

    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaComputeVo computeQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaNetworkVo networkQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaVolumnVo volumeQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageComputeVo computeUsage;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageNetworkVo networkUsage;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageVolumnVo volumeUsage;

}
