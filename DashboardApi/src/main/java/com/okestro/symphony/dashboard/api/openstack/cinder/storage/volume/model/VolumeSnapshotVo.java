/*
 * Developed by yjlee90 on 2019-06-10.
 * Last modified 2019-06-10 21:06:30.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;
import org.openstack4j.model.storage.block.Volume;

import java.util.Date;

@Data
public class VolumeSnapshotVo {

    private String name;
    private String description;
    private String id;
    private int size;
    private Volume.Status status;
    private String volumeId;
    private String volumeNm;
    private Date createdAt;
    private String updatedAt;

    // 생성시 필요
    private boolean isForce;
    private String projectId;

}
