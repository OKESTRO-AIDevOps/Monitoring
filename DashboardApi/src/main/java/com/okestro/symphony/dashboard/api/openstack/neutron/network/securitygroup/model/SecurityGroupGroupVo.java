/*
 * Developed by yy.go@okestro.com on 2020-07-27
 * Last modified 2020-07-27 13:07:37
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityGroupGroupVo extends CommonVo {
    List<com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model.SecurityGroupVo> securityGroups;

}
