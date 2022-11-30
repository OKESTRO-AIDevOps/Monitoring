/*
 * Developed by yjlee90 on 2019-05-23.
 * Last modified 2019-05-09 10:12:24.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class VolumeVo extends CommonVo {

    /**
     * 오픈스택 볼륨
     */
    private String id;
    private String name;
    private String description;
    private int size;
    private String volumeType;
    private boolean bootable;
    private boolean encrypted;
    private Date createdAt;
    private Map<String, String> metaData;
    private Volume.Status status;
    private Volume.MigrationStatus migrateStatus;
    private String vmName;

    private String source;

    private String sourceVolId;
    private String snapshotId;
    private String imageId;

    private List<? extends VolumeAttachment> attachments;
    private String host;
    private String tenantId;
    private String zone;
}
