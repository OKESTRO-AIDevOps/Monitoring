package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.repo.NovaFloatingIpsRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.svc.OpenstackHypervisorService;
import com.okestro.symphony.dashboard.batch.entity.NovaFloatingIpsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenstackHypervisorServiceImpl implements OpenstackHypervisorService {

//    @Autowired
//    NovaFloatingIpsRepository keystoneFloatingIpsRepository;

    /**
     * nova.floating_ips 조회
     * @return List<NovaFloatingIpsEntity>
     * */
//    @Override
//    public List<NovaFloatingIpsEntity> listAllInstances() {
//        return keystoneFloatingIpsRepository.findAll();
//    }
}
