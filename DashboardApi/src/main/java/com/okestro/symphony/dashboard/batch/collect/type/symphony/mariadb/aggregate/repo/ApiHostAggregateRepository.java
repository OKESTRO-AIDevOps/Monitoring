package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiHostAggregateRepository extends JpaRepository<ApiHostAggregateEntity, String> {
}