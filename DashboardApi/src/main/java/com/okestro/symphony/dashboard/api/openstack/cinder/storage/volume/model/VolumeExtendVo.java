/*
 * Developed by yjlee90 on 2019-07-15.
 * Last modified 2019-07-15 13:23:50.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class VolumeExtendVo extends CommonVo {
    private String volumeId;
    private String size;
}
