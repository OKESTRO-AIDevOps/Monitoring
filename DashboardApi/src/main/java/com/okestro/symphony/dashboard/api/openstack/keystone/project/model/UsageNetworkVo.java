package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class UsageNetworkVo {

    private int subnetUsed;
    private int routerUsed;
    private int portUsed;
    private int networkUsed;
    private int fixedIpUsed;
    private int floatingIpUsed;
    private int securityGroupUsed;
    private int securityGroupRuleUsed;
}
