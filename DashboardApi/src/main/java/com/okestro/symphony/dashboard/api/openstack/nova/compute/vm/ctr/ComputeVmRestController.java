//package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.ctr;
//
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.InstancesEntity;
//import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.*;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.svc.ComputeVmService;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.List;
//
//@CrossOrigin("*")
//@Api(value = "compute", description = "[API] compute 조회 리스트")
//@RestController
//public class ComputeVmRestController {
//
//    @Autowired
//    private ComputeVmService vmService;
//
//
//    /**
//     * Webclient[Api]를 통해 가상머신 모두 조회
//     *
//     * @return List<VmVo>
//     */
//    @ApiOperation(value = "[Metric] Webclient[Api]를 통해 가상머신 모두 조회.", notes = "[Metric] Webclient[Api]를 통해 가상머신 모두 조회.")
//    @CrossOrigin("*")
//    @GetMapping(value = {"/wc/vms/retrieveVms","/wc/vms/retrieveVms/{dbSaveOption}"})
//    private List<VmVo> retrieveVmsToWc(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {
//
//        if(dbSaveOpt == null){
//            dbSaveOpt = false;
//        }
//
//        return vmService.retrieveWcVms(dbSaveOpt);
//    }
//
//    /**
//     * SDK[Api]를 통해 가상머신 모두 조회
//     *
//     * @return List<VmVo>
//     */
//    @ApiOperation(value = "[Metric] SDK[Api]를 통해 가상머신 모두 조회.", notes = "[Metric] SDK[Api]를 통해 가상머신 모두 조회. *병렬처리 (Default 스레드 4)")
//    @CrossOrigin("*")
//    @GetMapping(value={"/sdk/vms/retrieveVms","/sdk/vms/retrieveVms/{dbSaveOption}"})
//    private List<VmVo> retrieveVmsToSdk(@PathVariable(value = "dbSaveOption", required = false) Boolean dbSaveOpt) {
//        if(dbSaveOpt == null){
//            dbSaveOpt = false;
//        }
//        return vmService.retrieveVms(dbSaveOpt);
//    }
//
//
//    /**
//     * [Api]가상머신 상세 조회
//     *
//     * @param vm
//     * @return VmVo
//     */
////    @CrossOrigin("*")
////    @PostMapping("/vm/retrieveVm")
////    private VmVo retrieveVm(@RequestBody VmVo vm) {
////        return vmService.retrieveVm(vm);
////    }
//
//    /**
//     * [mariaDB]admin 유저 권한으로 특정기간동안의 모든 vm 리스트 조회.
//     * @param arg
//     * @return
//     */
////    @RequestMapping(value = "/instances_between", method = RequestMethod.POST)
//    public List<InstancesEntity> betweenListAllInstances(@RequestBody HashMap<String, Object> arg) {
//        String sTimeStr = arg.get("sTime").toString();
//        String eTimeStr = arg.get("eTime").toString();
//        LocalDateTime sTime = LocalDateTime.parse(sTimeStr, DateTimeFormatter.ISO_DATE_TIME);
//        LocalDateTime eTime = LocalDateTime.parse(eTimeStr, DateTimeFormatter.ISO_DATE_TIME);
//        System.out.println("## Y_TEST sTime["+sTimeStr+"]");
//
//
//        return vmService.betweenListAllInstances(sTime,eTime);
//    }
//
//    /**
//     * openstack-mariaDB 조회 (target table : nova.instances) -> 사용 못함(openstack mariadb nova.instances 조회용이여서 해당 controller에 맞지 않음)
//     * */
////    @GetMapping("/instances")
////    public List<InstancesEntity> listAllInstances() {
////        return vmService.listAllInstances();
////    }
//
//}
