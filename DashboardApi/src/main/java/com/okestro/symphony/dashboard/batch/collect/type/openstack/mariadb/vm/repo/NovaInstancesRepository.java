package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaInstancesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovaInstancesRepository extends JpaRepository<NovaInstancesEntity, Integer> {

    @Query("SELECT i from NovaInstancesEntity i where i.vmState = 'active'")
    List<NovaInstancesEntity> findAllActive();

}
