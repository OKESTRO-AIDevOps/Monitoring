package com.okestro.symphony.dashboard.api.openstack.dashboard.repo;

import com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity.VHostAggregateHostLastedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VHostAggregateHostLastedRepository extends JpaRepository<VHostAggregateHostLastedEntity, String> {

}
