package com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.repo.ApiHostAggregateHostRepository;
import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.repo.ApiHostAggregateRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo.NovaapiAggregateHostRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo.NovaapiAggregatesRepository;
import com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.svc.OpenstackAggregateHostService;
import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateEntity;
import com.okestro.symphony.dashboard.batch.entity.ApiHostAggregateHostEntity;
import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregateHostsEntity;
import com.okestro.symphony.dashboard.batch.entity.NovaApiAggregatesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OpenstackAggregateHostServiceImpl implements OpenstackAggregateHostService {

//    @Autowired
//    NovaapiAggregatesRepository novaapiAggregatesRepository;
//    @Autowired
//    NovaapiAggregateHostRepository novaapiAggregateHostRepository;
//    @Autowired
//    ApiHostAggregateRepository apiHostAggregateRepository;
//    @Autowired
//    ApiHostAggregateHostRepository apiHostAggregateHostRepository;

    /**
     * nova_api.aggregates 조회
     * @return List<NovaApiAggregatesEntity>
     * */
//    @Override
//    public List<NovaApiAggregatesEntity> listAllAggregates() {
//        return novaapiAggregatesRepository.findAll();
//    }

    /**
     * nova_api.aggregate_host 조회
     * @return List<NovaApiAggregateHostsEntity>
     * */
//    @Override
//    public List<NovaApiAggregateHostsEntity> listAllAggregateHost() {
//        return novaapiAggregateHostRepository.findAll();
//    }

    /**
     * SYMPHONY.HOST_AGGREGATE 저장
     * @return void
     * */
//    @Override
//    public void saveHostAggregate() {
//        List<Object> list = novaapiAggregatesRepository.findAllWithMeta();
//        Map<Integer, NovaApiAggregatesEntity> aggreatesMap = parseAggregateMetaResourceToMap(list);
//
//        for(Integer aggregatesId : aggreatesMap.keySet()) {
//            ApiHostAggregateEntity hostAggregateEntity = new ApiHostAggregateEntity();
//            NovaApiAggregatesEntity novaApiAggregatesEntity = aggreatesMap.get(aggregatesId);
//
//            hostAggregateEntity.setHostAggregateId(novaApiAggregatesEntity.getId().toString());
//            hostAggregateEntity.setHostAggregateName(novaApiAggregatesEntity.getName());
//            hostAggregateEntity.setAzName(novaApiAggregatesEntity.getAzZone());
//            hostAggregateEntity.setCollectDt(new Date());
//
//            // TEST
//            hostAggregateEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//
//            apiHostAggregateRepository.save(hostAggregateEntity);
//        }
//    }

    /**
     * SYMPHONY.HOST_AGGREGATE_HOST 저장 (이를 위해 HOST_AGGREGATE 데이터 먼저 저장 후, HOST_AGGREGATE_HOST 저장함)
     * @return void
     * */
//    @Override
//    public void saveHostAggregateHost() {
//        List<ApiHostAggregateEntity> resultHostAggregateEntityList = new ArrayList<>();
//        List<Object> listMeta = novaapiAggregatesRepository.findAllWithMeta();
//        Map<Integer, NovaApiAggregatesEntity> aggreatesMap = parseAggregateMetaResourceToMap(listMeta);
//
//        for(Integer aggregatesId : aggreatesMap.keySet()) {
//            ApiHostAggregateEntity hostAggregateEntity = new ApiHostAggregateEntity();
//            NovaApiAggregatesEntity novaApiAggregatesEntity = aggreatesMap.get(aggregatesId);
//
//            hostAggregateEntity.setHostAggregateId(novaApiAggregatesEntity.getId().toString());
//            hostAggregateEntity.setHostAggregateName(novaApiAggregatesEntity.getName());
//            hostAggregateEntity.setAzName(novaApiAggregatesEntity.getAzZone());
//            hostAggregateEntity.setCollectDt(new Date());
//
//            // TEST
//            hostAggregateEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//
//            apiHostAggregateRepository.save(hostAggregateEntity);
//            resultHostAggregateEntityList.add(hostAggregateEntity);
//        }
//
//        List<NovaApiAggregateHostsEntity> list2 = novaapiAggregateHostRepository.findAll();
//
//        for(ApiHostAggregateEntity hostAggregateEntity : resultHostAggregateEntityList) {
//            System.out.println("hostAggregateEntity.getHostAggregateId() = " + hostAggregateEntity.getHostAggregateId());
//            List<NovaApiAggregateHostsEntity> listByAggregateId = novaapiAggregateHostRepository.findAllByAggregateId(Integer.valueOf(hostAggregateEntity.getHostAggregateId()));
//            ApiHostAggregateHostEntity hostAggregateHostEntity = null;
//            for(NovaApiAggregateHostsEntity host : listByAggregateId) {
//                hostAggregateHostEntity = new ApiHostAggregateHostEntity();
//                hostAggregateHostEntity.setHostId(host.getId().toString());
//                hostAggregateHostEntity.setHostAggregateId(hostAggregateEntity.getHostAggregateId());
//                hostAggregateHostEntity.setCollectDt(hostAggregateEntity.getCollectDt());
//                hostAggregateHostEntity.setHostName(host.getHost());
//
//                hostAggregateHostEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//
//                apiHostAggregateHostRepository.save(hostAggregateHostEntity);
//            }
//        }
//    }

    /**
     * nova_api.aggregate_meta.key = 'availability_zone' 의 value 조회 후, nova_api.aggregates.id 별로 NovaApiAggregatesEntity 생성
     * Map<nova_api.aggregates.id, new NovaApiAggregatesEntity()>
     * @return Map<Integer, NovaApiAggregatesEntity>
     * */
//    private Map<Integer, NovaApiAggregatesEntity> parseAggregateMetaResourceToMap(List<Object> list) {
//        Map<Integer, NovaApiAggregatesEntity> quotaMap = new HashMap<>();
//
//        for(Object row : list) {
//            Object[] rowObj = (Object[]) row;
//            NovaApiAggregatesEntity aggregatesEntity = (NovaApiAggregatesEntity)rowObj[0];
//            // AZ_ZONE data check
//            String key = (rowObj[1] == null) ? null : rowObj[1].toString();
//            String value = (rowObj[2] == null) ? null : rowObj[2].toString();
//
//            if (key != null && key.equals("availability_zone")) {
//                aggregatesEntity.setAzZone(value);
//            }
//            quotaMap.put(aggregatesEntity.getId(), aggregatesEntity);
//        }
//        return quotaMap;
//    }
}
