package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiVmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiVmRepository extends JpaRepository<ApiVmEntity, String> {
}
