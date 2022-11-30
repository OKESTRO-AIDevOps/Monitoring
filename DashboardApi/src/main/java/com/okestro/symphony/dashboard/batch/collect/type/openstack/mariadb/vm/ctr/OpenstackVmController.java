package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.svc.OpenstackVmService;
import com.okestro.symphony.dashboard.batch.entity.NovaInstancesEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-vm")
//@Api(value = "openstack-vm-rest-controller", description = "[openstack DB] nova.instances 데이터 조회 리스트")
@RestController
public class OpenstackVmController {
    @Autowired
    OpenstackVmService dbVmService;

    /**
     * nova.instances 조회
     * @return List<NovaInstancesEntity>
     * */
    //@GetMapping("/instances")
//    public List<NovaInstancesEntity> listAllInstances() {
//        return dbVmService.listAllInstances();
//    }

    /**
     * nova.instances vm_state=active 만 조회
     * @return List<NovaInstancesEntity>
     * */
    //@GetMapping("/instances-active")
//    public List<NovaInstancesEntity> listAllInstancesActive() {
//        return dbVmService.listAllInstancesActive();
//    }

    /**
     * SYMPHONY.VM 저장 함수
     * @return void
     * */
    //@GetMapping("/save")
//    public void saveVm() {
//        dbVmService.saveVm();
//    }
}
