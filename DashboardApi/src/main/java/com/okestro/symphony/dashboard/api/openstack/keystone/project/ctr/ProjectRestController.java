package com.okestro.symphony.dashboard.api.openstack.keystone.project.ctr;

import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.svc.ProjectService;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Api(value = "project", description = "[API] Project & ProjectQuota 리스트")
@RequestMapping("/project")
@RestController
public class ProjectRestController {
    @Autowired
    ProjectService projectService;



    /**
     * Webclient를 통해 admin 유저 권한으로 모든 프로젝트 List를 물러옴.
     * @param dbSaveOpt
     * @return
     */
//    @GetMapping(value = {"/wc/list","/wc/list/{dbSaveOption}"})
    public List<ProjectVo> listAllToWc(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return projectService.getWcProjectList(dbSaveOpt);
    }

    /**
     * SDK를 통해 admin 유저 권한으로 모든 프로젝트 List를 물러옴.
     * @param dbSaveOpt
     * @return
     */
//    @GetMapping(value = {"/sdk/list","/sdk/list/{dbSaveOption}"})
    public List<ProjectVo> listAllToSdk(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return projectService.getSdkProjectList(dbSaveOpt);
    }


    /**
     * Webclient를 통해 admin 유저 권한으로 모든 프로젝트 List를 불러온 후 각 프로젝트의 Quota 정보 리턴.
     * @param dbSaveOpt
     * @return
     */
//    @GetMapping(value = {"/wc/quotalist","/wc/quotalist/{dbSaveOption}"})
    public List<QuotaProjectVo> listAllProjectQuotaToWc(@PathVariable("dbSaveOption") Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return projectService.getWcAllProjectQuotaList(dbSaveOpt);
    }


    /**
     * SDK를 통해 admin 유저 권한으로 모든 프로젝트 List를 불러온 후 각 프로젝트의 Quota 정보 리턴.
     * @param dbSaveOpt
     * @return
     */
//    @GetMapping(value = {"/sdk/quotalist","/sdk/quotalist/{dbSaveOption}"})
    public List<QuotaProjectVo> listAllProjectQuotaToSdk(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return projectService.getAllProjectQuotaList(dbSaveOpt);
    }

    /**
     * SDK & WebClient 를 통해 admin 유저 권한으로 모든 프로젝트 List를 불러온 후 각 프로젝트의 Quota 정보 리턴.
     * @param dbSaveOpt
     * @return
     */
    @ApiOperation(value = "[Metric] Project Quota,Usage 조회.", notes = "[Metric] SDK & WebClient 를 통해 admin 유저 권한으로 모든 프로젝트 List를 불러온 후 각 프로젝트의 Quota 정보 조회")
    @GetMapping(value = {"/quotalist","/quotalist/{dbSaveOption}"})
    public List<QuotaProjectVo> listAllProjectQuota(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return projectService.getAllProjectQuotaList(dbSaveOpt);
    }


}
