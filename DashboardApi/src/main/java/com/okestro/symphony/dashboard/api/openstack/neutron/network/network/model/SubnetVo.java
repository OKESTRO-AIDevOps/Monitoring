package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubnetVo extends CommonVo {
    private String id;
    private String name;
    private String subnetUpExName;
    private Boolean enableDHCP;
    private String networkId;
    private String tenantId;
    private List<String> dnsNames;
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.PoolsVo> pools;
    private List<String> hostRoutes;
    private String ipVersion;
    private String gateway;
    private String cidr;
    private int usedIpCount;
    private int totalIpCount;
}
