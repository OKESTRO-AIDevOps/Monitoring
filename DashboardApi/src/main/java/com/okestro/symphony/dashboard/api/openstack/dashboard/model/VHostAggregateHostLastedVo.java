package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VHostAggregateHostLastedVo {
    private String id;
    private String name;
    private String hostAggregateId;
    private String providerId;
    private String providerNm;
    private String avZoneNm;
    private String collectDt;

}
