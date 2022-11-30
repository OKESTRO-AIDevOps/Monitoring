package com.okestro.symphony.dashboard.api.openstack.nova.compute.keypair.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultKeypairVo extends com.okestro.symphony.dashboard.api.openstack.nova.compute.keypair.model.KeypairVo {
    private String resultCode;
    private String resultMessage;
}
