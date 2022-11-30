package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.repo.ApiHostRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.repo.NovaHostRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.svc.OpenstackHostService;
import com.okestro.symphony.dashboard.batch.entity.ApiHostEntity;
import com.okestro.symphony.dashboard.batch.entity.NovaComputeNodesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OpenstackHostServiceImpl implements OpenstackHostService {

//    @Autowired
//    NovaHostRepository novaHostRepository;
//    @Autowired
//    ApiHostRepository apiHostRepository;

    /**
     * nova.compute_nodes 조회
     * @return List<NovaComputeNodesEntity>
     * */
//    @Override
//    public List<NovaComputeNodesEntity> listAllComputeNodes() {
//        return novaHostRepository.findAll();
//    }

    /**
     * SYMPHONY.HOST 저장 함수
     * @return void
     * */
//    @Override
//    public void saveHost() {
//        List<NovaComputeNodesEntity> list = novaHostRepository.findAll();
//
//        // ComputeNodesEntity -> HostEntity
//        Map<String, ApiHostEntity> entityMap = parseComputeNodesResourceMap(list);
//
//        for(String hostId: entityMap.keySet()) {
//            ApiHostEntity hostEntity = entityMap.get(hostId);
//
//            hostEntity.setCollectDt(new Date());
//
//            // TEST //
//            hostEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//            apiHostRepository.save(hostEntity);
//        }
//    }

    /**
     * nova.compute_nodes -> SYMPHONY.HOST 맵핑 Map<nova.compute_nodes.id, ApiHostEntity>를 생성함
     * @return Map<String, ApiHostEntity>
     * */
//    private Map<String, ApiHostEntity> parseComputeNodesResourceMap(List<NovaComputeNodesEntity> list) {
//        Map<String, ApiHostEntity> entityMap = new HashMap<>();
//
//        for(NovaComputeNodesEntity novaComputeNodesEntity : list) {
//            ApiHostEntity hostEntity = new ApiHostEntity();
//
//            hostEntity.setHostId(novaComputeNodesEntity.getId().toString());
//            hostEntity.setHostName(novaComputeNodesEntity.getHost());
//            hostEntity.setLocalStorageTotal(novaComputeNodesEntity.getLocalGb());
//            hostEntity.setLocalStorageUsage((novaComputeNodesEntity.getLocalGbUsed()));
//            hostEntity.setRamTotal(novaComputeNodesEntity.getMemoryMb());
//            hostEntity.setRamUsage(novaComputeNodesEntity.getMemoryMbUsed());
//            hostEntity.setVcpuTotal(novaComputeNodesEntity.getVcpus());
//            hostEntity.setVcpuUsage(novaComputeNodesEntity.getVcpusUsed());
//
//            entityMap.put(novaComputeNodesEntity.getId().toString(), hostEntity);
//        }
//
//        return entityMap;
//    }
}
