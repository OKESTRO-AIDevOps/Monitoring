package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.svc;

import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model.SecurityGroupVo;

public interface SecurityGroupService {

    public OpenStackResultVo getSecurityGroups(String projectName , String userId);

    public OpenStackResultVo retrieveSecurityGroup(SecurityGroupVo securityGroupVo);

    public OpenStackResultVo checkDupSecGroupName(String projectName, String name , String userId);

}
