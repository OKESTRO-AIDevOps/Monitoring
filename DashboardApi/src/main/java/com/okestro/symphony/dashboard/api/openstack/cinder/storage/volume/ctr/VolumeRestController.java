package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.ctr;

import com.okestro.symphony.dashboard.api.openstack.baremetal.svc.BareMetalService;
import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model.VolumeVo;
import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.svc.VolumeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@Api(value = "volume", description = "[API] Volume 리스트")
@RequestMapping("/volume")
@RestController
public class VolumeRestController {
    @Autowired
    VolumeService volumeService;

    @ApiOperation(value = "[Meta] Volume 조회.", notes = "[Meta] SDK admin 권한으로 모든 Volume List 조회")
    @GetMapping("/list")
    public List<VolumeVo> listAllVolumes(){

        return volumeService.listAllVolumes();
    }

}
