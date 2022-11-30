/*
 * Developed by yjlee90 on 2019-07-22.
 * Last modified 2019-07-22 10:24:56.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class VolumeDetachVo extends CommonVo {

    private String projectId;
    private String attachmentId;
    private String volumeId;
    private String instanceId;
    private Boolean isForce;

}
