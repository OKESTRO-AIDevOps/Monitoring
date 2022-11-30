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

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.image.model.ImageVo;
import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volumesnapshot.model.VolumeSnapshotVo;
import lombok.Data;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.compute.ext.AvailabilityZone;

import java.util.List;

@Data
public class VmCreateInfoVo extends CommonVo {
    // 생성용 정보
    private String providerId;
    private List<? extends AvailabilityZone> availabilityZoneList;
    private List<com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VolumeVo> volumeList;
    private List<VolumeSnapshotVo> volumeSnapshotList;
    private List<ImageVo> imageList;
    private List<ImageVo> snapshotList;
    private List<FlavorVo> flavorList;
    private List<NetworkVo> networkList;
    private List<? extends SecGroupExtension> secGroupExtensionList;
    private List<KeypairVo> keypairList;

    // 생성 정보
    private String id;
    private String name;
    private String zoneName;
    private FlavorVo flavor;
    private com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VolumeVo volume;
    private VolumeSnapshotVo volumeSnapshot;
    private ImageVo image;
    private ImageVo snapshot;
    private boolean deleteOnTermination;
//    private List<NetworkVo> networks;
    private List<PortVo> networkPortList;
    private List<com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.SecGroupVo> secGroups;
    private KeypairVo keypair;
    private String keypairName;
    private String userData; // 사용자 지정 스크립트?
    private String snapshotName;

    private VmCreateBootingSourceVo bootingSource;

    // 기타 정보
    private String action; // 제어용 변수
    private String changeInstanceTypeId; // 인스턴스 유형 변경용 ID

    // live migration 관련
    private String hostName;
    private String targetHostName;
    private boolean diskOverCommit;
    private boolean blockMigration;

}
