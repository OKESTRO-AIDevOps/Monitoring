/*
 * Developed by bhhan@okestro.com on 2020-07-17
 * Last modified 2020-07-17 14:56:10
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BareMetalVo {
	private List<NodeVo> nodes;
	private List<DriverVo> drivers;
}
