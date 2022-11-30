package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class UsageVolumnVo {

    private int volumeUsed;
    private int snapshotUsed;
    private int gigabyteUsed;

}
