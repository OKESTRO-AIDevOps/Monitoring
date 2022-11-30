package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HealthMonitorVo extends CommonVo {
    private String id;
    private String tenantId;
    private String type;
    private int delay;
    private int timeout;
    private int maxRetries;
    private String httpMethod;
    private String urlPath;
    private String expectedCodes;
    private boolean adminStateUp;
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.PoolVo> pools;
    private String poolId;
    private String loadBalancerId;
}
