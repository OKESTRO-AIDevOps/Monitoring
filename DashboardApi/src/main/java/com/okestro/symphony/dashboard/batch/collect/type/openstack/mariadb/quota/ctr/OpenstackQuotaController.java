package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.ctr;

//import com.okestro.symphony.dashboard.batch.collect.type.db.quota.entity.ComputeNodesEntity;
//import com.okestro.symphony.dashboard.batch.collect.type.db.quota.entity.FloatingIpsEntity;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.svc.OpenstackQuotaService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-quota")
//@Api(value = "openstack-quota-rest-controller", description = "[openstack DB] [cinder novaapi, neutron].quotas 데이터 조회 리스트")
@RestController
public class OpenstackQuotaController {

    @Autowired
    OpenstackQuotaService quotaService;

    /**
     * openstack-mariaDB 조회 (target table : nova.compute_nodes)
     * */
//    @GetMapping("/compute-nodes/")
//    public List<ComputeNodesEntity> listAllComputeNodes() {
//        return quotaService.listAllComputeNodes();
//    }

    /**
     * openstack-mariaDB 조회 (target table : nova.floating_ips)
     * */
//    @GetMapping("/floating-ips/")
//    public List<FloatingIpsEntity> listAllFloatingIps() {
//        return quotaService.listAllFloatingIps();
//    }

    /**
     * nova_api.quotas 조회
     * @return List<Object>
     * */
    //@GetMapping("/novaapi")
//    public List<Object> listAllNovaApiQuota() {
//        return quotaService.listAllNovaApiQuota();
//    }

    /**
     * cinder.quotas 조회
     * @return List<Object>
     * */
    //@GetMapping("/cinder")
//    public List<Object> listAllCinderQuota() {
//        return quotaService.listAllCinderQuota();
//    }

    /**
     * neutron.quotas 조회
     * @return List<Object>
     * */
    //@GetMapping("/neutron")
//    public List<Object> listAllNeutronQuota() {
//        return quotaService.listAllNeutronQuota();
//    }

    /**
     * SYMPHONY.PROJECT_QUOTA 저장 함수
     * @return void
     * */
    //@GetMapping("/save")
//    public void saveProjectQuota() {
//        quotaService.saveProjectQuota();
//    }
}
