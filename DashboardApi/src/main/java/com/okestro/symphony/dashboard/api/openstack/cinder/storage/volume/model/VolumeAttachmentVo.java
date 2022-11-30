/*
 * Developed by yjlee90 on 2019-06-24.
 * Last modified 2019-06-24 13:58:40.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Data;

@Data
public class VolumeAttachmentVo extends CommonVo {


    private String id;
    private String attachmentId;
    private String volumeId;
    private String instanceId;
    private String serverId;
    private String attachmentAt;
    private String device;


//    // 연결 필요 정보
//    @JsonProperty("volume_id")
//    private String volume_id;
//    @JsonProperty("instance_id")
//    private String instance_id;
//
//    @JsonProperty("mount_point")
//    private String mount_point;
//    @JsonProperty("host_name")
//    private String host_name;
//
//    // 연결 후 정보
//    @JsonAlias("server_id")
//    private String sever_id;
//
//    @JsonProperty("attachment_id")
//    private String attachment_id;
//
//    @JsonProperty("attached_at")
//    private String attached_At;
}
