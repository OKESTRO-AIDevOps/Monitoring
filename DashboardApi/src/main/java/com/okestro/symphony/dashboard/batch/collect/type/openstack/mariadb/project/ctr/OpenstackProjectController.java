package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.svc.OpenstackProjectService;
import com.okestro.symphony.dashboard.batch.entity.KeystoneProjectEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/db-project")
//@Api(value = "openstack-project-rest-controller", description = "[openstack DB] keystone.project 데이터 조회 리스트")
@RestController
public class OpenstackProjectController {

    @Autowired
    OpenstackProjectService openstackProjectService;

    /**
     * keystone.project 조회
     * @return List<KeystoneProjectEntity>
     * */
    //@GetMapping("/project")
//    public List<KeystoneProjectEntity> listAllProject() {
//        return openstackProjectService.listAllProject();
//    }

    /**
     * SYMPHONY.PROJECT 저장 함수
     * @return void
     * */
    //@GetMapping("/save")
//    public void saveProject() {
//        openstackProjectService.saveProject();
//    }
}
