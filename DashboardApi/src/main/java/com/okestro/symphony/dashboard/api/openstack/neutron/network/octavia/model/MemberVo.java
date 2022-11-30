package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVo extends CommonVo {
    private String id;
    private String name;
    private int weight;
    private boolean adminStateUp;
    private String subnetId;
    private String tenantId;
    private String address;
    private int protocolPort;
    private String poolId;
    private String loadBalancerId;
    private String liName;

    private String deviceId;
    private String deviceOwner;
    private String vmName;
    private String portId;
    private String floatingIpId;
    private String floatingIp;
    private String subnetName;

    private String crud;
    private String rgtdVm;
}
