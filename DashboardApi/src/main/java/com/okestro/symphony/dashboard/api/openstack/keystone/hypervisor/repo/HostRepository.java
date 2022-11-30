package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.repo;

import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HostRepository extends JpaRepository<HostEntity, String> {
//    @Query("select *  from HOST h where COLLECT_DT >= DATE_ADD(NOW(), INTERVAL - 7 day) GROUP by HOST_ID order by COLLECT_DT DESC")
//    List<HostEntity> findByCollectDtLastData();

//    HostEntity findByCollectedDtBetween(Date start, Date end);

}
