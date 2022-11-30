package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NovaapiAggregateMetadataRepository extends JpaRepository<NovaApiAggregatesEntity, Integer> {
}