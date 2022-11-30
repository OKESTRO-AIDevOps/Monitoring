/*
 * Developed by ygchoi on 2019-06-12.
 * Last modified 2019-06-11 23:50:27.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.keystone.user.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OstackUserVo extends CommonVo {

    private String name;
    private String id;
    private String password;
    private String email;
    private String domainId;
    private String domain;
    private UserProjectVo userProjectVo;
//    private ProviderVo provider;
    private UserProjectVo project;
//    private RoleVo role;

    private String message;
    private String desc;
    private Boolean enabled;

}
