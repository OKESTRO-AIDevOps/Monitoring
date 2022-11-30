package com.okestro.symphony.dashboard.api.openstack.cinder.storage.objectstorage.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ContainerVo {

    private String id;

    private String name;

    private long objectCount;

    private long size;

    private String createdAt;

    private Map<String, String> metadata;
}
