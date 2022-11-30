package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.svc.SymphonyAggregateService;
import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateEntity;
import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateHostEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/symphony-aggregate")
//@Api(value = "symphony-aggregate-rest-controller", description = "[Symphony DB] SYMPHONY.HOST_AGGREGATE, HOST_AGGREGATE_HOST 데이터 조회 리스트")
@RestController(value = "SymphonyAggregateRestController")
public class AggregateRestController {

    @Autowired
    SymphonyAggregateService symphonyAggregateService;

    /**
     * HOST_AGGREGATE 조회
     * @return List<ApiHostAggregateEntity>
     * */
   // @GetMapping("/host-aggregate")
//    public List<ApiHostAggregateEntity> listAllHostAggregate() {
//        return symphonyAggregateService.listAllHostAggregate();
//    }

    /**
     * HOST_AGGREGATE_HOST 조회
     * @return List<>
     * */
    //@GetMapping("/host-aggregate-host")
//    public List<ApiHostAggregateHostEntity> listAllHostAggregateHost() {
//        return symphonyAggregateService.listAllHostAggregateHost();
//    }
}
