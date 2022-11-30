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
 * Last modified 2020-07-14 15:50:48
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;

import java.util.List;

@Data
public class SecGroupVo {
    private String id;
    private String name;
    private String description;
    private List<SecGroupRuleVo> rules;
}
