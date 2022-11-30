package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiHostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiHostRepository extends JpaRepository<ApiHostEntity, String> {
}
