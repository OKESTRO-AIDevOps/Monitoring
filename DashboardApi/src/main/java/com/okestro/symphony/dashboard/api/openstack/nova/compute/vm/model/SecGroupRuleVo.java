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
 * Last modified 2020-07-14 15:51:06
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;

@Data
public class SecGroupRuleVo {
    private String id;
    private String tenantId;
    private String securityGroupId;
    private String direction;
    private String etherType;
    private String remoteGroup;
    private int portRangeMin;
    private int portRangeMax;
    private String protocol;
    private String remoteIpPrefix;
}
