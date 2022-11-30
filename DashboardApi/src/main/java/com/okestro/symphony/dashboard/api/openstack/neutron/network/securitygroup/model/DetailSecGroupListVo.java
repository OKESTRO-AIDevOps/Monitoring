/*
 * Developed by yy.go@okestro.com on 2020-07-26
 * Last modified 2020-07-26 19:02:14
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class DetailSecGroupListVo extends CommonVo {

    private Map<String, com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model.DetailSecGroupVo> securityGroup;
}
