/*
 * Developed by sychoi on 2020-07-16
 * Last modified 2020-07-16 14:11:43
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.svc;
import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpVo;

public interface FloatingIpService {

    /**
     * 유동 IP 목록 조회
     * @param floatingIpVo
     * @return
     */
    public OpenStackResultVo retrieveNetFloatingIps(FloatingIpVo floatingIpVo);

    /**
     * 연결할 가상머신 목록 조회
     * @param floatingIpVo
     * @return
     */
//    public OpenStackResultVo retrieveConnectVmList(FloatingIpVo floatingIpVo);

    /**
     *
     * @param floatingIpVo
     * @return
     */
//    public OpenStackResultVo getNetworkQuotas(FloatingIpVo floatingIpVo);
}
