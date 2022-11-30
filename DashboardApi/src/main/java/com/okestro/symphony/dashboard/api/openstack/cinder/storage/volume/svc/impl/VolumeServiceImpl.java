package com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.svc.impl;

import com.google.gson.Gson;
import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.model.VolumeVo;
import com.okestro.symphony.dashboard.api.openstack.cinder.storage.volume.svc.VolumeService;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class VolumeServiceImpl implements VolumeService {


    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;

    @Override
    public List<VolumeVo> listAllVolumes(){
        OSClient.OSClientV3 osClient = null;
        List<? extends Volume> volumes = null;
        List<VolumeVo> volumeVoList = new ArrayList<>();
        // get current date time
//        String nowDate = DateUtil.now();

        try{
            osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);

            volumes = osClient.blockStorage().volumes().list();


            for(Volume tmpVolume : volumes){
                log.info("##### Y_TEST tmpVolume Value["+tmpVolume.toString()+"]");
                log.info("###################################################");

                VolumeVo vo = new VolumeVo();

                vo.setName(tmpVolume.getName());
                vo.setId(tmpVolume.getId());
                vo.setAttachments(tmpVolume.getAttachments());
                vo.setStatus(tmpVolume.getStatus());
                vo.setCreatedAt(tmpVolume.getCreated());
                vo.setDescription(tmpVolume.getDescription());
                vo.setEncrypted(tmpVolume.encrypted());
                vo.setImageId(tmpVolume.getImageRef());
                vo.setMetaData(tmpVolume.getMetaData());
                vo.setMigrateStatus(tmpVolume.getMigrateStatus());
                vo.setSnapshotId(tmpVolume.getSnapshotId());
                vo.setSize(tmpVolume.getSize());
                vo.setSourceVolId(tmpVolume.getSourceVolid());
                vo.setTenantId(tmpVolume.getTenantId());
                vo.setZone(tmpVolume.getZone());
                vo.setVolumeType(tmpVolume.getVolumeType());
                vo.setHost(tmpVolume.host());

                volumeVoList.add(vo);
            }

            log.info("## Y_TEST volumeVoList Size["+volumeVoList.size()+"]");
            log.info("## Y_TEST volumeVoList Size["+volumeVoList.size()+"]");



        }catch (Exception e){
            log.info("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return volumeVoList;
    }

}
