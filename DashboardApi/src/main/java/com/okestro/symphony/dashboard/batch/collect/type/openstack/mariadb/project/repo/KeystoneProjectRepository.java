package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.repo;

import com.okestro.symphony.dashboard.batch.entity.KeystoneProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeystoneProjectRepository extends JpaRepository<KeystoneProjectEntity, String>{
}