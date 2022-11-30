///*
// * Developed by sychoi on 2020-07-16
// * Last modified 2020-07-16 14:11:43
// */
//
//package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.ctr;
//
//
//import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.svc.FloatingIpService;
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@CrossOrigin("*")
//@RestController
//@Api(value = "floatingIp", description = "[API] 네트워크 FloatingIp 리스트 (개발중)")
//public class FloatingIpRestController {
//
//    @Autowired
//    private FloatingIpService floatingIpService;
//
//    @RequestMapping(value = "/floatingIp/list", method = RequestMethod.POST)
//    public OpenStackResultVo getRouterList(@RequestBody FloatingIpVo floatingIpVo) {
//        return floatingIpService.retrieveNetFloatingIps(floatingIpVo);
//    }
//
////    @RequestMapping(value = "/floatingIp/connectVmList", method = RequestMethod.POST)
////    public OpenStackResultVo getConnectVmList(@RequestBody FloatingIpVo floatingIpVo) {
////        return floatingIpService.retrieveConnectVmList(floatingIpVo);
////    }
//
////    @RequestMapping(value = "/floatingIp/projectQuotas", method = RequestMethod.POST)
////    public OpenStackResultVo getNetworkQuotas(@RequestBody FloatingIpVo floatingIpVo) {
////        return floatingIpService.getNetworkQuotas(floatingIpVo);
////    }
//}
