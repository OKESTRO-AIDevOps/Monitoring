package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PortVo extends CommonVo {
    private boolean adminStateUp;
    private String adminStateUpKo;
    private String tenantId;
    private String status;
    private String statusKo;
    private boolean portSecurityEnabled;
    private String portSecurityEnabledKo;
    private String networkId;
    private String name;
    private String portUpExName;
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

    private List<AllowedAddressPairVo> allowedAddressPairs;

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
