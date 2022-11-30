package com.okestro.symphony.dashboard.api.openstack.keystone.user.svc;




import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.OstackUserVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.UserProjectVo;
import org.openstack4j.model.identity.v3.Role;

import java.util.List;

public interface UserService {

    /**
     * 모든 사용자 목록 조회
     * @return String(JsonStr)
     */
    public String listAllUser();


    /**
     * 특정 프로젝트 scope 사용자 목록 조회
     * @return List<OstackUserVo>
     */
    public List<OstackUserVo> list(String projectName, String userId);


//    public List<UserProjectVo>  projectList(String projectName, String userId);

//    public List<? extends Role> roleList(String projectName, String userId);
}
