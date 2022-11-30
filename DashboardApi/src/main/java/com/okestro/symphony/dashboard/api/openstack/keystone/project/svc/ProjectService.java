package com.okestro.symphony.dashboard.api.openstack.keystone.project.svc;

import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.*;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VmVo;

import java.util.List;

public interface ProjectService {

    public List<ProjectVo> getSdkProjectList(boolean dbSaveOpt);

    public List<ProjectVo> getWcProjectList(boolean dbSaveOpt);


    public List<QuotaProjectVo> getAllProjectQuotaList(boolean dbSaveOpt);

    public List<QuotaProjectVo> getSdkAllProjectQuotaList(boolean dbSaveOpt);

    public List<QuotaProjectVo> getWcAllProjectQuotaList(boolean dbSaveOpt);


}
