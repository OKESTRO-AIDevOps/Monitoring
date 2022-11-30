/*
 * Developed by yy.go@okestro.com on 2020-07-08
 * Last modified 2020-07-08 15:43:56
 */

package com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.svc.impl;

import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.svc.InstanceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.Flavor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class InstanceTypeServiceImpl implements InstanceTypeService {
    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    WebsocketService websocketService;

    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;

    /**
     * Get All Flavor List
     */
    @Override
    public List<FlavorVo> retrieveAllInstanceTypes() {
        List<FlavorVo> flavors = new ArrayList<FlavorVo>();

        try {
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint, domain, user, password);

            for (Flavor item : osClient.compute().flavors().list()) {

                FlavorVo flavor = new FlavorVo();

                flavor.setName(item.getName());
                flavor.setId(item.getId());
                flavor.setRam(item.getRam());
                flavor.setVcpus(item.getVcpus());
                flavor.setDisk(item.getDisk());
                flavor.setEphemeral(item.getEphemeral());
                flavor.setSwap(item.getSwap());
                flavor.setRxtxFactor(item.getRxtxFactor());
                flavor.setPublic(item.isPublic());
                flavor.setDisabled(item.isDisabled());

                flavors.add(flavor);
            }

            if (flavors.size() > 0) {
                // set last updated
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                flavors.get(flavors.size() - 1).setLastUpdated(LocalDateTime.now().format(formatter));
            }

        } catch (ConnectException e) {
            //
            log.error(e.getMessage(), e);
        } finally {
            openStackConnectionService.close();
        }

        return flavors;
    }


//    /**
//     * Get Flavor List
//     */
//    @Override
//    public List<FlavorVo> retrieveInstanceTypes(String projectName , String userId) {
//        List<FlavorVo> flavors = new ArrayList<FlavorVo>();
//
//        try {
//            OSClient.OSClientV3 osClient = connect(projectName , userId);
//
//            for (Flavor item : osClient.compute().flavors().list()) {
//
//                FlavorVo flavor = new FlavorVo();
//
//                flavor.setName(item.getName());
//                flavor.setId(item.getId());
//                flavor.setRam(item.getRam());
//                flavor.setVcpus(item.getVcpus());
//                flavor.setDisk(item.getDisk());
//                flavor.setEphemeral(item.getEphemeral());
//                flavor.setSwap(item.getSwap());
//                flavor.setRxtxFactor(item.getRxtxFactor());
//                flavor.setPublic(item.isPublic());
//                flavor.setDisabled(item.isDisabled());
//
//                flavors.add(flavor);
//            }
//
//            if (flavors.size() > 0) {
//                // set last updated
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                flavors.get(flavors.size() - 1).setLastUpdated(LocalDateTime.now().format(formatter));
//            }
//
//        } catch (ConnectException e) {
//            //
//            log.error(e.getMessage(), e);
//        } finally {
//            openStackConnectionService.close();
//        }
//
//        return flavors;
//    }



    /**
     * connect to openstack
     */
    private OSClient.OSClientV3 connect(String projectName , String userId) throws ConnectException {
       return openStackConnectionService.connect(projectName, userId);
//        return openStackConnectionService.connect("https://os00.okestro.cld:5000/v3", "default", "admin", "admin", "okestro2018");

    }

    private OSClient.OSClientV3 connectbyAdmin() throws ConnectException {
        return openStackConnectionService.connect("https://os00.okestro.cld:5000/v3", "default", "admin", "admin", "okestro2018");
    }
}
