package com.okestro.symphony.dashboard.api.openstack.dashboard.repo;

import com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity.VHostLastedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VHostLastedRepository extends JpaRepository<VHostLastedEntity, String> {
}
