package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregateHostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovaapiAggregateHostRepository extends JpaRepository<NovaApiAggregateHostsEntity, Integer> {
//    @Query("select ah from NovaApiAggregateHostsEntity ah where ah.aggregateId")
    List<NovaApiAggregateHostsEntity> findAllByAggregateId(Integer aggregateId);
}
