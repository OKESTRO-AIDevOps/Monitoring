package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedIpVo extends CommonVo {
    private String ipAddress;
    private String subnetId;
}
