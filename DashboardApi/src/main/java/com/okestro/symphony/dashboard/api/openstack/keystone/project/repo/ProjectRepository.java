package com.okestro.symphony.dashboard.api.openstack.keystone.project.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, String> {


}
