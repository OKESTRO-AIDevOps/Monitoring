package com.okestro.symphony.dashboard.cmm.websocket.config;

public enum WsDestType {

    //오픈스택 콘솔에서 메시지 받을 세부 페이지
    VMS("vms"),                                   /** 가상머신  **/
    KEYPAIR("keypair"),                         /** 키페어  **/
    IMAGE("image"),                             /** 이미지  **/
    NETWORK("network"),                         /** 네트워크  **/
    NETWORKDETAIL("networkdetail"),             /** 네트워크 상세 **/
    LOADBALANCER("loadbalancer"),               /** 로드벨런서  **/
    LOADBALANCERDETAIL("loadbalancerdetail"),   /** 로드벨런서 상세 **/
    FLOATINGIP("floatingip"),                   /** 유동IP  **/
    SECURITYGROUP("securitygroup"),             /** 보안그룹  **/
    SECURITYGROUPDETAIL("securitygroupdetail"), /** 보안그룹 상세  **/
    ROUTER("router"),                           /** 라우터  **/
    ROUTERDETAIL("routerdetail"),               /** 라우터 상세**/
    VOLUME("volume"),                           /** 볼륨  **/
    STORAGEVOLUME("storageVolume"),
    SNAPSHOT("snapshot"),                       /** 스냅샷  **/
    STORAGESNAPSHOT("storageSnapshot"),         /** 스냅샷  **/
    OBJECTSTORAGE("objectstorage"),             /** 오브젝트스토리지  **/
    CONTAINER("container"),                     /** 오브젝트스토리지 상세  **/
    INSTANCETYPE("instancetype"),               /** 인스턴스유형  **/
    GROUP("group"),                             /** 그룹  **/
    USER("user"),                               /** 사용자  **/
    HYPERVISOR("hypervisor"),                   /** 하이퍼바이저  **/
    ORCHESTRATIONS("orchestrations"),             /** 오케스트레이션  **/

    //관리자 페이지에서 메시지 받을 세부 페이지
    SVCCNSLE("svcCnsle"),                       /** 콘솔관리  **/
    OPENSTACKPROVIDER("openstackprovider"),     /** IaaS연계관리  **/
    MENUSPO("menuspo"),                         /** 권한메뉴관리  **/
    USERLIST("userlist"),                       /** 사용자관리  **/
    PROJECT("project"),                         /** 프로젝트관리  **/
    STATISTICS("statistics"),                   /** 통계  **/
    AUDITLOG("auditlog"),                       /** 감사로그관리  **/
    ASSERT("assert"),                           /** 자산관리  **/
    MYINFO("myinfo"),                           /** 내정보  **/
    OPERATIONMANAGEMENT("operationmanagement"); /** 운영관리  **/


    private String name;

    private WsDestType(String name){
        this.name = name;
    }
}