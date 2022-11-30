package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.svc;

import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model.VolumeVo;


import java.util.List;

public interface VolumeService {

    public List<VolumeVo> listAllVolumes();
}
