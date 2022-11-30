package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.openstack4j.model.network.Subnet;

import java.util.List;

@Getter
@Setter
public class NetworkVo extends CommonVo {

    private String status;
    private String statusKo;
    private List<String> subnets;
//    private List<SubnetVo> subnets;
//    private List<String> neutronSubnets;
    private List<SubnetVo> neutronSubnets;

    private String name;
    private String updateExName;
    private String providerPhyNet;
    private Boolean adminStateUp;
    private String tenantId;
    private String networkType;
    private Boolean routerExternal;
    private String id;
    private Boolean shared;
    private String providerSegID;
    private List<String> availabilityZoneHints;
    private List<String> availabilityZones;
    private int mtu;
    private String checkBox;

    private cmdProject cmdProject;
    private String cmdProviderId;

    //생성시 필요한 서브넷정보 추가
    private String cidr;

    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.SecurityGroupVo> securityGroupVoList;
    @Data
    public class cmdProject {
        private String id;
        private String name;
        private String role;
    }

}
