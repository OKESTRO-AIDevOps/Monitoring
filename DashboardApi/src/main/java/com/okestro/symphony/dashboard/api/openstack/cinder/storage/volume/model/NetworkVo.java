/*
 * Developed by kbcho on 2019-07-02.
 * Last modified 2019-06-28 10:08:42.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import lombok.Data;

import java.util.List;

@Data
public class NetworkVo {
    private String name;
    private String id;
    private List<String> subnetList;
    private boolean shared;
    private boolean adminStateUp;
    private String status;
    private boolean portSecurityEnabled;
    private String type;
    private String ip;
    private String macAddress;
}
