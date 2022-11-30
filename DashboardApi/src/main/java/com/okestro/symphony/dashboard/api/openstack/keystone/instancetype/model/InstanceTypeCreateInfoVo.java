/*
 * Developed by yy.go@okestro.com on 2020-07-15
 * Last modified 2020-07-15 18:43:57
 */

package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model;


import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceTypeCreateInfoVo extends CommonVo {

    private String id;
    private String name;
    private int vcpus;
    private int ram;
    private int disk;
    private int ephemeralDisk;
    private int swapDisk;
    private float rxtxFactor;
}
