/*
 * Developed by kbcho on 2019-05-28.
 * Last modified 2019-05-28 14:54:12.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;

@Data
public class ProjectVo {
    private String id;
    private String name;
    private String role;
}
