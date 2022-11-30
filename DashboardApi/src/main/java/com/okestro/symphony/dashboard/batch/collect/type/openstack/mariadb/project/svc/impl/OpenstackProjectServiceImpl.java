package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.project.repo.ApiProjectRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.repo.KeystoneProjectRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.svc.OpenstackProjectService;
import com.okestro.symphony.dashboard.batch.entity.ApiProjectEntity;
import com.okestro.symphony.dashboard.batch.entity.KeystoneProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenstackProjectServiceImpl implements OpenstackProjectService {
//    @Autowired
//    KeystoneProjectRepository keystoneProjectRepository;
//
//    @Autowired
//    ApiProjectRepository apiProjectRepository;

    /**
     * keystone.project 조회
     * @return List<KeystoneProjectEntity>
     * */
//    @Override
//    public List<KeystoneProjectEntity> listAllProject() {
//        return keystoneProjectRepository.findAll();
//    }

    /**
     * SYMPHONY.PROJECT 저장 함수
     * @return void
     * */
//    @Override
//    public void saveProject() {
//
//        List<KeystoneProjectEntity> list = keystoneProjectRepository.findAll();
//        //
//        Map<String, ApiProjectEntity> entityMap = parseProjectEntityResourceMap(list);
//
//        for(String projectId: entityMap.keySet()) {
//            ApiProjectEntity projectEntity = entityMap.get(projectId);
//
//            projectEntity.setCollectDt(new Date());
//
//            // TEST //
//            projectEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//            projectEntity.setProjectName("TEST-0329");
//            apiProjectRepository.save(projectEntity);
//        }
//    }

    /**
     * DbProjectEntity -> ProjectEntity 맵핑하여 저장 Map<keystone.project.id, ApiProjectEntity>를 생성함
     * @return Map<String, ApiProjectEntity>
     * */
//    private Map<String, ApiProjectEntity> parseProjectEntityResourceMap(List<KeystoneProjectEntity> list) {
//        Map<String, ApiProjectEntity> entityMap = new HashMap<>();
//
//        for(KeystoneProjectEntity keystoneProjectEntity : list) {
//            ApiProjectEntity projectEntity = new ApiProjectEntity();
//            projectEntity.setProjectId(keystoneProjectEntity.getId());
//            projectEntity.setProjectName(keystoneProjectEntity.getName());
//
//            entityMap.put(keystoneProjectEntity.getId(), projectEntity);
//        }
//
//        return entityMap;
//    }
}
