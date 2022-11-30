package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateHostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiHostAggregateHostRepository extends JpaRepository<ApiHostAggregateHostEntity, Integer> {
}
