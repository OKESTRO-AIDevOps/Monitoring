package com.okestro.symphony.dashboard.api.openstack.keystone.project.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectQuotaRepository extends JpaRepository<ProjectQuotaEntity, String> {

//    @Query("select *  from PROJECT_QUOTA pq where COLLECT_DT >= DATE_ADD(NOW(), INTERVAL - 7 day) GROUP by PROJECT_ID order by COLLECT_DT DESC")
//    List<ProjectQuotaEntity> findByCollectDtLastData();
}
