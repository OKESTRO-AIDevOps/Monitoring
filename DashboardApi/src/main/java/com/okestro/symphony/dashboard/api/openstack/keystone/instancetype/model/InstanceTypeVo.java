package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model;

/*
 * Developed by yy.go@okestro.com on 2020-07-08
 * Last modified 2020-07-08 13:23:35
 */

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstanceTypeVo extends CommonVo {
    private String id;  /*Flavor Id*/
    private String name; /*인스턴스 이름*/
    private int vcpus; /*인스턴스 vCpu*/
    private int ram;  /*인스턴스 메모리*/
    private int disk; /*인스턴스 디스크*/
}
