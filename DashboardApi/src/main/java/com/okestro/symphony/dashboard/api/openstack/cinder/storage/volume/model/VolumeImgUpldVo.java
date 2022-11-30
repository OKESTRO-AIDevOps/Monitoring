/*
 * Developed by yjlee90 on 2019-07-16.
 * Last modified 2019-07-16 18:11:11.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class VolumeImgUpldVo extends CommonVo {
    private String volumeId;
    private String imageName;
    private String diskFormat;
    private String volumeStatus;
    private Boolean isForce;

}
