/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.engine;

import com.okestro.symphony.dashboard.cmm.model.SimpleProjectVo;
import com.okestro.symphony.dashboard.cmm.model.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OpenStackConnectionController {
    @Autowired
    OpenStackConnectionService openStackConnectionService;

//    /**
//     * 프로젝트 목록 조회 - 포털 관리자
//     *
//     * @return List<ProjectVo>
//     */
//    @CrossOrigin("*")
//    @PostMapping("/retrieveAllProjectList")
//    private List<SimpleProjectVo> retrieveAllProjectList() {
//        return openStackConnectionService.retrieveAllProjectList();
//    }

//    /**
//     * 프로젝트 목록 조회 - 입주기관 관리자
//     *
//     * @return List<ProjectVo>
//     */
//    @CrossOrigin("*")
//    @PostMapping("/retrieveOrgProjectList")
//    private List<SimpleProjectVo> retrieveOrgProjectList(@RequestBody UserVo user) {
//        return openStackConnectionService.retrieveOrgProjectList(user.getOrgCd());
//    }

//    /**
//     * 프로젝트 목록 조회 - 사용자
//     *
//     * @return List<ProjectVo>
//     */
//    @CrossOrigin("*")
//    @PostMapping("/retrieveProjectList")
//    private List<SimpleProjectVo> retrieveProjectList(@RequestBody UserVo user) {
//        return openStackConnectionService.retrieveProjectList(user.getUserId());
//    }
}
