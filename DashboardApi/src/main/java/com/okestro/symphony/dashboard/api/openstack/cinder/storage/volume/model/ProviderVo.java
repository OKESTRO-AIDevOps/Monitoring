/*
 * Developed by kbcho on 2019-05-28.
 * Last modified 2019-05-28 14:54:06.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;

import java.util.List;

@Data
public class ProviderVo {
    private String url;
    private String id;
    private String name;
    private String type;
    private String userHashId;
    private String domain;
    private List<ProjectVo> projectList;
    //private AdminVo admin;
}
