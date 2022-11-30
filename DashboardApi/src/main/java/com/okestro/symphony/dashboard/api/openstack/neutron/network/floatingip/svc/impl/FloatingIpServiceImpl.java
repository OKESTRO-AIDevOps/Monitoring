///*
// * Developed by sychoi on 2020-07-16
// * Last modified 2020-07-16 14:11:43
// */
//
//package com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.svc.impl;
//
//import com.google.gson.*;
//import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
//import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
//import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
//import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
//import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpConnentVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpGroupVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpQuotaVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.model.FloatingIpVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.svc.FloatingIpService;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model.PortVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.network.svc.NetworkService;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.LoadBalancerGroupVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.model.LoadBalancerVo;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.octavia.svc.OctaviaService;
//import io.netty.handler.ssl.SslContext;
//import io.netty.handler.ssl.SslContextBuilder;
//import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.openstack4j.api.OSClient;
//import org.openstack4j.model.compute.Server;
//import org.openstack4j.model.network.*;
//import org.openstack4j.model.network.ext.LoadBalancerV2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.reactive.ReactorClientHttpConnector;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.netty.http.client.HttpClient;
//
//import javax.net.ssl.SSLException;
//import java.net.ConnectException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//
//
//@Service("floatingIpServiceImpl")
//@Slf4j
//public class FloatingIpServiceImpl implements FloatingIpService {
//
//    @Autowired
//    OpenStackConnectionService openStackConnectionService;
//
//    @Autowired
//    OctaviaService octaviaService;
//
//    @Autowired
//    NetworkService networkService;
//
//    @Autowired
//    AuditLogService auditLogService;
//
//    @Autowired
//    MessageSource messageSource;
//
//    @Autowired
//    WebsocketService websocketService;
//
//    /**
//     * 유동 IP 목록 조회
//     *
//     * @return
//     */
//    @Override
//    public OpenStackResultVo retrieveNetFloatingIps(FloatingIpVo floatingIpVo) {
//        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
//        List<FloatingIpVo> floatingIpVoList = new ArrayList<FloatingIpVo>();
//        List<FloatingIpVo> filteredList = new ArrayList<>();
//
//        try {
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(floatingIpVo.getProjectName(), floatingIpVo.getUserId());
//            // projectId
//            String projectId = osClient.getToken().getProject().getId();
//
//            try {
//                Thread.sleep(1 * 1000);
//                List<? extends Port> ports = osClient.networking().port().list();
//                List<? extends LoadBalancerV2> loadBalancerV2s = osClient.networking().lbaasV2().loadbalancer().list();
//                List<? extends Network> networks = osClient.networking().network().list();
//
//                Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
//
//                WebClient webClient = getWebClient(osClient, floatingIpVo.getProjectName(), floatingIpVo.getUserId());
//
//                //로드벨런서 리스트 조회
//                LoadBalancerGroupVo loadBalancerGroupVo = octaviaService.getLoadBalancerList(floatingIpVo.getProjectName(), floatingIpVo.getUserId());
//
//
//                // get resource
//                String resultData = webClient.get()
//                        .uri("/v2.0/floatingips")
//                        .retrieve()
//                        .bodyToMono(String.class)
//                        .log()
//                        .block();
//
//                JsonObject jsonObject = JsonParser.parseString(resultData).getAsJsonObject();
//                FloatingIpGroupVo floatingIpGroupVo = gson.fromJson(jsonObject, FloatingIpGroupVo.class);
//                List<FloatingIpVo> tempFloatingIpVoList = floatingIpGroupVo.getFloatingips();
//
//                for (FloatingIpVo ipVo : tempFloatingIpVoList) {
//                    if (ipVo.getProjectId().equalsIgnoreCase(osClient.getToken().getProject().getId())) {
//                        // 속도 개선 로직
//                        if (ipVo.getPortId() != null) {
//                            Port port = null;
//                            for (Port po : ports) {
//                                if (po.getId().equalsIgnoreCase(ipVo.getPortId())) {
//                                    port = po;
//                                    break;
//                                }
//                            }
//
//                            if (port != null) {
//                                if (port.getDeviceOwner().equalsIgnoreCase(OpenStackConstant.COMPUTE_NOVA)) {
//                                    Server server = osClient.compute().servers().get(port.getDeviceId());
//                                    ipVo.setDeviceName(server.getName());
////                                } else if (port.getDeviceOwner().equalsIgnoreCase(OpenStackConstant.NEUTRON_LOADBALANCERV2)) {
////                                    LoadBalancerV2 loadBalancerV2 = null;
////
////                                    for (LoadBalancerV2 lb : loadBalancerV2s) {
////                                        if (lb.getId().equalsIgnoreCase(port.getDeviceId())) {
////                                            loadBalancerV2 = lb;
////                                            break;
////                                        }
////                                    }
////
////                                    if (loadBalancerV2 != null) {
////                                        ipVo.setDeviceName(loadBalancerV2.getName());
////                                    }
//                                }
//
//                                if (loadBalancerGroupVo != null && loadBalancerGroupVo.getLoadbalancers().size() != 0) {
//                                    for (LoadBalancerVo loadBalancer : loadBalancerGroupVo.getLoadbalancers()) {
//                                        if (loadBalancer.getVipPortId().equals(port.getId())) {
//                                            ipVo.setDeviceName("loadBalancer : " + loadBalancer.getName());
//                                        }
//                                    }
//                                }
//
//                            } else {
//                                ipVo.setDeviceName("-");
//                                ipVo.setFixedIpAddress("-");
//                                ipVo.setStatus("DOWN");
//                            }
//
//                            // 원래 로직
////                    if (ipVo.getPortId() != null) {
////                        Port port = osClient.networking().port().get(ipVo.getPortId());
////                        if (port.getDeviceOwner().equalsIgnoreCase("compute:nova")) {
////                            Server server = osClient.compute().servers().get(port.getDeviceId());
////                            ipVo.setDeviceName(server.getName());
////                        }
////
////                        if (port.getDeviceOwner().equalsIgnoreCase("neutron:LOADBALANCERV2")) {
////                            LoadBalancerV2 loadBalancerV2 = osClient.networking().lbaasV2().loadbalancer().get(port.getDeviceId());
////                            ipVo.setDeviceName(loadBalancerV2.getName());
////                        }
////
////                    } else {
////                        ipVo.setDeviceName("-");
////                        ipVo.setFixedIpAddress("-");
////                    }
//                        } else {
//                            ipVo.setDeviceName("-");
//                            ipVo.setFixedIpAddress("-");
//                            ipVo.setStatus("DOWN");
//                        }
//
//                        Network network = null;
//                        for (Network net : networks) {
//                            if (net.getId().equalsIgnoreCase(ipVo.getFloatingNetworkId())) {
//                                network = net;
//                                break;
//                            }
//                        }
//
//                        if (network != null) {
//                            ipVo.setFloatingNetworkName(network.getName());
//                        }
//                    }
//                    floatingIpVoList.add(ipVo);
//                }
//
//                //같은 tenantId(프로젝트) 인 데이터만 list에 보여줌 jh
//                for (int i = 0; i < floatingIpVoList.size(); i++) {
//                    if (floatingIpVoList.get(i).getTenantId().equals(projectId)) {
//                        filteredList.add(floatingIpVoList.get(i));
//                    }
//                }
////                openStackResultVo.setViewList(floatingIpVoList);
//                openStackResultVo.setViewList(filteredList);
//                openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
//
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//                openStackResultVo.setResultCode(OpenStackConstant.ERROR);
//                openStackResultVo.setResultMessage(e.getMessage());
//            }
//        } catch (ConnectException ce) {
//            log.error(ce.getMessage(), ce);
//            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
//            openStackResultVo.setResultMessage(ce.getMessage());
//        }
//
//        return openStackResultVo;
//    }
//
//    /**
//     * 선택된 유동IP에 연결할 수 있는 가상머신 리스트를 조회한다.
//     *
//     * @param floatingIpVo
//     * @return
//     */
////    @Override
////    public OpenStackResultVo retrieveConnectVmList(FloatingIpVo floatingIpVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        List<FloatingIpConnentVo> floatingIpConnentVoList = new ArrayList<FloatingIpConnentVo>();
////
////        try {
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(floatingIpVo.getProjectName(), floatingIpVo.getUserId());
////
////            try {
////
////                List<PortVo> portVoList = new ArrayList<>();
////                List<? extends Router> routers = osClient.networking().router().list();
////                List<? extends Port> ports = osClient.networking().port().list();
////
////                //로드벨런서 리스트 조회
////                LoadBalancerGroupVo loadBalancerGroupVo = octaviaService.getLoadBalancerList(floatingIpVo.getProjectName(), floatingIpVo.getUserId());
////
////                List<PortVo> portVos = new ArrayList<>();
////                for (Port po : ports) {
////                    if (po.getTenantId().equalsIgnoreCase(osClient.getToken().getProject().getId())) {
////                        PortVo vo = networkService.getPortToPortVo(po);
////                        portVos.add(vo);
////                    }
////                }
////
////                // port 중 deviceId가 router의 id와 일치하는 것을 찾아서(라우터에 연결된 port) 그 라우터에 등록된 서브넷을 공유하고 있는 port들 중 compute인지 로드밸런서인지 파악하여 장치명을 붙임.
////                for (Router router : routers) {
////                    if (router.getTenantId().equalsIgnoreCase(osClient.getToken().getProject().getId())) {
////                        for (PortVo portVo : portVos) {
////                            if (portVo.getDeviceId().equalsIgnoreCase(router.getId())) {
////                                if (portVo.getDeviceOwner().equalsIgnoreCase(OpenStackConstant.NETWORK_ROUTER_INTERFACE) || portVo.getDeviceOwner().equalsIgnoreCase(OpenStackConstant.NETWORK_ROUTER_INTERFACE_DISTRBUTED)) {
////                                    portVo.setDeviceType("internal");
////                                    portVoList.add(portVo);
////                                }
////                            }
////                        }
////                    }
////                }
////
////                for (PortVo portVo : portVoList) {
////                    for (Port port : ports) {
////                        Set<? extends IP> ips = port.getFixedIps();
////
////                        String portSubnetId = "";
////                        String portIp = "";
////
////                        for (IP ip : ips) {
////                            portSubnetId = ip.getSubnetId();
////                            portIp = ip.getIpAddress();
////                        }
////
////                        if (port.getTenantId().equalsIgnoreCase(osClient.getToken().getProject().getId()) && portVo.getFixedIps().get(0).getSubnetId().equalsIgnoreCase(portSubnetId)) {
////                            if (port.getDeviceOwner() != null) {
////                                FloatingIpConnentVo floatingIpConnentVo = new FloatingIpConnentVo();
////
////                                boolean setIp = false;
////
////                                Server server = osClient.compute().servers().get(port.getDeviceId());
//////                                LoadBalancerV2 loadBalancerV2 = null;
////
////                                if (server != null) {
////                                    floatingIpConnentVo.setVmName(server.getName());
////                                    setIp = true;
////                                }
////
////                                //로드밸런서도 유동IP 붙을 수 있게 리스트에 추가
////                                else {
////                                    if (loadBalancerGroupVo != null && loadBalancerGroupVo.getLoadbalancers().size() != 0) {
////                                        for (LoadBalancerVo loadBalancer : loadBalancerGroupVo.getLoadbalancers()) {
////                                            if (loadBalancer.getVipPortId().equals(port.getId())) {
////                                                floatingIpConnentVo.setVmName("loadBalancer : " + loadBalancer.getName());
////                                                setIp = true;
////                                            }
////                                        }
////                                    }
////
//////                                    loadBalancerV2 = osClient.networking().lbaasV2().loadbalancer().get(port.getDeviceId());
////                                }
////
//////                                if (loadBalancerV2 != null) {
//////                                    floatingIpConnentVo.setVmName(loadBalancerV2.getName());
//////                                    setIp = true;
//////                                }
////
////
////                                if (setIp) {
////                                    floatingIpConnentVo.setPortId(port.getId());
////                                    floatingIpConnentVo.setDeviceId(port.getDeviceId());
////                                    floatingIpConnentVo.setDeviceOwner(port.getDeviceOwner());
////                                    floatingIpConnentVo.setFloatingIpId(floatingIpVo.getId());
////                                    floatingIpConnentVo.setFloatingIp(floatingIpVo.getFloatingIpAddress());
////                                    floatingIpConnentVo.setVmIp(portIp);
////
////                                    floatingIpConnentVoList.add(floatingIpConnentVo);
////                                }
////                            }
////                        }
////                    }
////                }
////
////                openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////                openStackResultVo.setViewList(floatingIpConnentVoList);
////            } catch (Exception e) {
////                log.error(e.getMessage(), e);
////                openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////                openStackResultVo.setResultMessage(e.getMessage());
////            }
////        } catch (ConnectException ce) {
////            log.error(ce.getMessage(), ce);
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(ce.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//    /**
//     * @param floatingIpVo
//     * @return
//     */
////    @Override
////    public OpenStackResultVo getNetworkQuotas(FloatingIpVo floatingIpVo) {
////        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
////        List<FloatingIpQuotaVo> projectList = new ArrayList<FloatingIpQuotaVo>();
////
////        try {
////            OSClient.OSClientV3 osClient = openStackConnectionService.connect(floatingIpVo.getProjectName(), floatingIpVo.getUserId());
////            try {
////                List<? extends NetFloatingIP> netFloatingIps = osClient.networking().floatingip().list();
////                List<? extends Network> networks = osClient.networking().network().list();
////
////                for (Network network : networks) {
////                    if (network.getProviderPhyNet() != null) {
////                        FloatingIpQuotaVo floatingIpQuotaVo = new FloatingIpQuotaVo();
////                        floatingIpQuotaVo.setProjectId(floatingIpVo.getProjectId());
////                        projectList.add(floatingIpQuotaVo);
////
////                        for (NetFloatingIP netFloatingIP : netFloatingIps) {
////                            for (FloatingIpQuotaVo vo : projectList) {
////                                if (osClient.getToken().getProject().getId().equalsIgnoreCase(netFloatingIP.getTenantId())) {
////                                    vo.setFloatingIpUsed(vo.getFloatingIpUsed() + 1);
////                                }
////                            }
////                        }
////
////                        // TODO - ProjectService 쪽이 없어서 일단 임시로 만듦
////                        //                int floatingIpCnt = 0;
////                        NetQuota netQuota = osClient.networking().quotas().get(osClient.getToken().getProject().getId());
////                        for (FloatingIpQuotaVo vo : projectList) {
////                            //                    for (NetFloatingIP netFloatingIP : netFloatingIps) {
////                            //                        if (floatingIpVo.getProjectId().equals(netFloatingIP.getTenantId())) {
////                            //                            floatingIpCnt++;
////                            //                        }
////                            //                    }
////                            //                    vo.setFloatingIpQuota(floatingIpCnt);
////                            vo.setFloatingIpQuota(netQuota.getFloatingIP());
////                        }
////
////                        // TODO - Custom Pool 작업 (외부 허용이 여러개일때는 고려 안하고 만듦)
////                        if (network.isRouterExternal()) {
////                            floatingIpQuotaVo.getPool().setNetworkId(network.getId());
////                            floatingIpQuotaVo.getPool().setNetworkName(network.getName());
////                        }
////                    }
////                }
////
////                openStackResultVo.setResultCode(OpenStackConstant.SUCCESS);
////                openStackResultVo.setViewList(projectList);
////            } catch (Exception e) {
////                log.error(e.getMessage(), e);
////                openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////                openStackResultVo.setResultMessage(e.getMessage());
////            }
////        } catch (ConnectException ce) {
////            log.error(ce.getMessage(), ce);
////            openStackResultVo.setResultCode(OpenStackConstant.ERROR);
////            openStackResultVo.setResultMessage(ce.getMessage());
////        }
////
////        return openStackResultVo;
////    }
//
//    /**
//     * get web client
//     */
//    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId) throws
//            SSLException {
//        WebClient webClient = null;
//
//        // get baremetal url
//        String floatingIpUrl = getFloatingIpUrl(projectName, userId);
//
//        if (floatingIpUrl.startsWith("https://")) {
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
//                    .baseUrl(floatingIpUrl)
//                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .build();
//        } else {
//            webClient = WebClient.builder()
//                    .baseUrl(floatingIpUrl)
//                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .build();
//        }
//
//        return webClient;
//    }
//
//    /**
//     * get network(neutron) url
//     * 일반 사용자 계정으로 엔드포인트 url 조회가 안돼 keystone url을 편집하여 사용
//     */
//    private String getFloatingIpUrl(String projectName, String userId) {
//        String url = null;
//
//        // get keystone url
////        url = openStackConnectionService.getKeystoneUrl(projectName, userId);
//
//        // get neutron url
//        url = url.substring(0, url.lastIndexOf(":")) + ":9696";
////        url = "https://openstack.mgmt.dev.egovp.kr:9696";
//        return url;
//    }
//
//}
