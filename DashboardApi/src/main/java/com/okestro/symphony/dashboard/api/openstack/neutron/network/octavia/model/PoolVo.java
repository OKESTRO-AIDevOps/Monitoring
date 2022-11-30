package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PoolVo extends CommonVo {
    private String id;
    private String name;
    private String tenantId;
    private String description;
    private String protocol;
    private String lbMethod;
    private boolean adminStateUp;
    private String listenerId;
    private List<ListenerVo> listeners;
    private List<MemberVo> members;
    private String healthMonitorId;
    private HealthMonitorVo healthmonitorVo;

    private String loadBalancerId;

    private String resultMessage;

}
