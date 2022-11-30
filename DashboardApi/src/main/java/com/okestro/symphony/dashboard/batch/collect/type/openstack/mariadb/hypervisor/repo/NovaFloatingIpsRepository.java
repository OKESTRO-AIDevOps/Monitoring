package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaFloatingIpsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovaFloatingIpsRepository extends JpaRepository<NovaFloatingIpsEntity, Integer>{
}