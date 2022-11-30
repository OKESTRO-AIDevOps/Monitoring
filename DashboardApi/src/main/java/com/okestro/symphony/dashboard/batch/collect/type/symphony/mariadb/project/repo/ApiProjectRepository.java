package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.project.repo;

import com.okestro.symphony.dashboard.batch.entity.ApiProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ApiProjectRepository extends JpaRepository<ApiProjectEntity, String>{
}