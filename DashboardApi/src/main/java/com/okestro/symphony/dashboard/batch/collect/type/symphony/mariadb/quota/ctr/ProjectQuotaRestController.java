package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.quota.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.quota.svc.SymphonyProjectQuotaService;
import com.okestro.symphony.dashboard.batch.entity.ApiProjectQuotaEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/symphony-quota")
//@Api(value = "symphony-projectquota-rest-controller", description = "[Symphony DB] SYMPHONY.PROJECT_QUOTA 데이터 조회 리스트")
@RestController(value = "SymphonyProjectQuotaRestController")
public class ProjectQuotaRestController {

    @Autowired
    SymphonyProjectQuotaService symphonyProjectQuotaService;

    /**
     * PROJECT_QUOTA 조회
     * @return List<ApiProjectQuotaEntity>
     * */
  //  @GetMapping("/project-quota")
//    public List<ApiProjectQuotaEntity> listAllProjectQuota() {
//        return symphonyProjectQuotaService.listAllProjectQuota();
//    }
}
