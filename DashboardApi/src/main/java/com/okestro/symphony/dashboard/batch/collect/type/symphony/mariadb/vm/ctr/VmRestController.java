package com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.ctr;

import com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.svc.VmService;
import com.okestro.symphony.dashboard.batch.entity.ApiProjectQuotaEntity;
import com.okestro.symphony.dashboard.batch.entity.ApiVmEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RequestMapping("/symphony-vm")
//@Api(value = "symphony-vm-rest-controller", description = "[Symphony DB] SYMPHONY.VM 데이터 조회 리스트")
@RestController(value = "SymphonyVmRestController")
public class VmRestController {

    @Autowired
    VmService vmService;

    /**
     * VM 조회
     * @return List<ApiVmEntity>
     * */
  //  @GetMapping("/vm")
//    public List<ApiVmEntity> listAllVm() {
//        return vmService.listAllVm();
//    }
}
