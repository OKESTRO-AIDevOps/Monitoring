package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo;

import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregatesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NovaapiAggregatesRepository extends JpaRepository<NovaApiAggregatesEntity, Integer> {

    /**
     * nova_api.aggregates 와 nova_api.aggregate_metadata join + availability_zone 데이터 조회
     * */
    @Query("select a, am.key, am.value from NovaApiAggregatesEntity a left join NovaApiAggregateMetadataEntity am on a.id = am.aggregateId and am.key = 'availability_zone'")
    List<Object> findAllWithMeta();
}