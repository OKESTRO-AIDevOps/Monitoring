package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.svc.impl;

import com.google.gson.Gson;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.PoolVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VolumeVo;
import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.*;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.svc.NetworkService;
import com.okestro.symphony.dashboard.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.network.*;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.ConnectException;
import java.util.*;


@Slf4j
@Service
@Transactional
public class NetworkServiceImpl implements NetworkService {
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
     * 모든 네트워크 리스트 조회
     * @return
     * @throws Exception
     */
    @Override
    public List<NetworkVo> NetworkListAll() throws Exception{
        OSClient.OSClientV3 osClient = null;
        List<? extends Network> networks = null;
        List<NetworkVo> networkVoList = new ArrayList<>();
        List<SubnetVo> subnetVoList = new ArrayList<>();


        // get current date time
        String nowDate = DateUtil.now();

        try{
            osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);


            networks = osClient.networking().network().list();


            for(Network tmpNetwork : networks){
                log.debug("##### Y_TEST tmpNetwork Value["+tmpNetwork.toString()+"]");
                log.debug("###################################################");

                NetworkVo vo = new NetworkVo();

                subnetVoList = setSubnetVo(tmpNetwork.getNeutronSubnets(),subnetVoList);

                vo.setName(tmpNetwork.getName());
                vo.setId(tmpNetwork.getId());
                vo.setMtu(tmpNetwork.getMTU());
                vo.setAvailabilityZones(tmpNetwork.getAvailabilityZones());
                vo.setAvailabilityZoneHints(tmpNetwork.getAvailabilityZoneHints());
                vo.setNeutronSubnets(subnetVoList);
                if (vo.getNetworkType() != null) {
                    vo.setNetworkType(tmpNetwork.getNetworkType().toString());
                }
                vo.setSubnets(tmpNetwork.getSubnets());
                vo.setTenantId(tmpNetwork.getTenantId());
                vo.setProviderPhyNet(tmpNetwork.getProviderPhyNet());
                vo.setAdminStateUp(tmpNetwork.isAdminStateUp());
                vo.setRouterExternal(tmpNetwork.isRouterExternal());
                vo.setShared(tmpNetwork.isShared());
                vo.setProviderSegID(tmpNetwork.getProviderSegID());


                networkVoList.add(vo);
            }

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
            throw e;
        }
        return networkVoList;
    }


    /**
     * 네트워크 리스트 조회
     *
     * @return
     * @throws Exception
     */
//    @Override
//    public List<NetworkVo> networkList(CommonVo commonVo) throws Exception {
//
//        List<NetworkVo> networkVos = new ArrayList<NetworkVo>();
//        try {
//            // get openstack client
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(commonVo.getProjectName(), commonVo.getUserId());
//
//            Map<String, String> filter = new HashMap<String, String>();
////            filter.put(OpenStackConstant.OS_FILTER_PROJECT_ID, osClient.getToken().getProject().getId());
//            String projectId = osClient.getToken().getProject().getId();
//            List<? extends Network> networks = osClient.networking().network().list();
//
//            // 네트워크 VO 설정
//            for (Network network : networks) {
//                if (network.isShared() || projectId.equals(network.getTenantId())) {
//                    networkVos.add(getNetworkDetail(network, osClient, true));
//                }
//            }
//
//
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            throw e;
//        }
//
//        return networkVos;
//    }

    /**
     * 네트워크 하나 조회 subnetAt = false로
     *
     * @param networkVo
     * @param subnetAt
     * @return
     */
//    @Override
//    public NetworkVo getNetwork(NetworkVo networkVo, boolean subnetAt) throws Exception {
//
//        try {
//            // get openstack client
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(networkVo.getProjectName(), networkVo.getUserId());
//
//            Network network = osClient.networking().network().get(networkVo.getId());
//            networkVo = getNetworkDetail(network, osClient, subnetAt);
//
//
//            List<? extends SecurityGroup> securityGroups = osClient.networking().securitygroup().list();
//
//            List<SecurityGroupVo> securityGroupVoList = new ArrayList<SecurityGroupVo>();
//
//            for (SecurityGroup securityGroup : securityGroups) {
//                if (securityGroup.getTenantId().equalsIgnoreCase(networkVo.getTenantId())) {
//                    SecurityGroupVo securityGroupVo = new SecurityGroupVo();
//
//                    securityGroupVo.setId(securityGroup.getId());
//                    securityGroupVo.setName(securityGroup.getName());
//                    securityGroupVo.setDescription(securityGroup.getDescription());
//                    securityGroupVo.setTenantId(securityGroup.getTenantId());
//
//                    securityGroupVoList.add(securityGroupVo);
//                }
//
//            }
//
//            networkVo.setSecurityGroupVoList(securityGroupVoList);
//
//        } catch (Exception e) {
//            //에러 처리 해야함
//            log.error(e.getMessage());
//            throw e;
//        }
//
//        return networkVo;
//    }


    /**
     * 네트워크 개별상세 조회 openstack : network to NetworkVo
     *
     * @param network
     * @param osClient
     * @return
     */
    @Override
    public NetworkVo getNetworkDetail(Network network, OSClient.OSClientV3 osClient, boolean subnetAt) {

        NetworkVo networkVo = new NetworkVo();
//
//        if (network.getStatus() != null) {
//            networkVo.setStatus(network.getStatus().toString());
//        }
//
//        //서브넷 정보가 필요할시 수행
//        if (subnetAt) {
//            List<SubnetVo> subnetVoList = new ArrayList<SubnetVo>();
//            if (network.getSubnets() != null) {
//                for (String subnetId : network.getSubnets()) {
//
//                    SubnetVo networkOpenstackSubnetVo = new SubnetVo();
//
//                    // Get a Subnet by ID
//                    Subnet subnet = osClient.networking().subnet().get(subnetId);
//
//                    if (subnet != null) {
//                        networkOpenstackSubnetVo = getSubnetToSubnetVo(subnet);
//                        subnetVoList.add(networkOpenstackSubnetVo);
//                    }
//                }
//                networkVo.setSubnets(subnetVoList);
//            }
//
//        }

//        networkVo.setName(network.getName());
//        networkVo.setProviderPhyNet(network.getProviderPhyNet());
//        networkVo.setAdminStateUp(network.isAdminStateUp());
//        networkVo.setTenantId(network.getTenantId());
//        if (network.getNetworkType() != null) {
//            networkVo.setNetworkType(network.getNetworkType().toString());
//        }
//        networkVo.setRouterExternal(network.isRouterExternal());
//        networkVo.setId(network.getId());
//        networkVo.setShared(network.isShared());
//        networkVo.setProviderSegID(network.getProviderSegID());
//        networkVo.setAvailabilityZoneHints(network.getAvailabilityZoneHints());
//        networkVo.setAvailabilityZones(network.getAvailabilityZones());
//        networkVo.setMtu(network.getMTU());

        return networkVo;
    }

    /**
     * openstack subnet 을 subnetVo로 변경
     *
     * @param subnet
     * @return
     */
    public SubnetVo getSubnetToSubnetVo(Subnet subnet) {

        SubnetVo subnetVo = new SubnetVo();
        if (subnet != null) {
            subnetVo.setId(subnet.getId());
            subnetVo.setName(subnet.getName());
            subnetVo.setEnableDHCP(subnet.isDHCPEnabled());
            subnetVo.setNetworkId(subnet.getNetworkId());
            subnetVo.setTenantId(subnet.getTenantId());
            subnetVo.setDnsNames(subnet.getDnsNames());
            if (subnet.getIpVersion() != null) {
                subnetVo.setIpVersion(subnet.getIpVersion().toString());
            }
            subnetVo.setGateway(subnet.getGateway());
            subnetVo.setCidr(subnet.getCidr());
            List<? extends Pool> pools = subnet.getAllocationPools();

            List<PoolsVo> poolsVos = new ArrayList<PoolsVo>();
            for (Pool pool : pools) {
                PoolsVo poolsVo = new PoolsVo();
                poolsVo.setStart(pool.getStart());
                poolsVo.setEnd(pool.getEnd());

                poolsVos.add(poolsVo);
            }
            subnetVo.setPools(poolsVos);
            subnetVo.setDnsNames(subnet.getDnsNames());
        }

        return subnetVo;
    }

    /**
     * 네트워크 기준으로 포트 리스트 조회
     *
     * @param networkVo
     * @return
     */
    @Override
    public List<PortVo> getNetworkPortList(NetworkVo networkVo) {
        List<PortVo> resultPort = new ArrayList<PortVo>();

        try {
            OSClient.OSClientV3 osClient = openStackConnectionService.connect(networkVo.getProjectName(), networkVo.getUserId());
            List<? extends Port> ports = osClient.networking().port().list();

            for (Port port : ports) {
                if (port.getNetworkId().equalsIgnoreCase(networkVo.getId())) {
                    PortVo portVo = getPortToPortVo(port);
                    List<String> securityGroupNams = new ArrayList<String>();

                    for (String securityGroup : portVo.getSecurityGroups()) {
                        SecurityGroup securityGroups = osClient.networking().securitygroup().get(securityGroup);
                        if (securityGroups != null) {
                            securityGroupNams.add(securityGroups.getName());
                        }
                    }

                    portVo.setSecurityGroupNames(securityGroupNams);

                    resultPort.add(portVo);
                }
            }


        } catch (Exception e) {
            //에러 처리 해야함
            e.printStackTrace();
        }

        return resultPort;
    }

    /**
     * openstack Port를 PortVo로 변환
     *
     * @param port
     * @return
     */
    @Override
    public PortVo getPortToPortVo(Port port) {


        if (port.getName().equalsIgnoreCase("")) {
            port.setName(port.getId().substring(0, 8));
        }

        PortVo portVo = new PortVo();

        portVo.setAdminStateUp(port.isAdminStateUp());
        portVo.setTenantId(port.getTenantId());
        portVo.setStatus(port.getState().toString());
        portVo.setPortSecurityEnabled(port.isPortSecurityEnabled());
        portVo.setNetworkId(port.getNetworkId());
        portVo.setName(port.getName());
        portVo.setMacAddress(port.getMacAddress());
        portVo.setId(port.getId());
        portVo.setDeviceOwner(port.getDeviceOwner());
        portVo.setDeviceId(port.getDeviceId());
        portVo.setVnicType(port.getvNicType());
        portVo.setVifType(port.getVifType());
        portVo.setHostId(port.getHostId());


        //AllowedAddressPair 설정
        Set<? extends AllowedAddressPair> allowedAddressPairs = port.getAllowedAddressPairs();
        List<AllowedAddressPairVo> allowedAddressPairVos = new ArrayList<AllowedAddressPairVo>();

        for (AllowedAddressPair allowedAddressPair : allowedAddressPairs) {
            AllowedAddressPairVo allowedAddressPairVo = new AllowedAddressPairVo();
            allowedAddressPairVo.setIpAddress(allowedAddressPair.getIpAddress());
            allowedAddressPairVo.setMacAddress(allowedAddressPair.getMacAddress());
            allowedAddressPairVos.add(allowedAddressPairVo);
        }
        portVo.setAllowedAddressPairs(allowedAddressPairVos);


        //FixedIps
        Set<? extends IP> ips = port.getFixedIps();
        List<FixedIpVo> fixedIpVos = new ArrayList<FixedIpVo>();

        for (IP ip : ips) {
            FixedIpVo fixedIpVo = new FixedIpVo();
            fixedIpVo.setIpAddress(ip.getIpAddress());
            fixedIpVo.setSubnetId(ip.getSubnetId());
            fixedIpVos.add(fixedIpVo);
        }

        portVo.setFixedIps(fixedIpVos);
        portVo.setProfileMap(port.getProfile());
        portVo.setVifDetailsMap(port.getVifDetails());
        portVo.setSecurityGroups(port.getSecurityGroups());


        return portVo;
    }

    /**
     * 프로젝트 id 가져오기
     *
     * @param projectName
     * @return
     * @throws Exception
     */
    @Override
    public String getNetworkProjectId(String projectName, String userId) throws Exception {
        String projectId = "";
        try {
            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
            projectId = osClient.getToken().getProject().getId();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
        return projectId;
    }


    /**
     * Network Type에서 Subnet정보 추출 후 SubnetVo셋팅.
     * @param sbInfoList
     * @param subnetVoList
     * @return
     */
    private List<SubnetVo> setSubnetVo(List<? extends Subnet> sbInfoList, List<SubnetVo> subnetVoList){

        SubnetVo subnetVo = null;

        for(Subnet tmpSubnet : sbInfoList ){
            subnetVo = new SubnetVo();
            subnetVo = getSubnetToSubnetVo(tmpSubnet);

            subnetVoList.add(subnetVo);
        }

        return subnetVoList;
    }

    /**
     * connect to openstack
     * TODO-차후 변경 예정
     */
    private OSClient.OSClientV3 connect() throws ConnectException {
        return openStackConnectionService.connect("https://os00.okestro.cld:5000/v3", "default", "egov", "egovuser", "okestro2018");
    }
}
