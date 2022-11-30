package com.okestro.symphony.dashboard.api.openstack.dashboard.repo;

import com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity.VProjectQuotaLastedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VProjectQuotaLastedRepository extends JpaRepository<VProjectQuotaLastedEntity, String> {

}
