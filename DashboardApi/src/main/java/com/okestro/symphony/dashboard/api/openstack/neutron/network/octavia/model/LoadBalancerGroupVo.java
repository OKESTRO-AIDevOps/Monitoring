package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoadBalancerGroupVo {
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.LoadBalancerVo> loadbalancers;
}
