package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.repo.ApiVmRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.repo.NovaInstancesRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.svc.OpenstackVmService;
import com.okestro.symphony.dashboard.batch.entity.NovaInstancesEntity;
import com.okestro.symphony.dashboard.batch.entity.ApiVmEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OpenstackVmServiceImpl implements OpenstackVmService {
//    @Autowired
//    NovaInstancesRepository novaInstancesRepository;
//    @Autowired
//    ApiVmRepository apiVmRepository;

    /**
     * vm.instances 조회
     * @return List<NovaInstancesEntity>
     * */
//    @Override
//    public List<NovaInstancesEntity> listAllInstances() {
//        return novaInstancesRepository.findAll();
//    }

    /**
     * vm.instances vm_state=active 만 조회
     * @return List<NovaInstancesEntity>
     * */
//    @Override
//    public List<NovaInstancesEntity> listAllInstancesActive() {
//        return novaInstancesRepository.findAllActive();
//    }

    /**
     *  vm_state = 'active' 만 target
     * SYMPHONY.VM 저장 함수 + saveAll() 테스트 함수
     * @return void
     * */
//    @Override
//    public void saveVm() {
//
//        List<NovaInstancesEntity> list = novaInstancesRepository.findAllActive();
//
//        // InstancesEntity -> ApiVmEntity
//        // 1. 일반 repo.save() 함수 사용한 것
////        Map<String, ApiVmEntity> entityMap = parseInstancesResourceMap(list);
////
////        for(String instancesId: entityMap.keySet()) {
////            ApiVmEntity apiVmEntity = entityMap.get(instancesId);
////
////            apiVmEntity.setCollectDt(new Date());
////
////            // TEST //
////            apiVmEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
////            apiVmRepository.save(apiVmEntity);
////        }
//
//        // 2. repo.saveAll() 함수 사용한 것
//        List<ApiVmEntity> entityList = parseInstancesResourceList(list);
//        apiVmRepository.saveAll(entityList);
//
//    }

    /**
     * nova.instances에서 vm_state = 'active' 인 데이터만 저장하는 함수
     * InstancesEntity -> ApiVmEntity
     * @return Map<String, ApiVmEntity>
     * */
//    private Map<String, ApiVmEntity> parseInstancesResourceMap(List<NovaInstancesEntity> list) {
//        Map<String, ApiVmEntity> entityMap = new HashMap<>();
//        for(NovaInstancesEntity novaInstancesEntity : list) {
//            ApiVmEntity apiVmEntity = new ApiVmEntity();
//
//            System.out.println(novaInstancesEntity.toString());
//
//            if (novaInstancesEntity.getAvailabilityZone() != null) {
//                apiVmEntity.setAzName(novaInstancesEntity.getAvailabilityZone());
//            }
//            apiVmEntity.setVmName(novaInstancesEntity.getDisplayName());
//            apiVmEntity.setHostId(novaInstancesEntity.getHost());
//
//            // SYMPHONY.VM 의 HOST_NAME char(18) 제한 반영함
//            int hostNameIdx = novaInstancesEntity.getHostname().length();
//            hostNameIdx = (hostNameIdx > 18) ? 18 : hostNameIdx;
//            apiVmEntity.setHostName(novaInstancesEntity.getHostname().substring(0,hostNameIdx));
//            apiVmEntity.setProviderId(novaInstancesEntity.getProjectId());
//            apiVmEntity.setVmId(novaInstancesEntity.getUuid());
//            apiVmEntity.setCreateDt(novaInstancesEntity.getCreatedAt());
//
//            // added @ 210330
//            apiVmEntity.setRam(novaInstancesEntity.getMemoryMb());
//            apiVmEntity.setPowerState((novaInstancesEntity.getPowerState() == null) ? "0" : String.valueOf(novaInstancesEntity.getPowerState())); //default = 0 인거 확인 필요!
//            apiVmEntity.setDisk(novaInstancesEntity.getRootGb());
//            apiVmEntity.setCpu(novaInstancesEntity.getVcpus());
//            apiVmEntity.setVmState(novaInstancesEntity.getVmState());
//
//            entityMap.put(novaInstancesEntity.getId().toString(), apiVmEntity);
//        }
//
//        return entityMap;
//    }

    /**
     * nova.instances에서 vm_state = 'active' 인 데이터만 저장하는 함수
     * InstancesEntity -> ApiVmEntity
     * @return List<ApiVmEntity>
     * */
//    private List<ApiVmEntity> parseInstancesResourceList(List<NovaInstancesEntity> list) {
//        Map<String, ApiVmEntity> entityMap = new HashMap<>();
//        List<ApiVmEntity> entityList = new ArrayList<>();
//
//        for(NovaInstancesEntity novaInstancesEntity : list) {
//            ApiVmEntity apiVmEntity = new ApiVmEntity();
//
//            System.out.println(novaInstancesEntity.toString());
//
//            if (novaInstancesEntity.getAvailabilityZone() != null) {
//                apiVmEntity.setAzName(novaInstancesEntity.getAvailabilityZone());
//            }
//            apiVmEntity.setVmName(novaInstancesEntity.getDisplayName());
//            apiVmEntity.setHostId(novaInstancesEntity.getHost());
//
//            // SYMPHONY.VM 의 HOST_NAME char(18) 제한 반영함
//            int hostNameIdx = novaInstancesEntity.getHostname().length();
//            hostNameIdx = (hostNameIdx > 18) ? 18 : hostNameIdx;
//            apiVmEntity.setHostName(novaInstancesEntity.getHostname().substring(0,hostNameIdx));
//            apiVmEntity.setProviderId(novaInstancesEntity.getProjectId());
//            apiVmEntity.setVmId(novaInstancesEntity.getUuid());
//            apiVmEntity.setCreateDt(novaInstancesEntity.getCreatedAt());
//
//            // added @ 210330
//            apiVmEntity.setRam(novaInstancesEntity.getMemoryMb());
//            apiVmEntity.setPowerState((novaInstancesEntity.getPowerState() == null) ? "0" : String.valueOf(novaInstancesEntity.getPowerState())); //default = 0 인거 확인 필요!
//            apiVmEntity.setDisk(novaInstancesEntity.getRootGb());
//            apiVmEntity.setCpu(novaInstancesEntity.getVcpus());
//            apiVmEntity.setVmState(novaInstancesEntity.getVmState());
//
//            apiVmEntity.setCollectDt(new Date());
//            apiVmEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//            entityList.add(apiVmEntity);
//        }
//
//        return entityList;
//    }
}
