package com.okestro.symphony.dashboard.api.openstack.keystone.user.ctr;


import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.OstackUserVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.UserProjectVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.svc.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.openstack4j.model.identity.v3.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@Api(value = "openstackUser", description = "[API] OpenStack 유저 리스트")
@RequestMapping("/user")
@RestController
public class UserRestController {
    @Autowired
    UserService userService;

    /**
     * admin 권한으로 모든 유저 List 조회.
     * @return String
     */
    @ApiOperation(value = "[Meta] User 조회.", notes = "[Meta] SDK admin 권한으로 모든 유저 List 조회")
    @GetMapping("/list")
    public String listAll() {
        return userService.listAllUser();
    }

//    @GetMapping("/list/{projectName}/{userId}")
//    public List<OstackUserVo> list(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return userService.list(projectName, userId);
//    }
//
//    @GetMapping("/list/projects/{projectName}/{userId}")
//    public List<UserProjectVo>  projectList(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return userService.projectList(projectName, userId);
//    }
//
//    @GetMapping("/list/roles/{projectName}/{userId}")
//    public List<? extends Role> roleList(@PathVariable("projectName") String projectName, @PathVariable("userId") String userId) {
//        return userService.roleList(projectName, userId);
//    }


}
