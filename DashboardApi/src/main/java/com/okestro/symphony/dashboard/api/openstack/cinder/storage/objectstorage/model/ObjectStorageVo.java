package com.okestro.symphony.dashboard.api.openstack.cinder.storage.objectstorage.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ObjectStorageVo extends CommonVo {

    private String name;

    private String objectCount;

    private boolean isPublic;

    private long totalSize;

    private String objectPath;

    private Map<String, String> metadata;

}
