package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.ctr;


import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.svc.HypervisorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Api(value = "hypervisor", description = "[API] Hypervisor 리스트")
@RequestMapping("/hypervisor")
@RestController
public class HypervisorRestController {

    @Autowired
    HypervisorService hypervisorService;

    /**
     * SDK를 통해 admin 유저 권한으로 모든 Hypervisor 리스트 받아옴.
     * @return HostVo
     */
    @ApiOperation(value = "[Meta] Hypervisor 리스트 조회.", notes = "[Metric] SDK를 통해 admin 유저 권한으로 모든 Hypervisor 리스트 받아옴.")
    @GetMapping(value = {"/list","/list/{dbSaveOption}"})
    public List<HostVo> listAllToSdk(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return hypervisorService.listAllHypervisor(dbSaveOpt);
    }

    /**
     * Webclient를 통해 admin 유저 권한으로 모든 Hypervisor 리스트 받아옴.
     * @return HostVo
     */
//    @GetMapping(value = {"/wc/list","/wc/list/{dbSaveOption}"})
    public List<HostVo> listAllToWc(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {

        if(dbSaveOpt == null){
            dbSaveOpt = false;
        }

        return hypervisorService.listWcAllHypervisor(dbSaveOpt);
    }

//    @GetMapping("/list/{projectName}/{userId}")
//    public List<HostVo> list(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return hypervisorService.list(projectName, userId);
//    }


}
