package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.ctr;


import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.NetworkVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.svc.NetworkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@Api(value = "network", description = "[API] 네트워크 리스트")
public class NetworkRestController {

    @Autowired
    NetworkService networkService;


    /**
     * 네트워크 리스트를 조회한다.
     * @return
     */
    @ApiOperation(value = "[Meta] network 리스트 조회.", notes = "[Metric] SDK를 통해 admin 유저 권한으로 모든 network 리스트 받아옴.")
    @GetMapping(value = "/network/list")
    public OpenStackResultVo getNetworkList() {
        OpenStackResultVo oepnStackResultVo = new OpenStackResultVo();
        try {
            oepnStackResultVo.setViewList(networkService.NetworkListAll());
            oepnStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
        } catch (Exception e) {
            oepnStackResultVo.setResultCode(OpenStackConstant.ERROR);
            oepnStackResultVo.setResultMessage(e.getMessage());
        }

        return oepnStackResultVo;
    }

    /**
     * 네트워크 리스트를 조회한다.
     */
//    @RequestMapping(value = "/network/list", method = RequestMethod.POST)
//    public OpenStackResultVo getNetworkList(@RequestBody CommonVo commonVo) {
//        OpenStackResultVo oepnStackResultVo = new OpenStackResultVo();
//        try {
//            oepnStackResultVo.setViewList(networkService.networkList(commonVo));
//            oepnStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
//        } catch (Exception e) {
//            oepnStackResultVo.setResultCode(OpenStackConstant.ERROR);
//            oepnStackResultVo.setResultMessage(e.getMessage());
//        }
//
//        return oepnStackResultVo;
//    }


    /**
     * 네트워크 하나 조회
     *
     * @param networkVo
     * @return
     */
//    @RequestMapping(value = "/network/getNetwork", method = RequestMethod.POST)
//    public OpenStackResultVo getNetwork(@RequestBody NetworkVo networkVo) {
//        OpenStackResultVo oepnStackResultVo = new OpenStackResultVo();
//        try {
//            oepnStackResultVo.setViewList(networkService.getNetwork(networkVo, true));
//            oepnStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
//        } catch (Exception e) {
//            oepnStackResultVo.setResultCode(OpenStackConstant.ERROR);
//            oepnStackResultVo.setResultMessage(e.getMessage());
//        }
//
//        return oepnStackResultVo;
//    }

    /**
     * 포트 리스트 조회
     *
     * @param networkVo
     * @return
     */
//    @RequestMapping(value = "/network/portList", method = RequestMethod.POST)
//    public OpenStackResultVo getPorts(@RequestBody NetworkVo networkVo) {
//
//        OpenStackResultVo oepnStackResultVo = new OpenStackResultVo();
//        try {
//            oepnStackResultVo.setViewList(networkService.getNetworkPortList(networkVo));
//            oepnStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
//        } catch (Exception e) {
//            oepnStackResultVo.setResultCode(OpenStackConstant.ERROR);
//            oepnStackResultVo.setResultMessage(e.getMessage());
//        }
//
//        return oepnStackResultVo;
//    }

}
