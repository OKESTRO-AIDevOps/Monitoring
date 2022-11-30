package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.repo;

import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.ComputeVmEntity;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.InstancesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ComputeVmRepository extends JpaRepository<ComputeVmEntity, String> {

}
