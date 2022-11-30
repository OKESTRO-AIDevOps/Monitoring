package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaComputeNodesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovaHostRepository extends JpaRepository<NovaComputeNodesEntity, Integer>{
//    @Query("SELECT c from ComputeNodesEntity c")
//    List<Object> findAllGroupByProjectId();
}