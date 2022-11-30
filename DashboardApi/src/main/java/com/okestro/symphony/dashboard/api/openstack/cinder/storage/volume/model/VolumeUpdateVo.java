/*
 * Developed by yjlee90 on 2019-06-21.
 * Last modified 2019-06-21 17:06:42.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class VolumeUpdateVo extends CommonVo {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean bootable;

    private String name;

    private String description;

    private String projectId;
}
