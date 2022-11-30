package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.svc.OpenstackAggregateHostService;
import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregateHostsEntity;
import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregatesEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-aggregate")
//@Api(value = "openstack-aggregate-rest-controller", description = "[openstack DB] nova_api.aggregates,aggregate_host 데이터 조회 리스트")
@RestController
public class OpenstackAggregateHostController {

    @Autowired
    OpenstackAggregateHostService openstackAggregateHostService;

    /**
     * nova_api.aggregates 조회
     * @return List<NovaApiAggregatesEntity>
     */
    //@GetMapping("/aggregates")
//    public List<NovaApiAggregatesEntity> listAllAggregates() {
//        return openstackAggregateHostService.listAllAggregates();
//    }

    /**
     * nova_api.aggregate_host 조회
     * @return List<NovaApiAggregateHostsEntity>
     * */
    //@GetMapping("/aggregate-host")
//    public List<NovaApiAggregateHostsEntity> listAllAggregateHost() {
//        return openstackAggregateHostService.listAllAggregateHost();
//    }

    /**
     * SYMPHONY.HOST_AGGREGATE 저장 함수
     * @return void
     * */
    //@GetMapping("/aggregates/save")
//    public void saveHostAggregate() {
//        openstackAggregateHostService.saveHostAggregate();
//    }

    /**
     * SYMPHONY.HOST_AGGREGATE_HOST & HOST_AGGREGATE 저장 함수
     * @return void
     * */
    //@GetMapping("/aggregate-host/save")
//    public void saveHostAggregateHost() {
//         openstackAggregateHostService.saveHostAggregateHost();
//    }
}
