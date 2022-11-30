package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo;

import com.okestro.symphony.dashboard.batch.entity.NeutronQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NeutronQuotaRepository extends JpaRepository<NeutronQuotaEntity, String> {
    @Query("SELECT q.projectId, q.resource, q.hardLimit from NeutronQuotaEntity q group by q.projectId, q.resource, q.hardLimit")
    List<Object> findAllGroupByProjectId();
}
