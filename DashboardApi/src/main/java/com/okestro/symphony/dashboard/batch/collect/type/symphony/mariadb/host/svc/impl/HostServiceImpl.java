package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.repo.ApiHostRepository;
import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.svc.HostService;
import com.okestro.symphony.dashboard.batch.entity.ApiHostEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "SymphonyHostService")
public class HostServiceImpl implements HostService {

//    @Autowired
//    ApiHostRepository apiHostRepository;

    /**
     * SYMPHONY.HOST 조회
     * @return List<ApiHostEntity>
     * */
//    @Override
//    public List<ApiHostEntity> listAllHost() {
//        return apiHostRepository.findAll();
//    }
}
