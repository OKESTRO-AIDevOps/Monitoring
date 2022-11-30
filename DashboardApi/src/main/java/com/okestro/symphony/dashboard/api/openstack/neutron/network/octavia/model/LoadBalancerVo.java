package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoadBalancerVo extends CommonVo {
    private String id;                          /** 로드밸런스 아이디  **/
    private String name;                        /** 로드밸런스 명  **/
    private String description;                 /** 로드밸런스 설명  **/
    private String provisioningStatus;          /** 프로비저닝 상태  **/
    private String tenantId;                    /** 프로젝트 아이디  **/
    private boolean adminStateUp;               /** 관리자 상태  **/
    private String provider;                    /** 공급자(haproxy)  **/
    private List<com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.PoolVo> pools;                 /** 풀리스트  **/
    private List<ListenerVo> listeners;         /** 리스너   **/
    private List<MemberVo> members;             /** 멤버  **/
    private String listenerName;                /** 리스너 이름  **/
    private String listenerId;                  /** 리스너 아이디  **/
    private String defaultPoolId;                      /** 풀 아이디  **/
    private String protocol;                    /** 프로토콜 타입  **/
    private int port;                           /** 프로토콜 포트  **/
    private String vipPortId;                   /** VIP 포트 아이디  **/
    private boolean vipFixed;                   /** VIP 고정 아이피 지정 **/
    private String vipAddress;                  /** VIP 주소  **/
    private String vipSubnetId;                 /** VIP 서브넷  **/
    private String operatingStatus;             /** 운영상태  **/
    private String floatingIp;                  /** 할당된 유동 아이피**/


    private String resultMessage;
}
