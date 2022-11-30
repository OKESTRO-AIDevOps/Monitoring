/*
 * Developed by yy.go@okestro.com on 2020-07-08
 * Last modified 2020-07-08 15:33:43
 */

package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.svc;

import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;

import java.util.List;

public interface InstanceTypeService {

    public List<FlavorVo> retrieveAllInstanceTypes();

//    public List<FlavorVo> retrieveInstanceTypes(String projectName , String userId);


}
