package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.svc.OpenstackHostService;
import com.okestro.symphony.dashboard.batch.entity.NovaComputeNodesEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-host")
//@Api(value = "openstack-host-rest-controller", description = "[openstack DB] nova.compute_nodes 데이터 조회 리스트")
@RestController
public class OpenstackHostController {

    @Autowired
    OpenstackHostService openstackHostService;

    /**
     * nova.compute_nodes 조회
     * @return List<NovaComputeNodesEntity>
     * */
    //@GetMapping("/compute-nodes")
//    public List<NovaComputeNodesEntity> listAllComputeNodes() {
//        return openstackHostService.listAllComputeNodes();
//    }

    /**
     * SYMPHONY.HOST 저장 함수
     * @return void
     * */
    //@GetMapping("/save")
//    public void saveHost() {
//        openstackHostService.saveHost();
//    }
}
