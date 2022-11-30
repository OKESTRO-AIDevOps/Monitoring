package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volumesnapshot.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultSnapshotVo<T> extends VolumeSnapshotVo{
    private String resultCode;

    private T viewList;

}
