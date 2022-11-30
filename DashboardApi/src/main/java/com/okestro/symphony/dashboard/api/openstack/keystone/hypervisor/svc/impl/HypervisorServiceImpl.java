package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.svc.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.repo.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.*;
import com.okestro.symphony.dashboard.cmm.constant.ElasticSearchConstant;
import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.svc.HypervisorService;
import com.okestro.symphony.dashboard.util.DateUtil;
import com.okestro.symphony.dashboard.util.OpenstackConUtil;
import com.okestro.symphony.dashboard.util.WebclientUtil;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HypervisorServiceImpl implements HypervisorService {
    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Autowired
    OpenstackConUtil openstackConUtil;

//    @Autowired
//    HostRepository hostRepository;
//
//    @Autowired
//    HostAggregateRepository hostAggregateRepository;
//
//    @Autowired
//    HostAggregateHostRepository hostAggregateHostRepository;

    @Autowired
    DateUtil dateUtil;


    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;


    /**
     * openstack API Webclient를 통해 모든 Hypervisor 리스트 조회.
     * @param dbSaveOpt
     * @return list
     */
    @Override
    public List<HostVo> listWcAllHypervisor(boolean dbSaveOpt) {
        // VoList
        List<HostVo> list = new ArrayList<>();
        // EntityList
        List<HostEntity> hostEntityList = new ArrayList<>();
        List<HostAggregateEntity> hostAggregateEntityList = new ArrayList<>();
        List<HostAggregateHostEntity> hostAggregateHostEntityArrayList = new ArrayList<>();
        // Entity
        HostEntity hostEntity = null;
        HostAggregateEntity hostAggregateEntity = null;
        HostAggregateHostEntity hostAggregateHostEntity = null;


        // webclient를 통해 응답받은 json데이터를 담기위한 변수.
        Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        WebclientUtil webclientUtil = new WebclientUtil();

        try {

            // connect to openstack
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);

            // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
            WebClient webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.OS_QUOTA_SETS);


            HypervisorMetricVo hypervisorMetricInfos = null;
            HostAggregateMetricVo hostAggregateMetricInfos = null;
            // get unix Time
            long epoch = System.currentTimeMillis();

            try {
                // hypervisor 상세 정보.
                hypervisorMetricInfos = webClient.get()
                        .uri("/v2.1/os-hypervisors/detail")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(HypervisorMetricVo.class).block();

                // hypervisor 상세 정보.
                hostAggregateMetricInfos = webClient.get()
                        .uri("/v2.1/os-aggregates")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(HostAggregateMetricVo.class).block();

                log.debug("## Y_TEST hostAggregate.size["+hostAggregateMetricInfos.getHostAggregates().size()+"]");

                // end time
                log.debug("========HypervisorList(Webclient)================");

            } catch (Exception e) {
                log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
            }


            // 조회한 hypervisor 정보 셋팅
            for(HypervisorMetricVo.Hypervisor hypervisor : hypervisorMetricInfos.getHypervisors()) {
                HostVo vo = new HostVo();
                hostEntity = new HostEntity();

                vo.setId(Integer.toString(hypervisor.getId()));
                vo.setName(hypervisor.getHypervisorHostname());
                vo.setTimestamp(String.valueOf(epoch));
                vo.setType(hypervisor.getHypervisorType());
                vo.setStatus(hypervisor.getStatus());
                vo.setState(hypervisor.getState());
                vo.setHostIp(hypervisor.getHostIp());
                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
                vo.setLeastDiskAvailable(hypervisor.getDiskAvailableLeast());
                vo.setHypervisorVersion(hypervisor.getHypervisorVersion());
                vo.setRunningVms(hypervisor.getRunningVms());
                vo.setVcpus(hypervisor.getVcpus());
                vo.setVcpusUsed(hypervisor.getVcpusUsed());
                vo.setLocalGb(hypervisor.getDiskAvailableLeast());
                vo.setLocalGbUsed(hypervisor.getLocalGbUsed());
                vo.setFreeDiskGb(hypervisor.getFreeDiskGb());
                vo.setMemoryMb(hypervisor.getMemoryMb());
                vo.setMemoryMbUsed(hypervisor.getMemoryMbUsed());
                vo.setFreeRam(hypervisor.getFreeRamMb());
                vo.setProviderName("openstack-1");



                hostEntity.setHostId(Integer.toString(hypervisor.getId()));
                hostEntity.setHostName(hypervisor.getHypervisorHostname());
                hostEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                hostEntity.setVmNum(hypervisor.getRunningVms());
                hostEntity.setVcpuTotal(hypervisor.getVcpus()+hypervisor.getVcpusUsed());
                hostEntity.setVcpuUsage(hypervisor.getVcpusUsed());
                hostEntity.setRamTotal(hypervisor.getFreeRamMb()+hypervisor.getMemoryMbUsed());
                hostEntity.setRamUsage(hypervisor.getMemoryMbUsed());
                hostEntity.setLocalStorageTotal(hypervisor.getDiskAvailableLeast()+hypervisor.getLocalGbUsed());
                hostEntity.setLocalStorageUsage(hypervisor.getLocalGbUsed());
                hostEntity.setState(hypervisor.getState());
                // hostEntity.setCollectDt(time);


                list.add(vo);
                hostEntityList.add(hostEntity);
            }

            // 조회한 hostAggregates 정보 셋팅
            for (HostAggregateMetricVo.HostAggregates hostAggregates : hostAggregateMetricInfos.getHostAggregates()){
                hostAggregateHostEntity = new HostAggregateHostEntity();

                // host집합 insert
                    hostAggregateEntity = new HostAggregateEntity();
                    log.debug("## Y_TEST HostAggregatesSize["+hostAggregateMetricInfos.getHostAggregates().size()+"]");
                    hostAggregateEntity.setHostAggregateId(Integer.toString(hostAggregates.getId()));
                    hostAggregateEntity.setHostAggregateName(hostAggregates.getName());
                    hostAggregateEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                    hostAggregateEntity.setAzName(hostAggregates.getAvailabilityZone());
                    // hostAggregateEntity.setCollectDt(time);

                    hostAggregateEntityList.add(hostAggregateEntity);
            }

            if(dbSaveOpt == true){
//                hostRepository.saveAll(hostEntityList);
//                hostAggregateRepository.saveAll(hostAggregateEntityList);
                log.debug("## Y_TEST DB Insert Option On!");
            }

            // end time
            // log.debug("========HypervisorList(Webclient)================");


        }catch (ConnectException e){
            // log
            log.error(e.getMessage(), e);
        }catch (Exception e){
            // log
            log.error(e.getMessage(), e);
        }

        return list;

    }

    /**
     * openstack API SDK를 통해 모든 Hypervisor 리스트 조회 후 ES에 인덱스 삽입.
     * @param dbSaveOpt
     * @return list
     */
    @Override
    public List<HostVo> listAllHypervisor(boolean dbSaveOpt) {
        // VoList
        List<HostVo> list = new ArrayList<>();
        // EntityList
        List<HostEntity> hostEntityList = new ArrayList<>();
        List<HostAggregateEntity> hostAggregateEntityList = new ArrayList<>();
        List<HostAggregateHostEntity> hostAggregateHostEntityArrayList = new ArrayList<>();
        // Entity
        HostEntity hostEntity = null;
        HostAggregateEntity hostAggregateEntity = null;
        HostAggregateHostEntity hostAggregateHostEntity = null;


        WebclientUtil webclientUtil = new WebclientUtil();

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
            // get unix Time
            long epoch = System.currentTimeMillis();

            // get hypervisors In a Webclient way
            // WebClient webClient = webclientUtil.getWebClient(osClient,projectNm,userId,"");


            // get hypervisors In a ApiClient way
            List<? extends Hypervisor> hypervisors = osClient.compute().hypervisors().list();
            List<? extends HostAggregate> hosts = osClient.compute().hostAggregates().list();
            log.debug("========HypervisorList(SDK)================");



            for(Hypervisor hypervisor : hypervisors) {
                HostVo vo = new HostVo();
                hostEntity = new HostEntity();
                hostAggregateHostEntity = new HostAggregateHostEntity();


                vo.setId(hypervisor.getId());
                vo.setName(hypervisor.getHypervisorHostname());
                vo.setTimestamp(String.valueOf(epoch));
                vo.setType(hypervisor.getType());
                vo.setStatus(hypervisor.getStatus());
                vo.setState(hypervisor.getState());
                vo.setHostIp(hypervisor.getHostIP());
                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
                vo.setLeastDiskAvailable(hypervisor.getLeastDiskAvailable());
                vo.setHypervisorVersion(hypervisor.getVersion());
                vo.setRunningVms(hypervisor.getRunningVM());
                vo.setVcpus(hypervisor.getVirtualCPU());
                vo.setVcpusUsed(hypervisor.getVirtualUsedCPU());
                vo.setLocalGb(hypervisor.getLocalDisk());
                vo.setLocalGbUsed(hypervisor.getLocalDiskUsed());
                vo.setFreeDiskGb(hypervisor.getFreeDisk());
                vo.setMemoryMb(hypervisor.getLocalMemory());
                vo.setMemoryMbUsed(hypervisor.getLocalMemoryUsed());
                vo.setFreeRam(hypervisor.getFreeRam());

                hostEntity.setHostId(hypervisor.getId());
                hostEntity.setHostName(hypervisor.getHypervisorHostname());
                hostEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                hostEntity.setVmNum(hypervisor.getRunningVM());
                hostEntity.setVcpuTotal(hypervisor.getVirtualCPU());
                hostEntity.setVcpuUsage(hypervisor.getVirtualUsedCPU());
                hostEntity.setRamTotal(hypervisor.getLocalMemory());
                hostEntity.setRamUsage(hypervisor.getLocalMemoryUsed());
                hostEntity.setLocalStorageTotal(hypervisor.getLocalDisk());
                hostEntity.setLocalStorageUsage(hypervisor.getLocalDiskUsed());
                hostEntity.setState(hypervisor.getState());
                // hostEntity.setCollectDt(String.valueOf(epoch));

                // host집합 insert
                for(int i =0 ; i < hosts.size() ; i++){
                    hostAggregateEntity = new HostAggregateEntity();
                    log.debug("## Y_TEST ["+i+"]["+hosts.get(i)+"]");
                    hostAggregateEntity.setHostAggregateId(hosts.get(i).getId());
                    hostAggregateEntity.setHostAggregateName(hosts.get(i).getName());
                    hostAggregateEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                    hostAggregateEntity.setAzName(hosts.get(i).getAvailabilityZone());
                    // hostAggregateEntity.setCollectDt(String.valueOf(epoch));

                    hostAggregateEntityList.add(hostAggregateEntity);
                }
                list.add(vo);
                hostEntityList.add(hostEntity);
            }

            if(dbSaveOpt == true){
                log.debug("## Y_TEST ES Insert Option On!");
                // boolean esCallResult = false;
                try {
                    // get projectMetricInfo In a Webclient way  ==> KEYSTONE_IDENTITY
                    WebClient webClient = webclientUtil.getWebClient(ElasticSearchConstant.ES_UPDATE_HYPERVISOR);

                    log.debug("## ES Index(Hypervisor) Call Result["+webClient.post()
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(list)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()+"]");

                } catch (Exception e) {
                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
                }
            }

            // end time
            // log.debug("========HypervisorList(SDK)================");


        }catch (ConnectException e){
            // log
            log.error(e.getMessage(), e);
        }catch (Exception e){
            // log
            log.error(e.getMessage(), e);
        }

        return list;

    }


//    /**
//     * openstack API 통해 모든 특정 Scope의 Hypervisor 리스트 조회.
//     * @param projectName
//     * @param userId
//     * @return
//     */
//
//    @Override
//    public List<HostVo> list(String projectName, String userId) {
//        List<HostVo> list = new ArrayList<>();
//        try {
//            // connect to openstack
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
//
//            // get project
//            List<? extends Hypervisor> hypervisors = osClient.compute().hypervisors().list();
//
//            for(Hypervisor hypervisor : hypervisors) {
//                HostVo vo = new HostVo();
//
//                vo.setId(hypervisor.getId());
//                vo.setName(hypervisor.getHypervisorHostname());
//                vo.setType(hypervisor.getType());
//                vo.setStatus(hypervisor.getStatus());
//                vo.setState(hypervisor.getState());
//                vo.setHostIp(hypervisor.getHostIP());
//                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
//                vo.setLeastDiskAvailable(hypervisor.getLeastDiskAvailable());
//                vo.setVersion(hypervisor.getVersion());
//                vo.setRunningVM(hypervisor.getRunningVM());
//                vo.setVirtualCPU(hypervisor.getVirtualCPU());
//                vo.setVirtualUsedCPU(hypervisor.getVirtualUsedCPU());
//                vo.setLocalDisk(hypervisor.getLocalDisk());
//                vo.setLocalDiskUsed(hypervisor.getLocalDiskUsed());
//                vo.setFreeDisk(hypervisor.getFreeDisk());
//                vo.setLocalMemory(hypervisor.getLocalMemory());
//                vo.setLocalMemoryUsed(hypervisor.getLocalMemoryUsed());
//                vo.setFreeRam(hypervisor.getFreeRam());
//
//
//                list.add(vo);
//            }
//
//        } catch (ConnectException e) {
//            // log
//            log.error(e.getMessage(), e);
//        }
//
//        return list;
//
//    }

}
