//package com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.svc.impl;
//
//import com.google.gson.*;
//import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
//import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
//import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaComputeVo;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaNetworkVo;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.QuotaVolumnVo;
//import com.okestro.symphony.dashboard.api.openstack.baremetal.svc.BareMetalService;
//import com.okestro.symphony.dashboard.api.openstack.dashboard.svc.impl.DashboardServiceImpl;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.*;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.svc.OctaviaService;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.openstack4j.api.OSClient;
//import org.openstack4j.api.compute.ComputeService;
//import org.openstack4j.api.networking.NetworkingService;
//import org.openstack4j.api.storage.BlockStorageService;
//import org.openstack4j.model.compute.QuotaSet;
//import org.openstack4j.model.network.*;
//import org.openstack4j.model.octavia.*;
//import org.openstack4j.model.storage.block.BlockQuotaSet;
//import org.openstack4j.openstack.octavia.domain.ListItem;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//
//import javax.net.ssl.SSLException;
//import java.net.ConnectException;
//import java.util.ArrayList;
//import java.util.List;
//
//
//@Slf4j
//@Service("octaviaServiceImpl")
//@Transactional
//public class OctaviaServiceImpl implements OctaviaService {
//
//    @Autowired
//    OpenStackConnectionService openStackConnectionService;
//
//    @Autowired
//    BareMetalService bareMetalService;
//
//    @Autowired
//    AuditLogService auditLogService;
//
//    @Autowired
//    MessageSource messageSource;
//
//    @Autowired
//    DashboardServiceImpl dashboardService;
//
//    @Autowired
//    WebsocketService websocketService;
//
//    /**
//     * 옥타비아 로드밸런스 리스트 조회
//     *
//     * @return
//     */
//    @Override
//    public LoadBalancerGroupVo getLoadBalancerList(String projectName, String userId) throws Exception {
//        LoadBalancerGroupVo loadBalancerGroupVo = new LoadBalancerGroupVo();
//        try {
//            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
//            WebClient webClient = getWebClient(osClient, projectName, userId);
//            // get resource
//            String resultData = webClient.get()
//                    .uri("/v2.0/lbaas/loadbalancers")
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .log()
//                    .block();
//
//            JsonObject jsonObject = new JsonParser().parse(resultData).getAsJsonObject();
//            loadBalancerGroupVo = gson.fromJson(jsonObject, LoadBalancerGroupVo.class);
//
//            List<LoadBalancerVo> loadbalancers = new ArrayList<LoadBalancerVo>();
//            for (LoadBalancerVo loadbalancer : loadBalancerGroupVo.getLoadbalancers()) {
//                if (loadbalancer.getTenantId().equalsIgnoreCase(osClient.getToken().getProject().getId())) {
//                    loadbalancers.add(loadbalancer);
//                }
//            }
//
//            loadBalancerGroupVo.setLoadbalancers(loadbalancers);
//
//
//            for (LoadBalancerVo loadbalancer : loadBalancerGroupVo.getLoadbalancers()) {
//                for (ListenerVo listener : loadbalancer.getListeners()) {
//                    ListenerV2 listenerV2 = osClient.octavia().listenerV2().get(listener.getId());
//                    listener.setName(listenerV2.getName());
//                    listener.setProtocol(listenerV2.getProtocol().toString());
//                    listener.setProtocolPort(listenerV2.getProtocolPort());
//                }
//            }
//
//        } catch (Exception e) {
//            throw e;
//        }
//
//        return loadBalancerGroupVo;
//    }
//
//    /**
//     * 전체 서브넷 조회
//     *
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public SelectBoxVo getSubnetList(String projectName, String userId) throws Exception {
////        return null;
////    }
//
//    /**
//     * 로드밸런서에 연결할 수 있는 가상머신 목록을 조회
//     *
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public List<MemberVo> getConnectionVMList(String projectName, String userId) throws Exception {
////        List<MemberVo> memberVoList = new ArrayList<MemberVo>();
////
////        try {
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
////            List<? extends Port> portList = osClient.networking().port().list();
////            List<? extends Subnet> subnetList = osClient.networking().subnet().list();
////            List<? extends Server> serverList = osClient.compute().servers().list();
////
////            for (Port port : portList) {
////                Set<? extends IP> ips = port.getFixedIps();
////
////                if (port.getDeviceId() != null) {
////                    MemberVo memberVo = new MemberVo();
////                    for (Server server : serverList) {
////                        if (port.getDeviceId().equalsIgnoreCase(server.getId())) {
////                            memberVo.setPortId(port.getId());
////                            memberVo.setVmName(server.getName());
////                            memberVo.setDeviceId(port.getDeviceId());
////                            memberVo.setDeviceOwner(port.getDeviceOwner());
////
////                            for (IP ip : ips) {
////                                memberVo.setAddress(ip.getIpAddress());
////                                memberVo.setSubnetId(ip.getSubnetId());
////
////                                for (Subnet subnet : subnetList) {
////                                    if (ip.getSubnetId().equalsIgnoreCase(subnet.getId())) {
////                                        memberVo.setSubnetName(subnet.getName());
////                                    }
////                                }
////
////                            }
////                            memberVo.setCrud("R");
////                            memberVoList.add(memberVo);
////                        }
////                    }
////                }
////            }
////
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////
////        return memberVoList;
////    }
//
//    /**
//     * 로드밸런서 상세
//     *
//     * @param loadBalancerVo
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public LoadBalancerVo getLoadBalancerDetail(LoadBalancerVo loadBalancerVo) throws Exception {
////        try {
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(loadBalancerVo.getProjectName(), loadBalancerVo.getUserId());
////            LoadBalancerV2 loadBalancerV2 = osClient.octavia().loadBalancerV2().get(loadBalancerVo.getId());
////            loadBalancerVo = loadBalancerVoConverter(loadBalancerV2);
////
////            List<? extends NetFloatingIP> netFloatingIPS = osClient.networking().floatingip().list();
////            for (NetFloatingIP netFloatingIP : netFloatingIPS) {
////                if (loadBalancerV2.getVipPortId() != null && netFloatingIP.getPortId() != null) {
////                    if (netFloatingIP.getPortId().equalsIgnoreCase(loadBalancerV2.getVipPortId())) {
////                        loadBalancerVo.setFloatingIp(netFloatingIP.getFloatingIpAddress());
////                    }
////                }
////            }
////
////
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////
////
////        return loadBalancerVo;
////    }
//
//    /**
//     * 로드밸런서 리스너 조회
//     *
//     * @param loadBalancerId
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public List<ListenerVo> getListenerList(String loadBalancerId, String projectName, String userId) throws Exception {
////        List<ListenerVo> listenerVoList = new ArrayList<ListenerVo>();
////        try {
////
////            List<MemberVo> memberVoList = this.getConnectionVMList(projectName, userId);
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
////
////            LoadBalancerV2 loadBalancerV2 = osClient.octavia().loadBalancerV2().get(loadBalancerId);
////
////            for (ListItem listenerItem : loadBalancerV2.getListeners()) {
////                ListenerV2 listener = osClient.octavia().listenerV2().get(listenerItem.getId());
////                ListenerVo listenerVo = new ListenerVo();
////
////                listenerVo.setId(listener.getId());
////                listenerVo.setProtocolPort(listener.getProtocolPort());
////                listenerVo.setProtocol(listener.getProtocol().toString());
////                listenerVo.setName(listener.getName());
////                listenerVo.setTenantId(listener.getProjectId());
////                listenerVo.setAdminStateUp(listener.isAdminStateUp());
////                listenerVo.setDefaultPoolId(listener.getDefaultPoolId());
////                listenerVo.setDescription(listener.getDescription());
////
////
////                String poolId = listenerVo.getDefaultPoolId();
////                if (poolId != null) {
////                    List<MemberVo> memberVos = new ArrayList<MemberVo>();
////                    List<? extends MemberV2> members = osClient.octavia().lbPoolV2().listMembers(poolId);
////
////                    if (members != null && members.size() != 0) {
////                        for (MemberV2 member : members) {
////                            MemberVo memberVo = new MemberVo();
////                            memberVo.setId(member.getId());
////                            memberVo.setAddress(member.getAddress());
////                            memberVo.setProtocolPort(member.getProtocolPort());
////                            memberVos.add(memberVo);
////                        }
////                        listenerVo.setMembers(memberVos);
////                    }
////
////                    LbPoolV2 lbPoolV2 = osClient.octavia().lbPoolV2().get(poolId);
////
////                    if (lbPoolV2.getHealthMonitorId() != null) {
////                        listenerVo.setHealthMonitorId(lbPoolV2.getHealthMonitorId());
////                        listenerVo.setHealthMonitorType(osClient.octavia().healthMonitorV2().get(lbPoolV2.getHealthMonitorId()).getType().toString());
////                        if (listenerVo.getHealthMonitorType().equalsIgnoreCase("HTTP")) {
////                            listenerVo.setHealthMonitorPath(osClient.octavia().healthMonitorV2().get(lbPoolV2.getHealthMonitorId()).getUrlPath());
////                        }
////                    } else {
////                        listenerVo.setHealthMonitorType("없음");
////                    }
////
////                    listenerVo.setLbMethod(lbPoolV2.getLbMethod().toString());
////                }
////
////                listenerVoList.add(listenerVo);
////            }
////
////            for (MemberVo member : memberVoList) {
////                for (ListenerVo listener : listenerVoList) {
////                    if (listener.getMembers() != null && listener.getMembers().size() > 0) {
////                        for (MemberVo memberVo : listener.getMembers()) {
////                            if (member.getAddress().equals(memberVo.getAddress())) {
////                                memberVo.setVmName(member.getVmName());
////                                memberVo.setSubnetName(member.getSubnetName());
////                            }
////                        }
////                    }
////                }
////            }
////
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////        return listenerVoList;
////    }
//
//    /**
//     * 맴버 상세조회
//     *
//     * @param memberVoList
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public List<MemberVo> getMemberDetail(List<MemberVo> memberVoList) throws Exception {
////        try {
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(memberVoList.get(0).getProjectName(), memberVoList.get(0).getUserId());
////            LoadBalancerV2 loadBalancerV2 = osClient.octavia().loadBalancerV2().get(memberVoList.get(0).getLoadBalancerId());
////            osClient.octavia().lbPoolV2().list();
////            for (MemberVo memberVo : memberVoList) {
////                MemberV2 memberV2 = osClient.octavia().lbPoolV2().getMember(memberVo.getPoolId(), memberVo.getId());
////                memberVo.setProtocolPort(memberV2.getProtocolPort());
////                memberVo.setAddress(memberV2.getAddress());
////                memberVo.setWeight(memberV2.getWeight());
////                memberVo.setPoolId(memberVo.getPoolId());
////            }
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////
////        return memberVoList;
////    }
//
//    /**
//     * 풀 리스트 조회
//     *
//     * @param poolVo
//     * @return
//     * @throws Exception
//     */
////    @Override
////    public List<PoolVo> getPoolList(PoolVo poolVo) throws Exception {
////
////        List<PoolVo> poolVoList = new ArrayList<PoolVo>();
////        List<MemberVo> members = new ArrayList<MemberVo>();
////
////        try {
////            List<ListenerVo> listenerVos = getListenerList(poolVo.getLoadBalancerId(), poolVo.getProjectName(), poolVo.getUserId());
////
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(poolVo.getProjectName(), poolVo.getUserId());
////            osClient.octavia().lbPoolV2().list();
////
////
////            for (ListenerVo listener : listenerVos) {
////                for (LbPoolV2 lbPoolV2 : osClient.octavia().lbPoolV2().list()) {
////                    if (lbPoolV2.getListeners().get(0).getId().equalsIgnoreCase(listener.getId())) {
////
////                        PoolVo pool = new PoolVo();
////                        pool.setId(lbPoolV2.getId());
////
////                        if (lbPoolV2.getMembers().size() != 0) {
////                            for (ListItem member : lbPoolV2.getMembers()) {
////                                MemberVo memberVo = new MemberVo();
////                                memberVo.setId(member.getId());
////
////                                members.add(memberVo);
////
////                                pool.setMembers(members);
////                            }
////                        }
////
////
////                        poolVoList.add(pool);
////                    }
////                }
////            }
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////
////        return poolVoList;
////    }
//
//
//    /**
//     * 포트 중복체크
//     */
////    @Override
////    public boolean duplicatedProtocolPort(LoadBalancerVo loadBalancerVo) throws Exception {
////
////        boolean result = true;
////        try {
////
////            List<ListenerVo> listenerVos = getListenerList(loadBalancerVo.getId(), loadBalancerVo.getProjectName(), loadBalancerVo.getUserId());
////
////            if (listenerVos.size() != 0) {
////                for (ListenerVo listener : listenerVos) {
////                    if (listener.getProtocol().equalsIgnoreCase(loadBalancerVo.getProtocol())) {
////                        if (listener.getProtocolPort() == loadBalancerVo.getPort()) {
////                            result = false;
////                        }
////                    }
////                }
////            }
////
////        } catch (Exception e) {
////            log.error(e.getMessage(), e);
////            throw e;
////        }
////
////        return result;
////    }
//
//
//    private QuotaComputeVo getQuotaCompute(String projectId, ComputeService compute) {
//        QuotaSet computeQuota = compute.quotaSets().get(projectId);
//
//        QuotaComputeVo result = new QuotaComputeVo();
//        result.setInstanceQuota(computeQuota.getInstances());
//        result.setCoreQuota(computeQuota.getCores());
//        result.setRamQuota(computeQuota.getRam());
//        result.setMetadataItemsQuota(computeQuota.getMetadataItems());
//        result.setInjectFilesQuota(computeQuota.getInjectedFiles());
//        result.setInjectedFileContentBytesQuota(computeQuota.getInjectedFileContentBytes());
//        result.setInjectedFilePathBytesQuota(computeQuota.getInjectedFilePathBytes());
//        result.setKeyPairsQuota(computeQuota.getKeyPairs());
//
//        return result;
//
//    }
//
//    private QuotaNetworkVo getQuotaNetwork(String projectId, NetworkingService networking) {
//        NetQuota netQuota = networking.quotas().get(projectId);
//
//        QuotaNetworkVo result = new QuotaNetworkVo();
//        result.setSubnetQuota(netQuota.getSubnet());
//        result.setRouterQuota(netQuota.getRouter());
//        result.setPortQuota(netQuota.getPort());
//        result.setNetworkQuota(netQuota.getNetwork());
//        result.setFloatingIpQuota(netQuota.getFloatingIP());
//        result.setSecurityGroupQuota(netQuota.getSecurityGroup());
//        result.setSecurityGroupRuleQuota(netQuota.getSecurityGroupRule());
//
//        return result;
//    }
//
//    private QuotaVolumnVo getQuotaVolumn(String projectId, BlockStorageService blockStorage) {
//        BlockQuotaSet blockQuotaSet = blockStorage.quotaSets().get(projectId);
//
//        QuotaVolumnVo result = new QuotaVolumnVo();
//        result.setVolumeQuota(blockQuotaSet.getVolumes());
//        result.setSnapshotQuota(blockQuotaSet.getSnapshots());
//        result.setGigabyteQuota(blockQuotaSet.getGigabytes());
//
//        return result;
//    }
//
//
//    /**
//     * 로드밸런스 VO변환
//     *
//     * @param loadBalancerV2
//     * @return
//     */
//    public LoadBalancerVo loadBalancerVoConverter(LoadBalancerV2 loadBalancerV2) {
//
//        LoadBalancerVo loadBalancerVo = new LoadBalancerVo();
//        loadBalancerVo.setName(loadBalancerV2.getName());
//        loadBalancerVo.setId(loadBalancerV2.getId());
//        loadBalancerVo.setProvider(loadBalancerV2.getProvider());
//        loadBalancerVo.setAdminStateUp(loadBalancerV2.isAdminStateUp());
//        loadBalancerVo.setDescription(loadBalancerV2.getDescription());
//        loadBalancerVo.setOperatingStatus(loadBalancerV2.getOperatingStatus().toString());
//        loadBalancerVo.setProvisioningStatus(loadBalancerV2.getProvisioningStatus().toString());
//        loadBalancerVo.setTenantId(loadBalancerV2.getProjectId());
//        loadBalancerVo.setVipPortId(loadBalancerV2.getVipPortId());
//        loadBalancerVo.setVipAddress(loadBalancerV2.getVipAddress());
//        loadBalancerVo.setVipSubnetId(loadBalancerV2.getVipSubnetId());
//
//        List<ListenerVo> listenerVoList = new ArrayList<ListenerVo>();
//        for (ListItem listener : loadBalancerV2.getListeners()) {
//            ListenerVo listenerVo = new ListenerVo();
//            listenerVo.setId(listener.getId());
//            listenerVoList.add(listenerVo);
//        }
//        loadBalancerVo.setListeners(listenerVoList);
//
//        return loadBalancerVo;
//    }
//
//
//    /**
//     * connect to openstack
//     * TODO-차후 변경 예정
//     */
//    private OSClient.OSClientV3 connect() throws ConnectException {
//        return openStackConnectionService.connect("https://os00.okestro.cld:5000/v3", "default", "egov", "egovuser", "okestro2018");
//    }
//
//    /**
//     * @param osClient
//     * @param projectName
//     * @return
//     * @throws SSLException
//     */
//    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId) throws SSLException {
//        WebClient webClient = null;
//
//        // get baremetal url
//        String bareMetalUrl = getLoadBalancerUrl(projectName, userId);
//        if (bareMetalUrl.startsWith("https://")) {
//            // set insecure
//            SslContext sslContext = SslContextBuilder
//                    .forClient()
//                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
//                    .build();
//
//            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
//
//            webClient = WebClient.builder()
//                    .clientConnector(new ReactorClientHttpConnector(httpClient))
//                    .baseUrl(bareMetalUrl)
//                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .build();
//        } else {
//            webClient = WebClient.builder()
//                    .baseUrl(bareMetalUrl)
//                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .build();
//        }
//
//        return webClient;
//    }
//
//    /**
//     * 로드밸런서 url 조회
//     */
//    private String getLoadBalancerUrl(String projectName, String userId) {
//        String url = null;
//
//        // get keystone url
////        url = openStackConnectionService.getKeystoneUrl(projectName, userId);
//
//        // get glance url
//        url = url.substring(0, url.lastIndexOf(":")) + ":9876";
//
////        url = "http://10.10.200.45:9876";
//
//        return url;
//    }
//
//}
