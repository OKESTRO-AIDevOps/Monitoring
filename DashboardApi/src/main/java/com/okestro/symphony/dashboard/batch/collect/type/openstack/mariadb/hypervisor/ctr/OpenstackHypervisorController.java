package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.svc.OpenstackHypervisorService;
import com.okestro.symphony.dashboard.batch.entity.NovaFloatingIpsEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-hypervisor")
//@Api(value = "openstack-hypervisor-rest-controller", description = "[openstack DB] nova.floating_ips 데이터 조회 리스트")
@RestController
public class OpenstackHypervisorController {

    @Autowired
    OpenstackHypervisorService openstackHypervisorService;

    /**
     * nova.floating_ips 조회
     * @return List<NovaFloatingIpsEntity>
     * */
    //@GetMapping("/floating-ips")
//    public List<NovaFloatingIpsEntity> listAllInstances() {
//        return openstackHypervisorService.listAllInstances();
//    }

}
