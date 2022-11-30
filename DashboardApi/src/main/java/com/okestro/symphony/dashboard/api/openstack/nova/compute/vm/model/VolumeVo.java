/*
 * Developed by kbcho@okestro.com on 2020-07-17
 * Last modified 2020-07-17 10:43:27
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class VolumeVo {
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

    private String sourceVolid;
    private String snapshotId;
    private String imageRef;

    private List<? extends VolumeAttachment> attachments;
    private String host;
    private String tenantId;
    private String zone;
}
