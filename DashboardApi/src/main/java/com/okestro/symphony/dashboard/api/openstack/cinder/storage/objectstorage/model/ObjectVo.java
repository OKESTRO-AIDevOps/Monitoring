package com.okestro.symphony.dashboard.api.openstack.cinder.storage.objectstorage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ObjectVo {

    private String name;

    private String type;

    private long size;

    private boolean isFolder;
}
