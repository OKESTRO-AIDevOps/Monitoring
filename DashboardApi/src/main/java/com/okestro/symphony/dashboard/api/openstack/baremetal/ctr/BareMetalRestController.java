/*
 * Developed by bhhan@okestro.com on 2020-07-17
 * Last modified 2020-07-16 20:34:33
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.ctr;

import com.okestro.symphony.dashboard.api.openstack.baremetal.model.DriverVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.NodeVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.svc.BareMetalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/baremetal")
@RestController
public class BareMetalRestController {
    @Autowired
    BareMetalService bareMetalService;

//    @GetMapping("/hasrole/{projectName}/{userId}")
//    public boolean isHasRole(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return bareMetalService.isHasRole(projectName, userId);
//    }

//    @GetMapping("/nodes/{projectName}/{userId}")
//    public List<NodeVo> retrieveNodes(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return bareMetalService.retrieveNodes(projectName, userId);
//    }

//    @GetMapping("/nodes/{projectName}/{userId}/{uuid}")
//    public NodeVo retrieveNodeDetail(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId, @PathVariable("uuid") String uuid) {
//        return bareMetalService.retrieveNodeDetail(projectName, userId, uuid);
//    }

//    @GetMapping("/drivers/{projectName}/{userId}")
//    public List<DriverVo> retrieveDrivers(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return bareMetalService.retrieveDrivers(projectName, userId);
//    }

}
