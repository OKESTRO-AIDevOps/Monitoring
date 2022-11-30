/*
 * Developed by sychoi on 2020-07-16
 * Last modified 2020-07-16 14:11:43
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class FloatingIpConnentVo extends CommonVo {

    private String deviceId;
    private String deviceOwner;
    private String vmName;
    private String vmIp;
    private String portId;
    private String floatingIpId;
    private String floatingIp;
    private String floatingIpAddress;
    private String subnetId;
    private String subnetName;
    private String lbName;
    private String type;

    private String crud;
    private int port;
}
