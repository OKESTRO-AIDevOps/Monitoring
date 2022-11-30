/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.repo;

import com.okestro.symphony.dashboard.cmm.model.CmpProjectEntity;
import com.okestro.symphony.dashboard.cmm.model.ProjectVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrgProjectRepository {
//public interface OrgProjectRepository extends JpaRepository<CmpProjectEntity, Long> {

//    @Query("SELECT new com.okestro.symphony.dashboard.cmm.model.ProjectVo(p.projectId, p.projectName) FROM CmpProjectEntity p " +
//            "WHERE p.statusCode = :projectStatusCode")
//    List<ProjectVo> retrieveAllProject(String projectStatusCode);
//
//    @Query("SELECT new com.okestro.symphony.dashboard.cmm.model.ProjectVo(p.projectId, p.projectName) FROM CmpProjectEntity p " +
//            "WHERE p.orgCd = :orgCd AND p.statusCode = :projectStatusCode")
//    List<ProjectVo> retrieveOrgProject(String orgCd, String projectStatusCode);
}