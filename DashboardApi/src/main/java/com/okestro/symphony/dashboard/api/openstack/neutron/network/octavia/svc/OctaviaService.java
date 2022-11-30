package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.svc;

import com.okestro.symphony.dashboard.cmm.model.SelectBoxVo;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.*;

import java.util.List;

public interface OctaviaService {

    /**
     * 로드밸런스 리스트
     * @return
     */
    public LoadBalancerGroupVo getLoadBalancerList(String projectName, String userId) throws Exception;

    /**
     * 서브넷 리스트 조회
     * @return
     * @throws Exception
     */
//    public SelectBoxVo getSubnetList(String projectName, String userId) throws Exception;

    /**
     * 연결 가능한 가상머신 리스트 조회
     * @return
     * @throws Exception
     */
//    public List<MemberVo> getConnectionVMList(String projectName, String userId) throws Exception;

    /**
     * 포트 중복체크
     * @param loadBalancerVo
     * @return
     * @throws Exception
     */
//    public boolean duplicatedProtocolPort(LoadBalancerVo loadBalancerVo) throws Exception;

    /**
     * 풀 리스트 조회
     * @param poolVo
     * @return
     * @throws Exception
     */
//    public List<PoolVo> getPoolList(PoolVo poolVo) throws Exception;

    /**
     * 로드밸런서 디테일
     * @param loadBalancerVo
     * @return
     * @throws Exception
     */
//    public LoadBalancerVo getLoadBalancerDetail(LoadBalancerVo loadBalancerVo) throws Exception;


    /**
     * 로드밸런서 리스너 조회
     * @param loadBalancerId
     * @return
     * @throws Exception
     */
//    public List<ListenerVo> getListenerList(String loadBalancerId, String projectName, String userId) throws Exception;

    /**
     * 맴버 상세조회
     * @param memberVoList
     * @return
     * @throws Exception
     */
//    public List<MemberVo> getMemberDetail(List<MemberVo> memberVoList) throws Exception;


}

