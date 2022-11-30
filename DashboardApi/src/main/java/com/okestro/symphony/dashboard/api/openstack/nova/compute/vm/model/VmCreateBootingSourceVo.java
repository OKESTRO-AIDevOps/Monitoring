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
 * Last modified 2020-07-14 13:57:54
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
 * Last modified 2020-07-14 11:45:24
 */

package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model;

import lombok.Data;

@Data

public class VmCreateBootingSourceVo {

    public static final int BOOT_FROM_VOLUME = 1;
    public static final int BOOT_FROM_VOLUME_SNAPSHOT = 2;
    public static final int BOOT_FROM_IMAGE = 3;
    public static final int BOOT_FROM_INSTANCE_SNAPSHOT = 4;
    private int bootingSourceType;
    private boolean createNewYn;
    private boolean cascadeYn;
    private int volumeSize;

}
