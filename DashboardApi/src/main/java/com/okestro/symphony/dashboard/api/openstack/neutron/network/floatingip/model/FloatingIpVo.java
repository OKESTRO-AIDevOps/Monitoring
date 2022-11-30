/*
 * Developed by sychoi on 2020-07-16
 * Last modified 2020-07-16 14:11:43
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.PortVo;
import lombok.Data;

import java.util.List;

@Data
public class FloatingIpVo extends CommonVo {
    private String id;
    private String routerId;
    private String status;
    private String description;
    private List<String> tags;
    private String tenantId;
    private String createdAt;
    private String updatedAt;
    private String floatingNetworkId;
    private String floatingNetworkName;
    private String type;
    private String vmName;
    private String lbName;

    private String fixedIpAddress;
    private String floatingIpAddress;
    private int revisionNumber;
    private String projectId;
    private String portId;
    private String qosPolicyId;
    private PortVo portDetails;
    private String deviceName;
    private List<route> routes;

    @Data
    public static class route {
        private String nexthop;
        private String destination;
    }
}
