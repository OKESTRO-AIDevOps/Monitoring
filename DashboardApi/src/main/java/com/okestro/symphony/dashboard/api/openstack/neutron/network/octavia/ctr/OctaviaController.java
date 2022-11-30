//package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.ctr;
//
//import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
//import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.LoadBalancerVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.MemberVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.PoolVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.svc.OctaviaService;
//import io.swagger.annotations.Api;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@CrossOrigin("*")
////@Api(value = "octavia", description = "Octavia 로드밸런서 API 리스트")
//public class OctaviaController {
//
//    @Autowired
//    @Qualifier("octaviaServiceImpl")
//    OctaviaService octaviaService;
//
//    /**
//     * 로드밸런서 리스트를 조회한다.
//     */
////    @RequestMapping(value = "/octavia/list", method = RequestMethod.POST)
////    public OpenStackResultVo getLoadBalancerList(@RequestBody LoadBalancerVo loadBalancerVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.getLoadBalancerList(loadBalancerVo.getProjectName(), loadBalancerVo.getUserId()));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//    /**
//     * 로드밸런서 상세
//     *
//     * @param loadBalancerVo
//     * @return
//     */
////    @RequestMapping(value = "/octavia/detail", method = RequestMethod.POST)
////    public OpenStackResultVo getLoadBalancerDetail(@RequestBody LoadBalancerVo loadBalancerVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.getLoadBalancerDetail(loadBalancerVo));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//
//    /**f
//     * 로드밸런서 리스너 조회
//     *
//     * @param loadBalancerVo
//     * @return
//     */
////    @RequestMapping(value = "/octavia/detail/listener", method = RequestMethod.POST)
////    public OpenStackResultVo getListeneList(@RequestBody LoadBalancerVo loadBalancerVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.getListenerList(loadBalancerVo.getId(), loadBalancerVo.getProjectName(), loadBalancerVo.getUserId()));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//    /**
//     * 풀 리스트 조회
//     *
//     * @param poolVo
//     * @return
//     */
////    @RequestMapping(value = "/octavia/pool", method = RequestMethod.POST)
////    public OpenStackResultVo getPoolList(@RequestBody PoolVo poolVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.getPoolList(poolVo));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//
//    /**
//     * 맴버 리스트 상세 조회
//     *
//     * @param memberVoList
//     * @return
//     */
////    @RequestMapping(value = "/octavia/detail/members", method = RequestMethod.POST)
////    public OpenStackResultVo getMemberDetail(@RequestBody List<MemberVo> memberVoList) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.getMemberDetail(memberVoList));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//    /**
//     * 포트 중복 체크
//     */
////    @RequestMapping(value = "/octavia/protocolPort/duplicated", method = RequestMethod.POST)
////    public OpenStackResultVo duplicatedProtocolPort(@RequestBody LoadBalancerVo loadBalancerVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        try {
////            openStackResultVo.setViewList(octaviaService.duplicatedProtocolPort(loadBalancerVo));
////            openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////        } catch (Exception e) {
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(e.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//}
