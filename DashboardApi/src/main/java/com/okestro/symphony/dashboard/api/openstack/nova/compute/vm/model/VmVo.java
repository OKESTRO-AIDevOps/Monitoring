/*
 * Developed by kbcho@okestro.com on 2020-07-16
 * Last modified 2020-07-16 14:56:02
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 20:20:41
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 16:53:02
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 16:46:13
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-15
 * Last modified 2020-07-15 10:58:43
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 17:25:00
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 13:57:53
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 12:07:28
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 12:02:09
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-14
 * Last modified 2020-07-14 09:27:46
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-13
 * Last modified 2020-07-13 15:47:57
 */

/*
 * Developed by kbcho@okestro.com on 2020-07-09
 * Last modified 2020-07-09 17:20:43
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
import lombok.Data;

import java.util.List;

@Data
public class VmVo extends CommonVo {
    private String id;
    private String name;
    private String description;
    private String lastUpdated;
    private String status;
    private String powerStatus;
    private String taskState;
    private String providerId;
    private String providerName;
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
    private String imageName;
    private String flavorId;
    private String flavorName;
    private String[] imageInfo;
    private String[] flavorInfo;
    private List<com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.NetworkVo> networkList;
    private List<com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VolumeVo> volumeList;
    private List<com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.SecGroupVo> secGroupList;
    private com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.KeypairVo keypair;
    private FaultVo fault;
    private int lines;
    private List<String> volumeId;
    private String targetHostName;
}