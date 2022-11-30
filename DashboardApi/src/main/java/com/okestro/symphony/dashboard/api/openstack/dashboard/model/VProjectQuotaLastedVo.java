package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VProjectQuotaLastedVo {
    private String id;
    private String name;
    private String providerId;
    private String providerNm;

    private String collectDt;

    // 프로젝트 Nova 쿼터 정보
    int instanceQuota;
    int coreQuota;
    int ramQuota;

    int instanceUsed;
    int coreUsed;
    int ramUsed;

    // 프로젝트 Cinder 쿼터 정보
    int volumeQuota;
    int snapshotQuota;

    int volumeUsed;
    int snapshotUsed;

    // 프로젝트 Nutron 쿼터 정보
    int routerQuota;
    int portQuota;
    int networkQuota;
    int floatingIpQuota;
    int securityGroupQuota;
    int securityGroupRuleQuota;

    int routerUsed;
    int portUsed;
    int networkUsed;
    int floatingIpUsed;
    int securityGroupUsed;
    int securityGroupRuleUsed;
}
