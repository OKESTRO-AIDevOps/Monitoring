package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.svc.HostService;
import com.okestro.symphony.dashboard.batch.entity.ApiHostEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/symphony-host")
//@Api(value = "symphony-host-rest-controller", description = "[Symphony DB] SYMPHONY.HOST 데이터 조회 리스트")
@RestController(value = "SymphonyHostRestController")
public class HostRestController {

    @Autowired
    HostService hostService;

    /**
     * HOST 조회
     * @return List<ApiHostEntity>
     * */
  //  @GetMapping("/host")
//    public List<ApiHostEntity> listAllHost() {
//        return hostService.listAllHost();
//    }
}
