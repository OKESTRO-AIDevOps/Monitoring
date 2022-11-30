package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.svc;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.NetworkVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.PortVo;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.Network;
import org.openstack4j.model.network.Port;

import java.util.List;

public interface NetworkService {


    /**
     * 네트워크 리스트 조회
     * @return
     */
    public List<NetworkVo> NetworkListAll() throws Exception;

    /**
     * 네트워크 리스트 조회
     * @return
     */
//    public List<NetworkVo> networkList(CommonVo commonVo) throws Exception;

    /**
     * 네트워크 개별정보 조회
     * @return
     */
//    public NetworkVo getNetwork(NetworkVo networkVo, boolean subnetAt) throws Exception;

    /**
     * 네트워크 디테일 조회 subnetAt 호출하지 false, true 로 조회할지 안할지 결정
     * @param network
     * @param osClient
     * @param subnetAt
     * @return
     */
    public NetworkVo getNetworkDetail(Network network, OSClient.OSClientV3 osClient, boolean subnetAt);

    /**
     * 네트워크 포트 리스트 조회
     * @param networkVo
     * @return
     */
    public List<PortVo> getNetworkPortList(NetworkVo networkVo);

    /**
     * 오픈스택 port객체를 Vo로 변경
     * @param port
     * @return
     */
    public PortVo getPortToPortVo(Port port);

    /**
     * 프로젝트 id 가져오기
     * @param projectName
     * @return
     * @throws Exception
     */
    public String getNetworkProjectId(String projectName , String userId) throws Exception;

}
