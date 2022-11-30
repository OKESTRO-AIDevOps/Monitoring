package com.okestro.symphony.dashboard.api.openstack.dashboard.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VProjectLastedVo {
    private String id;
    private String name;
    private String state;
    private String providerId;
    private String providerNm;

    private String collectDt;

}
