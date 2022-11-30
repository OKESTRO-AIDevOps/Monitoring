/*
 * Developed by bhhan@okestro.com on 2020-07-18
 * Last modified 2020-07-18 15:35:58
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverVo {
	private String name;
	private String password;
	private String host;
	private String address;
	private String port;
	private String kernel;
	private String ramdisk;

	private List<String> hosts;
}
