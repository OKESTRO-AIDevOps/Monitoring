/*
 * Developed by yjlee90 on 2019-06-10.
 * Last modified 2019-06-10 17:32:53.
 * Copyright (c) 2019 OKESTRO. All rights reserved
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;

@Data
public class VolumeQuotasVo {
    int volumeQuota;
    int snapshotQuota;
    int gigabyteQuota;

    int volumeUsed;
    int snapshotUsed;
    int gigabyteUsed;
}
