package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.project.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.project.svc.SymphonyProjectService;
import com.okestro.symphony.dashboard.batch.entity.ApiProjectEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/symphony-project")
//@Api(value = "symphony-project-rest-controller", description = "[Symphony DB] SYMPHONY.PROJECT 데이터 조회 리스트")
@RestController(value = "SymphonyProjectRestController")
public class ProjectRestController {

    @Autowired
    SymphonyProjectService symphonyProjectService;

    /**
     * PROJECT 조회
     * @return List<ApiProjectEntity>
     * */
    //@GetMapping("/project")
//    public List<ApiProjectEntity> listAllProject() {
//        return symphonyProjectService.listAllProject();
//    }
}
