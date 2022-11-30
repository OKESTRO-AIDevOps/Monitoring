package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
public class QuotaProjectVo extends CommonVo {

    private String projectId;
    private String projectName;
    private String groupName;
    private String domain;
    @JsonProperty("@timestamp")
    private String timestamp;

    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaComputeVo computeQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaNetworkVo networkQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaVolumnVo volumeQuota;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageComputeVo computeUsage;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageNetworkVo networkUsage;
    private com.okestro.symphony.dashboard.api.openstack.keystone.project.model.UsageVolumnVo volumeUsage;

}


