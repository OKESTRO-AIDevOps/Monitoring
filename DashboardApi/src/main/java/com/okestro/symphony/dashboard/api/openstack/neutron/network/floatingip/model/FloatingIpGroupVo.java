/*
 * Developed by sychoi on 2020-07-16
 * Last modified 2020-07-16 14:11:43
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model;

import lombok.Data;

import java.util.List;

@Data
public class FloatingIpGroupVo {
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpVo> floatingips;
}
