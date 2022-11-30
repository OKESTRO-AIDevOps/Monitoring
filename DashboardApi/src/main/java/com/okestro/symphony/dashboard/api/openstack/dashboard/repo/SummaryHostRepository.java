package com.okestro.symphony.dashboard.api.openstack.dashboard.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryHostRepository extends JpaRepository<HostEntity, String> {

}
