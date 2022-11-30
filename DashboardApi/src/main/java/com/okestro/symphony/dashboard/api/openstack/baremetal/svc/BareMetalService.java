/*
 * Developed by bhhan@okestro.com on 2020-07-17
 * Last modified 2020-07-08 17:22:49
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.svc;

import com.okestro.symphony.dashboard.api.openstack.baremetal.model.DriverVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.NodeVo;

import java.util.List;

public interface BareMetalService {
    public boolean isHasRole(String projectName, String userId);
    public List<NodeVo> retrieveNodes(String projectName, String userId);
    public NodeVo retrieveNodeDetail(String projectName, String userId, String uuid);
    public List<DriverVo> retrieveDrivers(String projectName, String userId);

}