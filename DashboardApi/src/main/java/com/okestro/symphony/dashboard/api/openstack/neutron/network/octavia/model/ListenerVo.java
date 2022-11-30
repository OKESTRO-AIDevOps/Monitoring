package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListenerVo extends CommonVo {
    private String id;
    private String tenantId;
    private String name;
    private String description;
    private String protocol;
    private int protocolPort;
    private int connectionLimit;
    private String defaultPoolId;
    private String loadbalancerId;
    private boolean adminStateUp;
    private String operatingStatus;

    private String healthMonitorId;
    private String healthMonitorType;
    private String healthMonitorPath;
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.MemberVo> members;
    private String lbMethod;
}
