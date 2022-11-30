package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostAggregateHostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HostAggregateHostRepository extends JpaRepository<HostAggregateHostEntity, String> {
}
