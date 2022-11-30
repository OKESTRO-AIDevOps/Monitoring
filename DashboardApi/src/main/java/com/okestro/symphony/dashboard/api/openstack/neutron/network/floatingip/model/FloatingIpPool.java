package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FloatingIpPool {
    private String networkName;
    private String networkId;
}
