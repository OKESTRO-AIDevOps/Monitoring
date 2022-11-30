/*
 * Developed by sychoi on 2020-07-16
 * Last modified 2020-07-16 14:11:43
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model;

import lombok.Data;

@Data
public class FloatingIpQuotaVo {

    private String projectId;
    private String projectNm;
    private int floatingIpUsed;
    private int floatingIpQuota;
    private FloatingIpPool pool = new FloatingIpPool();

}
