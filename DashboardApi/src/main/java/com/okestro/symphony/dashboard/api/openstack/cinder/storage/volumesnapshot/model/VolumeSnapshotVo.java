/*
 * Developed by sychoi on 2020-07-17
 * Last modified 2020-07-17 19:12:24
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volumesnapshot.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;
import org.openstack4j.model.storage.block.Volume;

import java.util.Date;

@Data
public class VolumeSnapshotVo extends CommonVo {

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

    private String resultMessage;

}
