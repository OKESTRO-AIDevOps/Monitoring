package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class QuotaVolumnVo {

    private int volumeQuota;
    private int snapshotQuota;
    private int gigabyteQuota;

}
