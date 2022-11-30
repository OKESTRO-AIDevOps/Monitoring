package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class QuotaNetworkVo {

    private int subnetQuota;
    private int routerQuota;
    private int portQuota;
    private int fixedIpQuota;
    private int networkQuota;
    private int floatingIpQuota;
    private int securityGroupQuota;
    private int securityGroupRuleQuota;

}
