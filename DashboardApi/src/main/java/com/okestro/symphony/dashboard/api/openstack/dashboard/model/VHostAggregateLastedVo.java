package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VHostAggregateLastedVo {
    private String id;
    private String providerId;
    private String providerNm;
    private String hostAggregateNm;
    private String avZoneNm;
    private String collectDt;
}
