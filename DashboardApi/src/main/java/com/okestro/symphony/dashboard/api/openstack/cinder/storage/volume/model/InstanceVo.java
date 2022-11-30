/*
 * Developed by kbcho on 2019-07-02.
 * Last modified 2019-06-28 10:08:42.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model;

import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.SecGroupVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VmVo;
import lombok.Data;

import java.util.List;

/**
 * openstack instance(vm)
 */
@Data
public class InstanceVo extends VmVo {
	private String id;
	private String name;
	private String description;
	private String lastUpdated;
	private String status;
	private String powerStatus;
	private String taskState;
	private String providerId;
	private String providerName;
	private String projectName;
	private String hostId;
	private String hostName;
	private String macAddress;
	private List<String> fixedIpAddresses;
	private List<String> floatingIpAddresses;
	private int cpu;
	private int ram;
	private int disk;
	private String availabilityZone;
	private String vnc;
	private FlavorVo flavor;
	private String keyName;
	private String log;
	private long created;
	private long launched;
	private String imageId;
	private String flavorId;
	private String[] imageInfo;
	private String[] flavorInfo;
	private List<com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model.NetworkVo> networkInfo;
	private List<com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model.VolumeVo> volumes;
	private List<SecGroupVo> secGroupList;
}