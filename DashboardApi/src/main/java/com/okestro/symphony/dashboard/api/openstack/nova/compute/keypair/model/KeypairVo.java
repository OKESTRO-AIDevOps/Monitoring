/*
 * Developed by yy.go@okestro.com on 2020-07-16
 * Last modified 2020-07-16 14:59:27
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.keypair.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeypairVo extends CommonVo {
    private int id;
    private String name;
    private String publicKey;
    private String fingerprint;
    private String userName;
    private String createAt;
    private String privateKey;

}

