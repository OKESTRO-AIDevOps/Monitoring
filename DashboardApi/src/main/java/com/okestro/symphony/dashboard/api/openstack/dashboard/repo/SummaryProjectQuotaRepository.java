package com.okestro.symphony.dashboard.api.openstack.dashboard.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectQuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryProjectQuotaRepository extends JpaRepository<ProjectQuotaEntity, String> {

}
