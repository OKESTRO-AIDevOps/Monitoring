package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.svc;

import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.InstancesEntity;
import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ComputeVmService {
    List<VmVo> retrieveVms(boolean dbSaveOpt);

    List<VmVo> retrieveWcVms(boolean dbSaveOpt);

//    VmVo retrieveVm(VmVo vm);

//    public List<InstancesEntity> listAllInstances();

    public List<InstancesEntity> betweenListAllInstances(LocalDateTime sTime, LocalDateTime eTime);

//    VmCreateInfoVo retrieveVmCreateInfo(VmVo vm);

//    List<NetworkVo> retrieveNetworkList(VmVo vm);

//    List<PortVo> retrieveNetworkPortList(VmVo vm);


//    List<NetworkVo> retrieveVmNetwork(VmVo vm);

//    OpenStackResultVo retrieveVmConsoleLog(VmVo vm);

//    List<HostVo> retrieveHosts(VmVo vm);
}
