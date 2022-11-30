package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.quota.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiProjectQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiProjectQuotaRepository extends JpaRepository<ApiProjectQuotaEntity, String> {
}
