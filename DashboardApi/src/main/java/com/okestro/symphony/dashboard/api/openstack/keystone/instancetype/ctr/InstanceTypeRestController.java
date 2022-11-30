package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.ctr;

import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.svc.InstanceTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "flavor", description = "[API] Flavor 리스트")
@RequestMapping("/flavor")
public class InstanceTypeRestController {

    @Autowired
    InstanceTypeService instanceTypeService;


    /**
     * admin 권한으로 모든 flavors 리스트 추출.
     *
     * @return List<FlavorVo>
     */
    @ApiOperation(value = "[Meta] flavors 리스트 조회.", notes = "[Metric] SDK를 통해 admin 유저 권한으로 모든 flavors 리스트 받아옴.")
    @CrossOrigin("*")
    @GetMapping("/retrieve")
    private List<FlavorVo> retrieveInstanceTypes() {

        return instanceTypeService.retrieveAllInstanceTypes();
    }

//    /**
//     * find all type of flavors
//     *
//     * @return List<FlavorVo>
//     */
//    @CrossOrigin("*")
//    @GetMapping("/retrieve/{projectName}/{userId}")
//    private List<FlavorVo> retrieveInstanceTypes(@PathVariable("projectName") String projectName ,@PathVariable("userId") String userId) {
//
//        return instanceTypeService.retrieveInstanceTypes(projectName , userId);
//    }


}
