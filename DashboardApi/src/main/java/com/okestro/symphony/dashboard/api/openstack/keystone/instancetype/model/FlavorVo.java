package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlavorVo extends com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.InstanceTypeVo {
    private int ephemeral;
    private int swap;
    private float rxtxFactor;
    private boolean isPublic;
    private boolean isDisabled;
    private String lastUpdated;
}
