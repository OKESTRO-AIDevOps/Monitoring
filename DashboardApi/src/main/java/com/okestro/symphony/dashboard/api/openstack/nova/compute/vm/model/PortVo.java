/*
 * Developed by kbcho@okestro.com on 2020-07-16
 * Last modified 2020-07-16 14:56:02
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 20:20:41
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 16:53:02
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 16:46:13
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 10:58:43
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 18:09:23
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PortVo {
    private boolean adminStateUp;
    private String tenantId;
    private String status;
    private boolean portSecurityEnabled;
    private String networkId;
    private String name;
    private String macAddress;
    private String id;
    private String deviceOwner;
    private String deviceId;
    private String vnicType;
    private String vifType;
    private String hostId;
    private boolean fixedIpAt;
    private String fixedIp;
    private String subnetId;

//    private List<AllowedAddressPairVo> allowedAddressPairs;

    //fiexd_ips
    private List<FixedIpVo> fixedIps;

    //profile
    private Map<String, Object> profileMap;

    //vifDetails
    private Map<String, Object> vifDetailsMap;

    //extraDhcpOpts

    //securityGroups
    private List<String> securityGroups;
    private List<String> securityGroupNames;


    //추가 정보
    private String crud;
    private String resultMessage;
    private String deviceType;




}

