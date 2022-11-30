package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.ctr;


import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model.SecurityGroupVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.svc.SecurityGroupService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "securityGroup", description = "[API] 네트워크 SecurityGroup 리스트 (구현중)")
public class SecurityGroupRestController {

    @Autowired
    SecurityGroupService securityGroupService;

    /**
     * 보안 그룹 조회 (WebClient방식)
     */
    @CrossOrigin("*")
    @GetMapping("/securityGroup/retrieve/webClient/{projectName}/{userId}")
    private OpenStackResultVo getSecurityGroups(@PathVariable("projectName") String projectName ,@PathVariable("userId") String userId) {
        return securityGroupService.getSecurityGroups(projectName ,userId);
    }

    /**
     * 보안 그룹 중복 이름 체크
     *
     * @return
     */
    @CrossOrigin("*")
    @GetMapping("/checkName/{projectName}/{secGroupName}/{userId}")
    private OpenStackResultVo checkDupSecGroupName(@PathVariable("projectName") String projectName ,@PathVariable("secGroupName") String name ,@PathVariable("userId") String userId) {

        return securityGroupService.checkDupSecGroupName(projectName,name,userId);

    }

    /**
     * 보안 그룹 상세
     */

    @CrossOrigin("*")
    @PostMapping("/detail/securityGroup")
    private OpenStackResultVo retrieveSecurityGroup(@RequestBody SecurityGroupVo securityGroupVo){
        return securityGroupService.retrieveSecurityGroup(securityGroupVo);
    }

}
