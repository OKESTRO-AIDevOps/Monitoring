package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.svc;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;

import java.util.List;


public interface HypervisorService {
    public List<HostVo> listAllHypervisor(boolean dbSaveOpt);

    public List<HostVo> listWcAllHypervisor(boolean dbSaveOpt);
//    public List<HostVo> list(String projectName, String userId);

}