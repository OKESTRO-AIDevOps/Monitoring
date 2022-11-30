//package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.svc.impl;
//
//import com.google.gson.Gson;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.ProjectVo;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.*;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.ComputeVmEntity;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.repo.ComputeVmRepository;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity.InstancesEntity;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.repo.InstancesRepository;
//import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
//import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
//import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.*;
//import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.svc.ComputeVmService;
//import com.okestro.symphony.dashboard.api.openstack.neutron.network.floatingip.svc.FloatingIpService;
//import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
//import com.okestro.symphony.dashboard.util.DateUtil;
//import com.okestro.symphony.dashboard.util.WebclientUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.openstack4j.api.OSClient;
//import org.openstack4j.api.compute.ServerService;
//import org.openstack4j.model.compute.SecurityGroup;
//import org.openstack4j.model.compute.*;
//import org.openstack4j.model.identity.v3.Project;
//import org.openstack4j.model.image.v2.Image;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StopWatch;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import javax.net.ssl.SSLException;
//import java.net.ConnectException;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ForkJoinPool;
//
//@Slf4j
//@Service
//@Transactional
//public class ComputeVmServiceImpl implements ComputeVmService {
//
//    @Autowired
//    OpenStackConnectionService openStackConnectionService;
//
//    @Autowired
//    FloatingIpService floatingIpServiceImpl;
//    @Autowired
//    AuditLogService auditLogService;
//    @Autowired
//    MessageSource messageSource;
//    @Autowired
//    WebsocketService websocketService;
////    @Autowired
////    ComputeVmRepository computeVmRepository;
////    @Autowired
////    InstancesRepository instancesRepository;
//
//    @Autowired
//    DateUtil dateUtil;
//
//
//    @Value("${config.openstack.endpoint}")
//    String endpoint = null;
//    @Value("${config.openstack.domain}")
//    String domain = null;
//    @Value("${config.openstack.user}")
//    String user = null;
//    @Value("${config.openstack.passwd}")
//    String password = null;
//
//    /**
//     * Webclient를 통해 retrieve virtual machines in openstack
//     * @param dbSaveOpt
//     * @return
//     */
//    @Override
//    public List<VmVo> retrieveWcVms(boolean dbSaveOpt) {
//        List<VmVo> vms = new ArrayList<>();
//        List<ProjectVo> projectVoList = new ArrayList<>();
//        List<ComputeVmEntity> computeVmEntityList = new ArrayList<>();
//        ComputeVmEntity computeVmEntity = null;
//
//        // get current date time
//        String nowDate = DateUtil.now();
//        Date time = new Date();
//
//        // For calculating elapsed time
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        try {
//            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
//
//            WebclientUtil webclientUtil = new WebclientUtil();
//
//            // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
//            WebClient webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.KEYSTONE_IDENTITY);
//
//
//            // get proejct metric info
//            ProjectMetricVo prMetricInfos = null;
//            try {
//                prMetricInfos = webClient.get()
//                        .uri("/projects")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .retrieve()
//                        .bodyToMono(ProjectMetricVo.class).block();
//
//            } catch (Exception e) {
//                log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
//            }
//
//
//            // project정보 Vo 셋팅
//            for(ProjectMetricVo.Project project : prMetricInfos.getProjects()){
//                ProjectVo projectVo = new ProjectVo();
//
//                projectVo.setName(project.getName());
//                projectVo.setProjectName(project.getName());
//                projectVo.setId(project.getId());
//                projectVo.setDesc(project.getDesc());
//                projectVo.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//                projectVo.setEnabled((project.getLinks().getEnabled() == "false") ? false : true);
//                projectVo.setDomainId(project.getDomainId() == null ? "default" : project.getDomainId());
//                projectVo.setRetrievedDt(nowDate);
//
//                //Heat Stack 프로젝트 or 프로젝트 상태 비활성화 인 프로젝트는 리스트에서 제외.
//                if(project.getDesc().contains("Heat") || !project.isEnabled()){
//                    log.debug("## Y_TEST This Project is HeatStack Project or Disabled!["+project.getName()+"]");
//                }else{
//                    log.debug("## Y_TEST add projectVo name["+projectVo.getName()+"]");
//                    projectVoList.add(projectVo);
//                }
//            }
//
//            log.debug("## Y_TEST projectVoList Size["+projectVoList.size()+"]");
//
//            // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
//            webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.OS_QUOTA_SETS);
//
//
//
//            // vo List를 하나씩 꺼내서 vms list에 셋팅.
//            for(ProjectVo tmpProjectVo : projectVoList){
//            // DB 저장용 ENTITY
//            computeVmEntity = new ComputeVmEntity();
//            computeVmEntity.setProjectName(tmpProjectVo.getProjectName());
//            computeVmEntity.setProjectId(tmpProjectVo.getId());
////=================================================================== 여기는 project ID 단위로 간단한 인스턴스 별 사용량 보여줌.
//                // project 단위로 인스턴스(vm) 사용량 metric info 조회.
///*                ProjectUsageMetricVo prUsageMetricInfos = null;
//                try {
//                    prUsageMetricInfos = webClient.get()
//                            .uri("/v2.1/os-simple-tenant-usage/"+tmpProjectVo.getId())
//                            .accept(MediaType.APPLICATION_JSON)
//                            .retrieve()
//                            .bodyToMono(ProjectUsageMetricVo.class).block();
//
//                } catch (Exception e) {
//                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
//                }
//*/
////===================================================================
//
//                log.debug("## Y_TEST vm.getProjectName["+tmpProjectVo.getProjectName()+"]");
//
////=================================================================== 여기는 모든 인스턴스 별 사용량 보여줌.
//
//                // 모든 인스턴스(vm) metric info 조회.
//                ServerMetricVo serverUsageMetricInfos = null;
//
//                try {
//                    serverUsageMetricInfos = webClient.get()
//                            .uri("/v2.1/servers/detail?all_tenants=1&project_id="+tmpProjectVo.getId())
//                            .accept(MediaType.APPLICATION_JSON)
//                            .retrieve()
//                            .bodyToMono(ServerMetricVo.class).block();
//                } catch (Exception e) {
//                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
//                }
//
//
//                // 모든 인스턴스(vm)의 정보를 VmEntity와 VmVo에 셋팅.
//                for (ServerMetricVo.Server server : serverUsageMetricInfos.getServers()) {
//                    // 특정 인스턴스(vm)의 image metric info 조회. (특정이미지 정보조회)
//                    ImageMetricVo imageMetricInfos = null;
//                    // 특정 인스턴스(vm)의 flavor metric info 조회. (특정항당량 정보조회)
//                    FlavorMetricVo flavorMetricInfos = null;
//
//                    // Image가 존재할때만 조회.
//                    if(server.getImage() != null){
//                        try {
//                            imageMetricInfos = webClient.get()
//                                    .uri("/v2/images/"+server.getImage().getId())
//                                    .accept(MediaType.APPLICATION_JSON)
//                                    .retrieve()
//                                    .bodyToMono(ImageMetricVo.class).block();
//                        } catch (Exception e) {
//                            log.error("Webclient로 images 호출간 응답 오류 발생! [" + e.getMessage()+"]");
//                        }
//                    }
//
//                    // Flavor가 존재할때만 조회.
//                    if(server.getFlavor() != null){
//                        try {
//                            flavorMetricInfos = webClient.get()
//                                    .uri("/v2/flavors/"+server.getFlavor().getId())
//                                    .accept(MediaType.APPLICATION_JSON)
//                                    .retrieve()
//                                    .bodyToMono(FlavorMetricVo.class).block();
//                        } catch (Exception e) {
//                            log.error("Webclient로 flavors 호출간 응답 오류 발생! [" + e.getMessage()+"]");
//                        }
//                    }
//
//                    // vmEntity 설정.
//                    computeVmEntity.setHostId(server.getHostId());
//                    computeVmEntity.setHostName(server.getOsHost());
//                    computeVmEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//                    computeVmEntity.setProviderName("openstack-1");
//                    computeVmEntity.setVmId(server.getId());
//                    computeVmEntity.setVmName(server.getName());
//                    computeVmEntity.setCreatDt(server.getCreated());
//                    computeVmEntity.setPowerState(server.getPowerState() == 1 ? "active" : "down");
//                    computeVmEntity.setAzName(server.getAvailabilityZone());
//                    computeVmEntity.setVmState(server.getVmState());
//                    computeVmEntity.setCpu(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getVcpus());
//                    computeVmEntity.setRam(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getRam());
//                    computeVmEntity.setDisk(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getDisk());
//                    computeVmEntity.setCollectDt(time);
//
//                    // vmVo 설정.
//                    VmVo instance = getWcInstance(server, computeVmEntity.getProjectName());
//                    // vmVo의 Image, Flavor 설정.
//                    instance.setImageName(imageMetricInfos == null ? null : imageMetricInfos.getImage().getName());
//                    instance.setFlavorName(flavorMetricInfos == null ? null : flavorMetricInfos.getFlavor().getName());
//                    instance.setCpu(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getVcpus());
//                    instance.setRam(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getRam());
//                    instance.setDisk(flavorMetricInfos == null ? 0 : (int)flavorMetricInfos.getFlavor().getDisk());
//
//                    if(!vms.contains(instance)) {
//                        vms.add(instance);
//                    }
//                    computeVmEntityList.add(computeVmEntity);
//                }
//
//            }
//            log.debug("## Y_TEST computeVmEntityList 사이즈 ["+computeVmEntityList.size()+"]");
//            for(ComputeVmEntity tmp : computeVmEntityList){
//                log.debug("## Y_TEST computeVm.ProjectName["+tmp.getProjectName()+"], ID ["+tmp.getProjectId()+"]");
//            }
//            if(dbSaveOpt == true){
//                // insert VmEntity
////                computeVmRepository.saveAll(computeVmEntityList);
//                log.debug("## Y_TEST DB Insert Option On!");
//            }
//
//            Gson vmsGson = new Gson();
//            String gsonStr = vmsGson.toJson(vms);
//            JSONArray tmpJArr = new JSONArray(gsonStr);
//
//
//            log.debug("========Response Convert Gson================");
//            log.debug("gson Result["+tmpJArr.toString(1)+"]");
//
//            // end time
//            log.debug("========retriveVms(Webclient)================");
//            if (stopWatch.isRunning()) {
//                stopWatch.stop();
//            }
//            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
////            websocketService.sendNotice(WsNoticeLevel.ERROR, messageSource.getMessage(OpenStackConstant.WEBSOCKET_REQUEST_OPENSTACK_SERVER_ERROR, null, Locale.KOREA), vm.getName());
//        } catch (ConnectException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
////            websocketService.sendNotice(WsNoticeLevel.ERROR, messageSource.getMessage(OpenStackConstant.WEBSOCKET_REQUEST_OPENSTACK_CONNECTION_ERROR, null, Locale.KOREA), vm.getName());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
//        } catch (SSLException e) {
//            e.printStackTrace();
//        } finally {
//            openStackConnectionService.close();
//        }
//
//        return vms;
//    }
//
//
//    /**
//     * SDK를 통해 retrieve virtual machines in openstack (병렬처리)
//     * @param dbSaveOpt
//     * @return
//     */
//    @Override
//    public List<VmVo> retrieveVms(boolean dbSaveOpt) {
////        VmVo vm = new VmVo();
//        List<VmVo> vms = new ArrayList<>();
//        List<? extends Project> projectList = new ArrayList<>();
//        List<com.okestro.symphony.dashboard.cmm.model.ProjectVo> projectVoList = new ArrayList<>();
//        List<ComputeVmEntity> computeVmEntityList = new ArrayList<>();
//        com.okestro.symphony.dashboard.cmm.model.ProjectVo projectVo = null;
//        // For calculating elapsed time
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        // current Date
//        Date time = new Date();
//
//        try {
//            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
//            // get openstack client
//            projectList = osClient.identity().projects().list();
//
//            log.debug("## Y_TEST projectList Size["+projectList.size()+"]");
//
//            for(Project project : projectList){
//                projectVo = new com.okestro.symphony.dashboard.cmm.model.ProjectVo();
//
//                projectVo.setProjectId(project.getId());
//                projectVo.setProjectName(project.getName());
//                projectVo.setDomain(((project.getDomain() == null) ? "" : project.getDomain().getName()));
//                projectVo.setProject(project.getName());
//                projectVo.setEndpoint(project.getLinks().get("self"));
//
//                //Heat Stack 프로젝트 or 프로젝트 상태 비활성화 인 프로젝트는 리스트에서 제외.
//                if(project.getDescription().contains("Heat") || !project.isEnabled()){
//                    log.debug("## Y_TEST This Project is HeatStack Project or Disabled!["+project.getName()+"]");
//                }else{
//                    projectVoList.add(projectVo);
//                }
//            }
//
//            log.debug("## Y_TEST ProjectVoList Size["+projectList.size()+"]");
//
//            ForkJoinPool myPool = new ForkJoinPool(4);
//            myPool.submit(() -> {
//                //다중스레드 병렬처리.
//                projectVoList.parallelStream().forEach(project -> {
//                    VmVo vm = new VmVo();
//                    OSClient.OSClientV3 os = null;
//                    ComputeVmEntity computeVmEntity = new ComputeVmEntity();
//                    try {
//                        vm.setProjectName(project.getProjectName());
//
//                        log.debug("## Y_TEST vm.getProjectName["+vm.getProjectName()+"]");
//                        os = openStackConnectionService.connect(endpoint,domain,vm.getProjectName(),user,password);
//                    } catch (ConnectException e) {
//                        e.printStackTrace();
//                    }
//
//                    // vmEntity 설정.
//                    computeVmEntity.setProjectName(project.getProjectName());
//                    computeVmEntity.setProjectId(project.getProjectId());
//
//
//                    ServerService serverService = os.compute().servers();
//
//                    List<? extends Server> servers = serverService.list();
//
//                    List<? extends Image> images = os.imagesV2().list();
//
//                    List<? extends Flavor> flavors = os.compute().flavors().list();
//
//                    // set vm
//                    for (Server server : servers) {
//                        // vmEntity 설정.
//                        computeVmEntity.setHostId(server.getHostId());
//                        computeVmEntity.setHostName(server.getHost());
//                        computeVmEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//                        computeVmEntity.setProviderName("openstack-1");
//                        computeVmEntity.setVmId(server.getId());
//                        computeVmEntity.setVmName(server.getName());
//                        computeVmEntity.setCreatDt(server.getCreated());
//                        computeVmEntity.setPowerState(server.getPowerState());
//                        computeVmEntity.setAzName(server.getAvailabilityZone());
//                        computeVmEntity.setVmState(server.getVmState());
//                        computeVmEntity.setCollectDt(time);
//
//                        VmVo instance = getInstance(server, vm.getProjectName());
//
//                        for(Image image : images) {
//                            if(image.getId().equals(instance.getImageId())) {
//                                instance.setImageName(image.getName());
//                            }
//                        }
//
//
//                        for(Flavor flavor : flavors) {
//                            if(flavor.getId().equals(instance.getFlavorId())) {
//                                instance.setFlavorName(flavor.getName());
//                                instance.setCpu(flavor.getVcpus());
//                                instance.setRam(flavor.getRam());
//                                instance.setDisk(flavor.getDisk());
//                                computeVmEntity.setCpu(flavor.getVcpus());
//                                computeVmEntity.setRam(flavor.getRam());
//                                computeVmEntity.setDisk(flavor.getDisk());
//
//                            }
//                        }
//                        if(!vms.contains(instance)) {
//                            vms.add(instance);
//                        }
//                        computeVmEntityList.add(computeVmEntity);
//                    }
//                });
//            }).get();
//            myPool.shutdown();
//            log.debug("threadPool isShutdown["+myPool.isShutdown()+"]");
//            log.debug("threadPool isTerminated["+myPool.isTerminated()+"]");
//            log.debug("threadPool isTerminating["+myPool.isTerminating()+"]");
//
//
//
//            // ===========================직렬 처리 (일반 for문)==========================
//            // vo List를 하나씩 꺼내서 vms list에 셋팅.
///*            for(com.okestro.symphony.dashboard.cmm.model.ProjectVo vo : projectVoList){
//                computeVmEntity = new ComputeVmEntity();
//                vm.setProjectName(vo.getProjectName());
//
//                log.debug("## Y_TEST vm.getProjectName["+vm.getProjectName()+"]");
//                osClient = openStackConnectionService.connect(endpoint,domain,vm.getProjectName(),user,password);
////                time = new Date(); // 원래 조회마다 현재시간 셋팅해서 넣어주려고했는데 API 조회 시간이 오래걸려서 유종팀장님 요청으로 조회 시작시간으로 수정.
//
//                // vmEntity 설정.
//                computeVmEntity.setProjectName(vo.getProjectName());
//                computeVmEntity.setProjectId(vo.getProjectId());
//
//
//                ServerService serverService = osClient.compute().servers();
//
//
//                List<? extends Server> servers = serverService.list();
//
//                List<? extends Image> images = osClient.imagesV2().list();
//
//                List<? extends Flavor> flavors = osClient.compute().flavors().list();
//
//                // set vm
//                for (Server server : servers) {
//                    // vmEntity 설정.
//                    computeVmEntity.setHostId(server.getHostId());
//                    computeVmEntity.setHostName(server.getHost());
//                    computeVmEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
//                    computeVmEntity.setProviderName("openstack-1");
//                    computeVmEntity.setVmId(server.getId());
//                    computeVmEntity.setVmName(server.getName());
//                    computeVmEntity.setCreatDt(server.getCreated());
//                    computeVmEntity.setPowerState(server.getPowerState());
//                    computeVmEntity.setAzName(server.getAvailabilityZone());
//                    computeVmEntity.setVmState(server.getVmState());
//                    computeVmEntity.setCollectDt(time);
//
//                    VmVo instance = getInstance(server, vm.getProjectName());
//
//                    for(Image image : images) {
//                        if(image.getId().equals(instance.getImageId())) {
//                            instance.setImageName(image.getName());
//                        }
//                    }
//
//
//                    for(Flavor flavor : flavors) {
//                        if(flavor.getId().equals(instance.getFlavorId())) {
//                            instance.setFlavorName(flavor.getName());
//                            instance.setCpu(flavor.getVcpus());
//                            instance.setRam(flavor.getRam());
//                            instance.setDisk(flavor.getDisk());
//                            computeVmEntity.setCpu(flavor.getVcpus());
//                            computeVmEntity.setRam(flavor.getRam());
//                            computeVmEntity.setDisk(flavor.getDisk());
//
//                        }
//                    }
//
//
//                    if(!vms.contains(instance)) {
//                        vms.add(instance);
//                    }
//                    computeVmEntityList.add(computeVmEntity);
//                }
//            }
//*/
//            // ===========================직렬 처리 (일반 for문)==========================
//            if(dbSaveOpt == true){
//                // insert VmEntity
////                computeVmRepository.saveAll(computeVmEntityList);
//                log.debug("## Y_TEST DB Insert Option On!");
//            }
//
//            // Test용 데이터 쉽게 보기위해 작성.
//            Gson vmsGson = new Gson();
//            String gsonStr = vmsGson.toJson(vms);
//            JSONArray tmpJArr = new JSONArray(gsonStr);
//
//
//            log.debug("========Response Convert Gson================");
//            log.debug("gson Result["+tmpJArr.toString(1)+"]");
//
//            // end time
//            log.debug("========retriveVms(SDK)================");
//            if (stopWatch.isRunning()) {
//                stopWatch.stop();
//            }
//            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");
//            log.debug("## Y_TEST result Size["+vms.size()+"]");
//
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
////            websocketService.sendNotice(WsNoticeLevel.ERROR, messageSource.getMessage(OpenStackConstant.WEBSOCKET_REQUEST_OPENSTACK_SERVER_ERROR, null, Locale.KOREA), vm.getName());
//        } catch (ConnectException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
////            websocketService.sendNotice(WsNoticeLevel.ERROR, messageSource.getMessage(OpenStackConstant.WEBSOCKET_REQUEST_OPENSTACK_CONNECTION_ERROR, null, Locale.KOREA), vm.getName());
//        } catch (JSONException e) {
//            e.printStackTrace();
//            log.error(e.getMessage(), e);
//        } catch (ExecutionException e) {
//            log.error("## Thread 병렬처리 수행간 에러발생! 내용["+e.getMessage()+"]");
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            openStackConnectionService.close();
//        }
//
//        return vms;
//    }
//
//    /**
//     * Symphony MariaDB에 instance 특정 기간 조건 조회.
//     * @param sTime
//     * @param eTime
//     * @return List<InstancesEntity>
//     */
//    @Override
//    public List<InstancesEntity> betweenListAllInstances(LocalDateTime sTime, LocalDateTime eTime){
//        log.debug("## Y_TEST startDataTime ["+sTime+"]");
//        log.debug("## Y_TEST endDataTime ["+eTime+"]");
//
//
////        return  instancesRepository.findAllByCreatedAtBetween(sTime,eTime);
//        return null;
//    }
//
//    /**
//     * openstack-mariaDB 조회 (target table : nova.instances) -> 사용 못함(openstack mariadb nova.instances 조회용이여서 해당 controller 에 맞지 않음)
//     * */
////    @Override
////    public List<InstancesEntity> listAllInstances() {
////        return instancesRepository.findAll();
////    }
//
//
//    private VmVo getInstance(Server server, String projectName) {
//        VmVo instance = new VmVo();
//
////        instance.setProviderId(providerId);
////        instance.setProviderName(providerName);
//        instance.setProjectName(projectName);
//
//        instance.setHostId(server.getHostId());
//        instance.setHostName(server.getHost());
//
//        instance.setId(server.getId());
//        instance.setName(server.getName());
//        instance.setPowerStatus(server.getPowerState());
//        instance.setStatus(server.getStatus().value());
//
//        instance.setTaskState(server.getTaskState());
//
////        System.out.println("server.getPowerState:" + server.getPowerState()); // 전원상태
////        System.out.println("server.getStatus().value:" + server.getStatus().value()); // 상태
////        System.out.println("server.getTaskState:" + server.getTaskState()); // 작업 - 사용할까?
//
////        instance.setDisk(server.getDiskConfig().name()); // 사용 안함
//        instance.setAvailabilityZone(server.getAvailabilityZone());
////        instance.setCreated(server.getCreated().getTime());
//        instance.setLaunched(server.getLaunchedAt() != null ? server.getLaunchedAt().getTime() : 0);
//        instance.setImageId(server.getImageId());
//        instance.setFlavorId(server.getFlavorId());
//        instance.setKeyName(server.getKeyName());
//
//        // 20.11.17 jd.eom 볼륨일 경우 이미지 가져오기
//        List<String> volumeList = server.getOsExtendedVolumesAttached();
//        if(volumeList!=null && volumeList.size()>0){
////            String mainVolumeId = volumeList.get(0);
////            instance.setVolumeId(mainVolumeId);
//              instance.setVolumeId(volumeList);
//        }
//
//        // 20.10.15 jd.eom 보안그룹 가져오기위함.
//        List<? extends SecurityGroup> sgExList = server.getSecurityGroups();
//        List<SecGroupVo> sgList = new ArrayList<>();
//        if(sgExList!=null){
//            for(SecurityGroup vo : sgExList) {
//                SecGroupVo sgVo = new SecGroupVo();
//                sgVo.setName(vo.getName());
//                sgList.add(sgVo);
//            }
//        }
//        instance.setSecGroupList(sgList);
//
////        instance.setImageInfo(server.getImage() != null ? new String[]{server.getImage().getId(), server.getImage().getName()} : new String[]{"-", "-"});
////        instance.setFlavorInfo(new String[]{server.getFlavor().getId(), server.getFlavor().getName()});
//
//        server.getAddresses().getAddresses().forEach((key, value) -> {
//            for (Address address : value) {
//                instance.setMacAddress(address.getMacAddr());
//                if (address.getType().equals("fixed")) {
//                    List<String> fixedAddresses = instance.getFixedIpAddresses() == null ? new ArrayList<>() : instance.getFixedIpAddresses();
//                    fixedAddresses.add(address.getAddr());
//                    instance.setFixedIpAddresses(fixedAddresses);
//                } else if (address.getType().equals("floating")) {
//                    List<String> floatingAddresses = instance.getFloatingIpAddresses() == null ? new ArrayList<>() : instance.getFloatingIpAddresses();
//                    floatingAddresses.add(address.getAddr());
//                    instance.setFloatingIpAddresses(floatingAddresses);
//                }
//
//            }
//        });
//
//        return instance;
//    }
//
//
//    /**
//     * Webclient를 통해 얻은 serverVo로 instance 정보 셋팅.
//     * @param server
//     * @param projectName
//     * @return
//     */
//    private VmVo getWcInstance(ServerMetricVo.Server server, String projectName) {
//        VmVo instance = new VmVo();
//
//        instance.setProjectName(projectName);
//
//        instance.setHostId(server.getHostId());
//        instance.setHostName(server.getOsHost());
//        instance.setId(server.getId());
//        instance.setName(server.getName());
//        instance.setPowerStatus(server.getPowerState() == 1 ? "active" : "down");
//        instance.setStatus(server.getStatus());
//        instance.setTaskState(server.getTaskState());
//        instance.setAvailabilityZone(server.getAvailabilityZone());
//        instance.setLaunched(server.getLaunchedAt() != null ? server.getLaunchedAt().getTime() : 0);
//        instance.setImageId(server.getImage() == null ? null : server.getImage().getId() );
//        instance.setFlavorId(server.getFlavor() == null ? null : server.getFlavor().getId());
//        instance.setKeyName(server.getKeyName());
//        // 확장 volume 셋팅
//        if(server.getExtenedVolumes() != null){
//            List<String> volumeIds = new ArrayList<>();
//            for (ServerMetricVo.ExtenedVolumes tmpExtendVolume : server.getExtenedVolumes()){
//                volumeIds.add(tmpExtendVolume.getId());
//            }
//            instance.setVolumeId(volumeIds);
//        }
//
//        if(server.getSecurityGroups() != null){
//            instance.setSecGroupList(server.getSecurityGroups());
//        }
//
//        server.getAddresses().getAddresses().forEach((key, value) -> {
//            for (Address address : value) {
//                instance.setMacAddress(address.getMacAddr());
//                if (address.getType().equals("fixed")) {
//                    List<String> fixedAddresses = instance.getFixedIpAddresses() == null ? new ArrayList<>() : instance.getFixedIpAddresses();
//                    fixedAddresses.add(address.getAddr());
//                    instance.setFixedIpAddresses(fixedAddresses);
//                } else if (address.getType().equals("floating")) {
//                    List<String> floatingAddresses = instance.getFloatingIpAddresses() == null ? new ArrayList<>() : instance.getFloatingIpAddresses();
//                    floatingAddresses.add(address.getAddr());
//                    instance.setFloatingIpAddresses(floatingAddresses);
//                }
//
//            }
//        });
//
//        return instance;
//    }
//
//
//    private boolean checkIsInstanceSnapshotYn(org.openstack4j.model.compute.Image imageInfo){
//        if(imageInfo==null) return false;
//        Map<String, Object> imageInfoMeta = imageInfo.getMetaData();
//        Object block_device_mapping = imageInfoMeta.get("block_device_mapping");
//        boolean isSnapshotYn = false;
//        if(block_device_mapping!=null) {
//            List tmpList = (List) block_device_mapping;
//            if(tmpList!=null && tmpList.size()==1){
//                Map<String, Object> tmpMap = (Map<String, Object>) tmpList.get(0);
//                if("snapshot".equalsIgnoreCase(String.valueOf(tmpMap.get("source_type")))){
//                    isSnapshotYn = true;
//                }
//            }
//        }
//        return imageInfo.isSnapshot() || isSnapshotYn;
//    }
//
//}
