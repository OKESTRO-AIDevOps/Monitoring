package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.svc.impl;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.repo.ApiVmRepository;
import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.svc.VmService;
import com.okestro.symphony.dashboard.batch.entity.ApiVmEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "SymphonyVmService")
public class VmServiceImpl implements VmService {

//    @Autowired
//    ApiVmRepository apiVmRepository;

    /**
     * VM 조회
     * @return List<ApiVmEntity>
     * */
//    @Override
//    public List<ApiVmEntity> listAllVm() {
//        return apiVmRepository.findAll();
//    }
}
