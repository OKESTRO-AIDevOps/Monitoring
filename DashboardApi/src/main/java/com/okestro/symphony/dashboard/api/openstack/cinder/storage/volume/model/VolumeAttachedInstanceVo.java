/*
 * Developed by yjlee90 on 2019-06-29.
 * Last modified 2019-06-29 15:50:40.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;

@Data
public class VolumeAttachedInstanceVo {

    private String serverId;
    private String hostId;
    private String instanceName;
    private String ipAddress;
    private String flavor;

}
