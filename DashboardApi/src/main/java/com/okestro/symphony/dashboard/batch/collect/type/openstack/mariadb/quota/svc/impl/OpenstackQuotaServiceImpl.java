package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.quota.repo.ApiProjectQuotaRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo.CinderQuotaRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo.NeutronQuotaRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo.NovaApiQuotaRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.svc.OpenstackQuotaService;
import com.okestro.symphony.dashboard.batch.entity.ApiProjectQuotaEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenstackQuotaServiceImpl implements OpenstackQuotaService {

//    @Autowired
//    QuotaRepository quotaRepository;
//    @Autowired //mariaDB
//    ApiProjectQuotaRepository apiProjectQuotaRepository;
//    @Autowired
//    NovaApiQuotaRepository novaApiQuotaRepository;
//    @Autowired
//    CinderQuotaRepository cinderQuotaRepository;
//    @Autowired
//    NeutronQuotaRepository neutronQuotaRepository;

//    @PersistenceContext
//    private EntityManager EM;

//    @Override
//    public List<ComputeNodesEntity> listAllComputeNodes() {
//
//        List<ComputeNodesEntity> result = computeNodesRepository.findAll();
//        // test json parse //
//        for (ComputeNodesEntity entity : result) {
//            if(!"".equals(entity.getCpuInfo())) {
//                Gson gson = new Gson();
//                CpuInfoEntity subEntity = gson.fromJson(entity.getCpuInfo(), CpuInfoEntity.class);
//                System.out.println(subEntity.getModel());
//                System.out.println(subEntity.getFeatures());
//                System.out.println(subEntity.getTopology());
//            }
//            if(!"".equals(entity.getStats())) {
//                Gson gson = new Gson();
//                StatsEntity subEntity = gson.fromJson(entity.getStats(), StatsEntity.class);
//                System.out.println(subEntity.getFailed_builds());
//            }
//        }
//
//        return result;
//    }
//
//    @Override
//    public List<FloatingIpsEntity> listAllFloatingIps() {
//        return floatingIpsRepository.findAll();
//    }

    /**
     * nova_api.quotas 조회
     * @return List<Object>
     * */
//    @Override
//    public List<Object> listAllNovaApiQuota() {
//        List<Object> list = novaApiQuotaRepository.findAllGroupByProjectId();
//        return list;
//    }

    /**
     * cinder.quotas 조회
     * @return List<Object>
     * */
//    @Override
//    public List<Object> listAllCinderQuota() {
//        List<Object> list = cinderQuotaRepository.findAllGroupByProjectId();
//        return list;
//    }

    /**
     * neutron.quotas 조회
     * @return List<Object>
     * */
//    @Override
//    public List<Object> listAllNeutronQuota() {
//        List<Object> list = neutronQuotaRepository.findAllGroupByProjectId();
//        return list;
//    }

    /**
     * SYMPHONY.PROJECT_QUOTA 저장 함수
     * nova_api.quotas, cinder.quotas, neutron.quotas 조회 후 취합하여  SYMPHONY.PROJECT_QUOTA장 에 저
     * @return void
     * */
//    @Override
//    public void saveProjectQuota() {
//        Map<String, ApiProjectQuotaEntity> entityMap = new HashMap<>();
//
//        List<Object> list = novaApiQuotaRepository.findAllGroupByProjectId();
//        Map<String, Map<String, String>> novaApiQuotaMap = parseQuotaResourceList(list);
//        setNovaApiQuota(novaApiQuotaMap, entityMap);
//
//        list = cinderQuotaRepository.findAllGroupByProjectId();
//        Map<String, Map<String, String>> cinderQuotaMap = parseQuotaResourceList(list);
//        setCinderQuota(cinderQuotaMap, entityMap);
//
//        list = neutronQuotaRepository.findAllGroupByProjectId();
//        Map<String, Map<String, String>> neutronQuotaMap = parseQuotaResourceList(list);
//        setNeutronQuota(neutronQuotaMap, entityMap);
//
//        for(String projectId: entityMap.keySet()) {
//            ApiProjectQuotaEntity projectQuotaEntity = entityMap.get(projectId);
//            projectQuotaEntity.setCollectDt(new Date());
//
//            // TEST //
//            projectQuotaEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
////            projectQuotaEntity.setRouterUsage(9122);
//
//            apiProjectQuotaRepository.save(projectQuotaEntity);
//        }
//    }

    /**
     * cinder.quotas, nova_api.quotas, neutron.quotas parse function
     * project_id 별로 자원관련 정보 조회 후 Map 생성
     * @return Map<String, Map<String, String>>
     * */
//    private Map<String, Map<String, String>> parseQuotaResourceList(List<Object> list) {
//        String projectId = null;
//        Map<String, Map<String, String>> quotaMap = new HashMap<>();
////        projectId, Map<String:String> resource-type-name : resource-limit-count
//        for(Object row : list) {
//            Object[] rowObj = (Object[]) row;
//
//            String tempProjectId = rowObj[0].toString();
//            // update project_id
//            if (projectId == null || !(projectId).equals(tempProjectId)) {
//                projectId = tempProjectId;
//                quotaMap.put(projectId, new HashMap<>());
//            }
//
//            int idx = 0;
//            String resourceType = null;
//            String resourceValue = null;
//            Map<String, String> quotaByRowMap = quotaMap.get(projectId);
//            for(; idx<rowObj.length; idx++) {
//                if (idx == 1) {
//                    resourceType = rowObj[idx].toString();
//                } else if (idx == 2) {
//                    resourceValue = rowObj[idx].toString();
//                }
//            }
//            quotaByRowMap.put(resourceType, resourceValue);
//        }
//        return quotaMap;
//    }

    /**
     * nova_api.quotas에서 project_id별로 자원 관련 정보 조회 후, ApiProjectQuotaEntity 생성
     * @return null
     * */
//    private void setNovaApiQuota(Map<String, Map<String, String>> novaQuotaMap, Map<String, ApiProjectQuotaEntity> entityMap) {
//        for (String projectId : novaQuotaMap.keySet()) {
//            Map<String, String> resourceMap = novaQuotaMap.get(projectId);
//
////            ProjectQuotaEntity projectQuotaEntity = entityMap.get(projectId);
//            ApiProjectQuotaEntity projectQuotaEntity = setProjectQuotaEntity(entityMap.get(projectId), projectId);
//
//            // Cannot add or update a child row: a foreign key constraint fails (`SYMPHONY`.`PROJECT_QUOTA`, CONSTRAINT `FK_PROJECT_QUOTA_PROVIDER_ID` FOREIGN KEY (`PROVIDER_ID`) REFERENCES `PROVIDER` (`PROVIDER_ID`))
//            projectQuotaEntity.setCpuQuota(checkIntValue(resourceMap.get("cores")));
//            projectQuotaEntity.setInstanceQuota(checkIntValue(resourceMap.get("instances")));
//            projectQuotaEntity.setRamQuota(checkIntValue(resourceMap.get("ram")));
//
//            entityMap.put(projectId, projectQuotaEntity);
//        }
//    }

    /**
     * cinder.quotas에서 project_id별로 자원 관련 정보 조회 후, ApiProjectQuotaEntity 생성
     * @return null
     * */
//    private void setCinderQuota(Map<String, Map<String, String>> cinderQuotaMap, Map<String, ApiProjectQuotaEntity> entityMap) {
//        for (String projectId : cinderQuotaMap.keySet()) {
//            Map<String, String> resourceMap = cinderQuotaMap.get(projectId);
//
//            ApiProjectQuotaEntity projectQuotaEntity = setProjectQuotaEntity(entityMap.get(projectId), projectId);
//
//            projectQuotaEntity.setSnapshotQuota(checkIntValue(resourceMap.get("snapshots")));
//            projectQuotaEntity.setStorageQuota(checkIntValue(resourceMap.get("gigabytes")));
//            projectQuotaEntity.setVolumeQuota(checkIntValue(resourceMap.get("volumes")));
//
//            entityMap.put(projectId, projectQuotaEntity);
//        }
//    }

    /**
     * neutron.quotas에서 project_id별로 자원 관련 정보 조회 후, ApiProjectQuotaEntity 생성
     * @return null
     * */
//    private void setNeutronQuota(Map<String, Map<String, String>> neutronQuotaMap, Map<String, ApiProjectQuotaEntity> entityMap) {
//        for (String projectId : neutronQuotaMap.keySet()) {
//            Map<String, String> resourceMap = neutronQuotaMap.get(projectId);
//
//            ApiProjectQuotaEntity projectQuotaEntity = setProjectQuotaEntity(entityMap.get(projectId), projectId);
//
//            projectQuotaEntity.setNetworkQuota(checkIntValue(resourceMap.get("network")));
//            projectQuotaEntity.setFloatingIpQuota(checkIntValue(resourceMap.get("floatingip")));
//            projectQuotaEntity.setSecurityGroupQuota(checkIntValue(resourceMap.get("security_group")));
//            projectQuotaEntity.setSecurityRuleQuota(checkIntValue(resourceMap.get("security_group_rule")));
//            projectQuotaEntity.setPortQuota(checkIntValue(resourceMap.get("port")));
//            projectQuotaEntity.setRouterQuota(checkIntValue(resourceMap.get("router")));
//
//            entityMap.put(projectId, projectQuotaEntity);
//        }
//    }

    /**
     * nova_api.quotas, cinder.quotas, neutron.quotas 조회 후 취합하여 Map<projectId, ApiProjectQuotaEntity> 생성
     * @return ApiProjectQuotaEntity
     * */
//    private ApiProjectQuotaEntity setProjectQuotaEntity(ApiProjectQuotaEntity projectQuotaEntity, String projectId) {
//        if (projectQuotaEntity == null) {
//            projectQuotaEntity = new ApiProjectQuotaEntity();
//            projectQuotaEntity.setProjectId(projectId);
//        }
//        return projectQuotaEntity;
//    }

    /**
     * insert 테스트 중 null exception 이 발생해서 만들었던 걸로 기억.. > 확인필요
     * */
//    private Integer checkIntValue(String value) {
//        return (value == null || value.trim().equals("")) ? 0 : Integer.valueOf(value);
//    }
}
