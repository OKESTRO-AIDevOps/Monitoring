package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaApiQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovaApiQuotaRepository extends JpaRepository<NovaApiQuotaEntity, String> {

    @Query("SELECT q.projectId, q.resource, q.hardLimit from NovaApiQuotaEntity q group by q.projectId, q.resource, q.hardLimit")
    List<Object> findAllGroupByProjectId();


//    List<Object> findAllByProjectId(String projectId);

//    @Query("SELECT q.resource, q.hardLimit from QuotaEntity q where 1=1 and q.projectId = :projectId")
//    List<Object> findAllByProjectId(String projectId);


}
