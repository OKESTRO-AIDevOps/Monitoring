package com.okestro.symphony.dashboard.api.openstack.keystone.project.svc.impl;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectDetailMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.repo.OpenstackProjectDetailRepository;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectQuotaEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ProjectMetricVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ProjectQuotaMetricVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.repo.ProjectQuotaRepository;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.repo.ProjectRepository;
import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.constant.ElasticSearchConstant;
import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.svc.ProjectService;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VmVo;
import com.okestro.symphony.dashboard.util.DateUtil;
import com.okestro.symphony.dashboard.util.WebclientUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.networking.NetworkingService;
import org.openstack4j.api.storage.BlockStorageService;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.QuotaSet;
import org.openstack4j.model.compute.Server;
import org.openstack4j.model.compute.SimpleTenantUsage;
import org.openstack4j.model.identity.v3.Project;
import org.openstack4j.model.network.NetQuota;
import org.openstack4j.model.storage.block.BlockQuotaSet;
import org.openstack4j.model.storage.block.BlockQuotaSetUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@Slf4j
@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    WebsocketService websocketService;

//    @Autowired
//    ProjectRepository projectRepository;
//
//    @Autowired
//    ProjectQuotaRepository projectQuotaRepository;
//
//    @Autowired
//    OpenstackProjectDetailRepository openstackProjectDetailRepository;

    @Autowired
    RestHighLevelClient esClient;

    @Autowired
    DateUtil dateUtil;

    @Autowired
    WebclientUtil webclientUtil;


    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;


    /**
     * webclient를 통해 admin 권한으로 모든 프로젝트 list 리턴.
     * @param dbSaveOpt (DB insert 옵션)
     * @return
     */
    @Override
    public List<ProjectVo> getWcProjectList(boolean dbSaveOpt){
        List<ProjectVo> projectList = new ArrayList<>();
        List<ProjectEntity> projectEntityList = new ArrayList<>();
        ProjectEntity projectEntity = null;
        // get current date time
        String nowDate = DateUtil.now();
        Date time = new Date();
        // For calculating elapsed time
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        try{
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);

            // get hypervisors In a Webclient way  ==> KEYSTONE_IDENTITY
            WebClient webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.KEYSTONE_IDENTITY);



            // get metric info
            ProjectMetricVo metricInfos = null;
            // get unix Time
            long epoch = System.currentTimeMillis();

            try {
                metricInfos = webClient.get()
                        .uri("/projects")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(ProjectMetricVo.class).block();

            } catch (Exception e) {
                log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
            }

            // project정보 Entity 셋팅
            for(ProjectMetricVo.Project project : metricInfos.getProjects()){
                ProjectVo vo = new ProjectVo();
                projectEntity = new ProjectEntity();
//              Date time = new Date(); // 원래 조회마다 현재시간 셋팅해서 넣어주려고했는데 API 조회 시간이 오래걸려서 유종팀장님 요청으로 조회 시작시간으로 수정.

                vo.setName(project.getName());
                vo.setId(project.getId());
                vo.setTimestamp(String.valueOf(epoch));
                vo.setDesc(project.getDesc());
                vo.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                vo.setEnabled((project.getLinks().getEnabled() == "false") ? false : true);
                vo.setDomainId(project.getDomainId() == null ? "default" : project.getDomainId());
                vo.setRetrievedDt(nowDate);

                projectEntity.setProjectId(project.getId());
                projectEntity.setProjectName(project.getName());
                projectEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                projectEntity.setState((project.getLinks().getEnabled() == "false") ? "false" : "true");
                projectEntity.setCollectDt(time);

                projectEntityList.add(projectEntity);
                projectList.add(vo);
            }

            //DB insert 옵션.
            if(dbSaveOpt == true){
                // INSERT, save와의 성능차이로 인해 saveAll로 변경.
//                projectRepository.saveAll(projectEntityList);
                log.debug("## Y_TEST DB Insert Option On!");
                boolean esCallResult = false;
                try {

                    webClient = webclientUtil.getWebClient(ElasticSearchConstant.ES_UPDATE_PROJECT);

                    log.debug("## ES Index(Project) Call Result["+webClient.post()
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(projectList)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()+"]");

                } catch (Exception e) {
                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
                }
            }

            // end time
            log.debug("========ProjectQuotaList(Webclient)================");
            //스탑워치가 돌고있으면 멈,
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");

        }catch (Exception e){
            log.error("## Exception 발생. ["+e.getMessage()+"]", e);
            e.printStackTrace();
        }

        return projectList;
    }


    /**
     *  admin 권한으로 모든 프로젝트 list 리턴.
     * @return List<ProjectVo>
     */
    @Override
    public List<ProjectVo> getSdkProjectList(boolean dbSaveOpt){
        List<? extends Project> projectArr = null;
        List<ProjectVo> projectList = new ArrayList<>();

        // get current date time
        String nowDate = DateUtil.now();
        // For calculating elapsed time
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try{
            OSClient.OSClientV3 os = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
            // get unix Time
            long epoch = System.currentTimeMillis();

            projectArr = os.identity().projects().list();
            for(Project project : projectArr){
                ProjectVo vo = new ProjectVo();

                vo.setName(project.getName());
                vo.setId(project.getId());
                vo.setTimestamp(String.valueOf(epoch));
                vo.setDesc(project.getDescription());
                vo.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                vo.setEnabled((project.getLinks().get("enabled") == "true") ? true : false);
                vo.setDomainId(project.getDomain() == null ? "" : project.getDomain().getId());
                vo.setDomainNm(project.getDomain() == null ? "default" : project.getDomain().getName());
                vo.setRetrievedDt(nowDate);

                projectList.add(vo);
            }

            //DB insert 옵션.
            if(dbSaveOpt == true){
                log.debug("## Y_TEST ES Insert Option On!");
                // get metric info
//                boolean esCallResult = false;
                try {

                    WebClient webClient = webclientUtil.getWebClient(ElasticSearchConstant.ES_UPDATE_PROJECT);

                    log.debug("## ES Index(Project) Call Result["+webClient.post()
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(projectList)
                            .retrieve()
                            .bodyToMono(Boolean.class)
                            .block()+"]");

                } catch (Exception e) {
                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
                }
            }

            // end time
            log.debug("========ProjectList(SDK)================");
            //스탑워치가 돌고있으면 멈,
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");

        }catch (Exception e){
            log.error("## Exception 발생. ["+e.getMessage()+"]", e);
            e.printStackTrace();
        }

        return projectList;
    }


    /**
     *  admin 권한으로 모든 프로젝트 list 조회 후 각 프로젝트 별 QuotaProjectVo 셋팅 후 list<QuotaProjectVo> 리턴. (다중 스레드 병렬처리)
     *  추가 작업으로 조회한내용을 ES에 ost-metric-projects-detail-2021-05-20 형식으로 저장하게끔 Vo를 던짐.
     * @return List<QuotaProjectVo>
     */
    @Override
    public List<QuotaProjectVo> getAllProjectQuotaList(boolean dbSaveOpt){
        List<QuotaProjectVo> projectVoList = new ArrayList<>();
        List<ProjectDetailMetric> projectDetailMetricList = new ArrayList<>();
        ProjectDetailMetric tmpProjectDetailMeticEntity = new ProjectDetailMetric();

        // For calculating elapsed time
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        try {
            // connect to openstack & connect to openstack (project socpe)
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
            long epoch = System.currentTimeMillis();


            List<? extends Project>  projects = osClient.identity().projects().list();

            ForkJoinPool myPool = new ForkJoinPool(4);
            myPool.submit(() -> {
                //다중스레드 병렬처리.
                projects.parallelStream().forEach(projectVo ->{
                    QuotaProjectVo projectQuotaVo = new QuotaProjectVo();
                    ProjectDetailMetric projectDetailMetric = new ProjectDetailMetric();
                    ProjectDetailMetric.ProjectDetailInfo projectDetailInfo = new ProjectDetailMetric.ProjectDetailInfo();
                    ProjectDetailMetric.ProjectDetailInfo.ProjectUsages projectUsages = new ProjectDetailMetric.ProjectDetailInfo.ProjectUsages();
                    ProjectDetailMetric.ProjectDetailInfo.ProjectQuotaSet projectQuotaSet = new ProjectDetailMetric.ProjectDetailInfo.ProjectQuotaSet();

                    OSClient.OSClientV3 os = null;
                    WebClient webClient = null;
                        try{
                            // 위에 osClient를 안쓰고 재정의 하는이유는 병렬처리시 각 스레드에서 같은 토큰값을 가지고 API조회시에 에러가 발생하여 재정의.
                            os = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
                            webClient = webclientUtil.getWebClient(os, OpenStackConstant.OS_QUOTA_DEFAULT_SETS);

                            // Vo에 Meta 정보 셋팅.
                            log.debug("## Y_TEST project Name ["+projectVo.getName() +"]");
                            projectQuotaVo.setProjectName(projectVo.getName());
                            projectQuotaVo.setUserId(user);
                            projectQuotaVo.setDomain(projectVo.getDomain() == null ? "default" : projectVo.getDomain().getName());
                            projectQuotaVo.setProjectId(projectVo.getId());
                            log.debug("## Y_TEST epoch time toString["+String.valueOf(epoch)+"]");
                            projectQuotaVo.setTimestamp(String.valueOf(epoch));

                            // Vo에 Quota 정보 셋팅. (SDK API & curl API)
                            projectQuotaVo = setProjectQuotaAndUsage(webClient, os, projectQuotaVo);
//                            projectQuotaVo.setComputeQuota(getQuotaCompute(projectVo.getId(), os.compute()));
//                            projectQuotaVo.setNetworkQuota(getQuotaNetwork(projectVo.getId(), os.networking()));
//                            projectQuotaVo.setVolumeQuota(getQuotaVolumn(projectVo.getId(), os.blockStorage()));

                            // Vo에 Compute Usage 정보 셋팅. (Curl API)
                            // projectQuotaVo.setComputeUsage(getUsageCompute(projectVo.getId(), os.compute()));
//                            projectQuotaVo.setComputeUsage(getUsageCompute(projectVo.getId(), webClient));

                            // Vo에 Volume Usage 정보 셋팅. (Curl API)
                            // projectQuotaVo.setVolumeUsage(getUsageVolumn(projectVo.getId(), os.blockStorage()));
//                            projectQuotaVo.setVolumeUsage(getUsageVolumn(projectVo.getId(), os.blockStorage()));

//                            projectDetailMetric.setId(projectQuotaVo.getProjectId()); // 랜덤 생성

                            projectVoList.add(projectQuotaVo);
                        }catch (Exception e){
                            log.error("## Y_TEST projectQuota 셋팅간 오류발생!["+e.getMessage()+"]");
                            e.printStackTrace();
                        }
                    });
            }).get();
            myPool.shutdown();
            log.debug("threadPool isShutdown["+myPool.isShutdown()+"]");
            log.debug("threadPool isTerminated["+myPool.isTerminated()+"]");
            log.debug("threadPool isTerminating["+myPool.isTerminating()+"]");

            // end time
            log.debug("========ProjectQuotaList(SDK)================");
            //스탑워치가 돌고있으면 멈춤.
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");

            //DB insert 옵션.
            if(dbSaveOpt == true){
                // get metric info
                boolean esIndexExist = false;
                Date date = new Date();
                date.setTime(epoch);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd");
                GetIndexRequest request = new GetIndexRequest("ost-metric-projects-detail-"+simpleDateFormat.format(date));

                esIndexExist = esClient.indices().exists(request, RequestOptions.DEFAULT);
                if(esIndexExist == false) {
                    log.debug("## Index is Not Exist!!  try to create Index... [" + ElasticSearchConstant.ES_INDEX_META_PROJECT_DETAIL + simpleDateFormat.format(date) + "]");
                }

                try {
                    // get projectMetricDtailInfo In a Webclient way  ==> KEYSTONE_IDENTITY
                    WebClient webClient = webclientUtil.getWebClient(ElasticSearchConstant.ES_UPDATE_PROJECT_DETAIL);

                    log.debug("## ES Index(ProjectDetail) Call Result["+webClient.post()
//                            .uri("/projects")
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(projectVoList)
                            .retrieve()
//                            .bodyToMono(new ParameterizedTypeReference<List<ProjectDetailMetric>>() {})
                            .bodyToMono(Boolean.class)
                            .block()+"]");

                } catch (Exception e) {
                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
                }
            }

        } catch (ConnectException | InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (ExecutionException e1){
            log.error("## Thread 병렬처리 수행간 에러발생! 내용["+e1.getMessage()+"]");
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return projectVoList;
    }


    /**
     * SDK 를 통해 ProjectQuota 조회.
     * @param dbSaveOpt
     * @return
     */
    @Override
    public List<QuotaProjectVo> getSdkAllProjectQuotaList(boolean dbSaveOpt) {


        return null;
    }

    /**
     *  Webclient와 SDK를 통해 admin 권한으로 모든 프로젝트 list 조회 후 각 프로젝트 별 QuotaProjectVo 셋팅 후 list<QuotaProjectVo> 리턴.
     * @return List<QuotaProjectVo>
     */
    @Override
    public List<QuotaProjectVo> getWcAllProjectQuotaList(boolean dbSaveOpt){
        List<QuotaProjectVo> projectVoList = new ArrayList<>();
        List<ProjectQuotaEntity> projectQuotaEntityList = new ArrayList<>();
        QuotaProjectVo projectQuotaVo = null;
        ProjectQuotaEntity projectQuotaEntity = null;
        // For calculating elapsed time
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // connect to openstack & connect to openstack (project socpe)
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
            OSClient.OSClientV3 osProjectClient = null;

            WebclientUtil webclientUtil = new WebclientUtil();

            // get hypervisors In a Webclient way  ==> OS_QUOTA_SETS
            WebClient webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.OS_QUOTA_SETS);

            // current time
            Date time = new Date();

            // get metric info
            ProjectQuotaMetricVo commonMetricInfos = null;

            for( Project projectVo : osClient.identity().projects().list()){
                projectQuotaVo = new QuotaProjectVo();
                projectQuotaEntity = new ProjectQuotaEntity();

                try {
                    // Common Metric 조회
                    commonMetricInfos = webClient.get()
                            .uri("/v2.1/os-quota-sets/"+projectVo.getId()+"/detail")
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(ProjectQuotaMetricVo.class).block();

                } catch (Exception e) {
                    log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
                }
                log.debug("## Y_TEST commonMetricInfos["+commonMetricInfos.toString()+"]");

                // connect to openstack (project socpe)
//                osProjectClient = openStackConnectionService.connect(endpoint,domain,projectVo.getName(),user,password);

                projectQuotaVo.setProjectId(projectVo.getId());
                projectQuotaVo.setDomain(projectVo.getDomain().getName());
                projectQuotaVo.setComputeQuota(getQuotaCompute(projectVo.getId(), osClient.compute()));
                projectQuotaVo.setNetworkQuota(getQuotaNetwork(commonMetricInfos));
                projectQuotaVo.setVolumeQuota(getQuotaVolumn(projectVo.getId(), osClient.blockStorage()));
                projectQuotaVo.setComputeUsage(getSimpleUsageCompute(projectVo.getId(), osClient.compute()));
                projectQuotaVo.setNetworkUsage(getSimpleUsageNetwork(commonMetricInfos));
                projectQuotaVo.setVolumeUsage(getUsageVolumn(projectVo.getId(), osClient.blockStorage()));


                projectQuotaEntity.setProjectId(projectQuotaVo.getProjectId());
                projectQuotaEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                projectQuotaEntity.setInstanceQuota(commonMetricInfos.getQuotaSets().getInstances().getLimit());
                projectQuotaEntity.setInstanceUsage(commonMetricInfos.getQuotaSets().getInstances().getInUse());
                projectQuotaEntity.setCpuQuota(commonMetricInfos.getQuotaSets().getCores().getLimit());
                projectQuotaEntity.setCpuUsage(commonMetricInfos.getQuotaSets().getCores().getInUse());
                projectQuotaEntity.setRamQuota(commonMetricInfos.getQuotaSets().getRam().getLimit());
                projectQuotaEntity.setRamUsage(commonMetricInfos.getQuotaSets().getRam().getInUse());
                projectQuotaEntity.setVolumeQuota(projectQuotaVo.getVolumeQuota().getVolumeQuota());
                projectQuotaEntity.setVolumeUsage(projectQuotaVo.getVolumeUsage().getVolumeUsed());
                projectQuotaEntity.setSnapshotQuota(projectQuotaVo.getVolumeQuota().getSnapshotQuota());
                projectQuotaEntity.setSnapshotUsage(projectQuotaVo.getVolumeUsage().getSnapshotUsed());
                projectQuotaEntity.setStorageQuota(projectQuotaVo.getVolumeQuota().getGigabyteQuota());
                projectQuotaEntity.setStorageUsage(projectQuotaVo.getVolumeUsage().getGigabyteUsed());
//                projectQuotaEntity.setNetworkQuota(projectQuotaVo.getNetworkQuota().getNetworkQuota());
//                projectQuotaEntity.setNetworkUsage(projectQuotaVo.getNetworkQuota().getNetworkUsed());
                projectQuotaEntity.setFloatingIpQuota(commonMetricInfos.getQuotaSets().getFloatingIps().getLimit());
                projectQuotaEntity.setFloatingIpUsage(commonMetricInfos.getQuotaSets().getFloatingIps().getInUse());
                projectQuotaEntity.setSecurityGroupQuota(commonMetricInfos.getQuotaSets().getSecurityGroups().getLimit());
                projectQuotaEntity.setSecurityGroupUsage(commonMetricInfos.getQuotaSets().getSecurityGroups().getInUse());
                projectQuotaEntity.setSecurityRuleQuota(commonMetricInfos.getQuotaSets().getSecurityGroupRules().getLimit());
                projectQuotaEntity.setSecurityRuleUsage(commonMetricInfos.getQuotaSets().getSecurityGroupRules().getInUse());
                projectQuotaEntity.setSecurityGroupQuota(commonMetricInfos.getQuotaSets().getSecurityGroups().getLimit());
                projectQuotaEntity.setSecurityGroupUsage(commonMetricInfos.getQuotaSets().getSecurityGroups().getInUse());
                projectQuotaEntity.setSecurityRuleQuota(commonMetricInfos.getQuotaSets().getSecurityGroupRules().getLimit());
                projectQuotaEntity.setSecurityRuleUsage(commonMetricInfos.getQuotaSets().getSecurityGroupRules().getInUse());
//                projectQuotaEntity.setPortQuota(projectQuotaVo.getNetworkQuota().getPortQuota());
//                projectQuotaEntity.setPortUsage(osClient.networking().port().list().size());
//                projectQuotaEntity.setRouterQuota(projectQuotaVo.getNetworkQuota().getRouterQuota());
//                projectQuotaEntity.setRouterUsage(osClient.networking().router().list().size());
                projectQuotaEntity.setCollectDt(time);

                projectVoList.add(projectQuotaVo);
                projectQuotaEntityList.add(projectQuotaEntity);

            }
            //DB insert 옵션.
            if(dbSaveOpt == true){
                //save와의 성능차이로 인해 saveAll로 변경.
//                projectQuotaRepository.saveAll(projectQuotaEntityList);
//                log.debug("## Y_TEST DB Insert Option On!");
            }

            // end time
            log.debug("========ProjectQuotaList(Webclient)================");
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }
            log.debug("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");


        } catch (ConnectException | SSLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        return projectVoList;
    }


    public QuotaComputeVo getQuotaCompute(String projectId, ComputeService compute) {
        QuotaSet computeQuota = compute.quotaSets().get(projectId);
//        SimpleTenantUsage computeUsage = compute.quotaSets().getTenantUsage(projectId);


        QuotaComputeVo result = new QuotaComputeVo();
        result.setInstanceQuota(computeQuota.getInstances());
        result.setCoreQuota(computeQuota.getCores());
        result.setRamQuota(computeQuota.getRam());

        result.setMetadataItemsQuota(computeQuota.getMetadataItems());
        result.setInjectFilesQuota(computeQuota.getInjectedFiles());
        result.setInjectedFileContentBytesQuota(computeQuota.getInjectedFileContentBytes());
        result.setInjectedFilePathBytesQuota(computeQuota.getInjectedFilePathBytes());
        result.setKeyPairsQuota(computeQuota.getKeyPairs());

        return result;

    }

    /**
     * 특정 ProjectQuota & Usage에 대한 정보를 조회한다.
     * 단, Volume Quota & Usage, Network Quota는 SDK 를 통해 조회하며 Network Usage는 현재 조회할수없어 임시 초기화.
     * @param webClient
     * @param osClient
     * @param projectQuotaVo
     * @return
     */
    public QuotaProjectVo setProjectQuotaAndUsage(WebClient webClient, OSClient.OSClientV3 osClient ,QuotaProjectVo projectQuotaVo) {
        QuotaComputeVo quotaComputeVo = new QuotaComputeVo();
//        QuotaNetworkVo quotaNetworkVo = new QuotaNetworkVo();
//        QuotaVolumnVo quotaVolumnVo = new QuotaVolumnVo();
        UsageComputeVo usageComputeVo = new UsageComputeVo();
        UsageNetworkVo usageNetworkVo = new UsageNetworkVo();
//        UsageVolumnVo usageVolumnVo = new UsageVolumnVo();

        QuotaProjectVo quotaProjectVo = new QuotaProjectVo();


        try {

            String computeQuotas = webClient.get()
                    .uri("/os-quota-sets/" + projectQuotaVo.getProjectId() + "/detail")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            JsonObject jsonObject = new JsonParser().parse(computeQuotas).getAsJsonObject();
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);
            LinkedTreeMap quotas = (LinkedTreeMap) treeMap.get("quota_set");

            quotaProjectVo = projectQuotaVo;

            quotaProjectVo.setComputeQuota(quotaComputeVo);
            quotaProjectVo.setComputeUsage(usageComputeVo);
            // Network Quota는 여기서 셋팅됨. (SDK API)
            quotaProjectVo.setNetworkQuota(getQuotaNetwork(projectQuotaVo.getProjectId(),osClient.networking()));
            quotaProjectVo.setNetworkUsage(usageNetworkVo);
            // Volume Quota & Usages는 여기서 셋팅됨. (SDK API)
            quotaProjectVo.setVolumeQuota(getQuotaVolumn(projectQuotaVo.getProjectId(), osClient.blockStorage()));
            quotaProjectVo.setVolumeUsage(getUsageVolumn(projectQuotaVo.getProjectId(), osClient.blockStorage()));

            // compute Quota
            quotaProjectVo.getComputeQuota().setInstanceQuota(((Double)((LinkedTreeMap) quotas.get("instances")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setCoreQuota(((Double)((LinkedTreeMap) quotas.get("cores")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setRamQuota(((Double)((LinkedTreeMap) quotas.get("ram")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setMetadataItemsQuota(((Double)((LinkedTreeMap) quotas.get("metadata_items")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setInjectFilesQuota(((Double)((LinkedTreeMap) quotas.get("injected_files")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setInjectedFileContentBytesQuota(((Double)((LinkedTreeMap) quotas.get("injected_file_content_bytes")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setInjectedFilePathBytesQuota(((Double)((LinkedTreeMap) quotas.get("injected_file_path_bytes")).get("limit")).intValue());
            quotaProjectVo.getComputeQuota().setKeyPairsQuota(((Double)((LinkedTreeMap) quotas.get("key_pairs")).get("limit")).intValue());

            // compute Usage
            quotaProjectVo.getComputeUsage().setInstanceUsed(((Double)((LinkedTreeMap) quotas.get("instances")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setCoreUsed(((Double)((LinkedTreeMap) quotas.get("cores")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setRamUsed(((Double)((LinkedTreeMap) quotas.get("ram")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setMetadataItemsUsed(((Double)((LinkedTreeMap) quotas.get("metadata_items")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setInjectFilesUsed(((Double)((LinkedTreeMap) quotas.get("injected_files")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setInjectedFileContentBytesUsed(((Double)((LinkedTreeMap) quotas.get("injected_file_content_bytes")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setInjectedFilePathBytesUsed(((Double)((LinkedTreeMap) quotas.get("injected_file_path_bytes")).get("in_use")).intValue());
            quotaProjectVo.getComputeUsage().setKeyPairsUsed(((Double)((LinkedTreeMap) quotas.get("key_pairs")).get("in_use")).intValue());

            // network Usage
            quotaProjectVo.getNetworkUsage().setNetworkUsed(0);
            quotaProjectVo.getNetworkUsage().setPortUsed(0);
            quotaProjectVo.getNetworkUsage().setSubnetUsed(0);
            quotaProjectVo.getNetworkUsage().setRouterUsed(0);
            quotaProjectVo.getNetworkUsage().setFixedIpUsed(((Double)((LinkedTreeMap) quotas.get("fixed_ips")).get("in_use")).intValue());
            quotaProjectVo.getNetworkUsage().setFloatingIpUsed(((Double)((LinkedTreeMap) quotas.get("floating_ips")).get("in_use")).intValue());
            quotaProjectVo.getNetworkUsage().setSecurityGroupRuleUsed(((Double)((LinkedTreeMap) quotas.get("security_group_rules")).get("in_use")).intValue());
            quotaProjectVo.getNetworkUsage().setSecurityGroupUsed(((Double)((LinkedTreeMap) quotas.get("security_groups")).get("in_use")).intValue());


        }catch (Exception e){
            log.error("## API 조회를 통해 ProjectQuota & Usage 셋팅중 에러발생. ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return quotaProjectVo;

    }

    /**
     * WebClient를 통해 project compute Quota 조회.
     * @param projectId
     * @param webClient
     * @return
     */
    public QuotaComputeVo getQuotaCompute(String projectId, WebClient webClient) {
        QuotaComputeVo result = new QuotaComputeVo();

        try {
            String computeQuotas = webClient.get()
                    .uri("/os-quota-sets/" + projectId + "/detail")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            JsonObject jsonObject = new JsonParser().parse(computeQuotas).getAsJsonObject();
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);
            LinkedTreeMap quotas = (LinkedTreeMap) treeMap.get("quota_set");


            result.setInstanceQuota(((Double)((LinkedTreeMap) quotas.get("instances")).get("limit")).intValue());
            result.setCoreQuota(((Double)((LinkedTreeMap) quotas.get("cores")).get("limit")).intValue());
            result.setRamQuota(((Double)((LinkedTreeMap) quotas.get("ram")).get("limit")).intValue());
            result.setMetadataItemsQuota(((Double)((LinkedTreeMap) quotas.get("metadata_items")).get("limit")).intValue());
            result.setInjectFilesQuota(((Double)((LinkedTreeMap) quotas.get("injected_files")).get("limit")).intValue());
            result.setInjectedFileContentBytesQuota(((Double)((LinkedTreeMap) quotas.get("injected_file_content_bytes")).get("limit")).intValue());
            result.setInjectedFilePathBytesQuota(((Double)((LinkedTreeMap) quotas.get("injected_file_path_bytes")).get("limit")).intValue());
            result.setKeyPairsQuota(((Double)((LinkedTreeMap) quotas.get("key_pairs")).get("limit")).intValue());

        }catch (Exception e){
            log.error("## webClient를 통해 ProjectComputeQuota 정보 조회간 에러발생. ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return result;

    }

    /**
     * WebClient를 통해 project compute Usage 조회.
     * @param projectId
     * @param webClient
     * @return
     */
    public UsageComputeVo getUsageCompute(String projectId, WebClient webClient) {
        UsageComputeVo result = new UsageComputeVo();

        try {
            String computeUsages = webClient.get()
                    .uri("/os-quota-sets/" + projectId + "/detail")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            JsonObject jsonObject = new JsonParser().parse(computeUsages).getAsJsonObject();
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);
            LinkedTreeMap quotas = (LinkedTreeMap) treeMap.get("quota_set");


            result.setInstanceUsed(((Double)((LinkedTreeMap) quotas.get("instances")).get("in_use")).intValue());
            result.setCoreUsed(((Double)((LinkedTreeMap) quotas.get("cores")).get("in_use")).intValue());
            result.setRamUsed(((Double)((LinkedTreeMap) quotas.get("ram")).get("in_use")).intValue());
            result.setMetadataItemsUsed(((Double)((LinkedTreeMap) quotas.get("metadata_items")).get("in_use")).intValue());
            result.setInjectFilesUsed(((Double)((LinkedTreeMap) quotas.get("injected_files")).get("in_use")).intValue());
            result.setInjectedFileContentBytesUsed(((Double)((LinkedTreeMap) quotas.get("injected_file_content_bytes")).get("in_use")).intValue());
            result.setInjectedFilePathBytesUsed(((Double)((LinkedTreeMap) quotas.get("injected_file_path_bytes")).get("in_use")).intValue());
            result.setKeyPairsUsed(((Double)((LinkedTreeMap) quotas.get("key_pairs")).get("in_use")).intValue());

        }catch (Exception e){
            log.error("## webClient를 통해 ProjectComputeUsage 정보 조회간 에러발생. ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return result;

    }

    /**
     * WebClient를 통해 project network Usage 조회.
     * 단, network, port, subnet, router 정보는 직접 조회가 불가하여 0으로 초기화.
     * @param projectId
     * @param webClient
     * @return
     */
    public UsageNetworkVo getUsageNetwork(String projectId, WebClient webClient) {
        UsageNetworkVo result = new UsageNetworkVo();

        try {
            String computeQuotas = webClient.get()
                    .uri("/os-quota-sets/" + projectId + "/detail")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            JsonObject jsonObject = new JsonParser().parse(computeQuotas).getAsJsonObject();
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);
            LinkedTreeMap quotas = (LinkedTreeMap) treeMap.get("quota_set");


            result.setNetworkUsed(0);
            result.setRouterUsed(0);
            result.setSubnetUsed(0);
            result.setPortUsed(0);
            result.setFixedIpUsed(((Double)((LinkedTreeMap) quotas.get("fixed_ips")).get("in_use")).intValue());
            result.setSecurityGroupUsed(((Double)((LinkedTreeMap) quotas.get("security_groups")).get("in_use")).intValue());
            result.setSecurityGroupRuleUsed(((Double)((LinkedTreeMap) quotas.get("security_group_rules")).get("in_use")).intValue());
            result.setFloatingIpUsed(((Double)((LinkedTreeMap) quotas.get("floating_ips")).get("in_use")).intValue());

        }catch (Exception e){
            log.error("## webClient를 통해 ProjectNetworkUsage 정보 조회간 에러발생. ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return result;

    }

    /**
     * simpleTenantUsage로 해당 프로젝트에 떠 있는 서버 기동시간과 사용하고 있는 vcpu를 곱해서 나온 값을 각 서버마다 더한값을 보여줌.
     * @param projectId
     * @param compute
     * @return
     */
    public UsageComputeVo getSimpleUsageCompute(String projectId, ComputeService compute) {
//        QuotaSet computeQuota = compute.quotaSets().get(projectId);
        SimpleTenantUsage computeUsage = compute.quotaSets().getTenantUsage(projectId);


        UsageComputeVo result = new UsageComputeVo();
        result.setInstanceUsed((computeUsage.getServerUsages() == null ? 0 : computeUsage.getServerUsages().size()));
        result.setCoreUsed((computeUsage.getTotalVcpusUsage() == null ? 0 : Integer.parseInt(String.format("%.12f",computeUsage.getTotalVcpusUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue()))));
        result.setRamUsed(computeUsage.getTotalMemoryMbUsage()== null ? 0 : Integer.parseInt(String.format("%.12f",computeUsage.getTotalMemoryMbUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue())));

        return result;

    }

    /**
     * 그룹, 그룹 멤버 쿼터도 포함해서 호출
     */
    private QuotaComputeVo getQuotaCompute(String projectId, OSClient.OSClientV3 osClient, String projectName, String userId) throws SSLException {
        WebClient webClient = null;

        String computeQuotas = webClient.get()
                .uri("/os-quota-sets/" + projectId)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        JsonObject jsonObject = new JsonParser().parse(computeQuotas).getAsJsonObject();
        Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

        LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);

        LinkedTreeMap quotas = (LinkedTreeMap) treeMap.get("quota_set");

        QuotaComputeVo result = new QuotaComputeVo();
        result.setInstanceQuota(((Double) quotas.get("instances")).intValue());
        result.setCoreQuota(((Double) quotas.get("cores")).intValue());
        result.setRamQuota(((Double) quotas.get("ram")).intValue());
        result.setMetadataItemsQuota(((Double) quotas.get("metadata_items")).intValue());
        result.setInjectFilesQuota(((Double) quotas.get("injected_files")).intValue());
        result.setInjectedFileContentBytesQuota(((Double) quotas.get("injected_file_content_bytes")).intValue());
        result.setInjectedFilePathBytesQuota(((Double) quotas.get("injected_file_path_bytes")).intValue());
        result.setKeyPairsQuota(((Double) quotas.get("key_pairs")).intValue());


        return result;

    }

    /**
     * Network Quota 조회.
     * @param commonMetricInfo
     * @return
     */
    private QuotaNetworkVo getQuotaNetwork(ProjectQuotaMetricVo commonMetricInfo) {

        QuotaNetworkVo result = new QuotaNetworkVo();

//        result.setSubnetQuota(netQuota.getSubnet());
//        result.setRouterQuota(netQuota.getRouter());
//        result.setPortQuota(netQuota.getPort());
//        result.setNetworkQuota(netQuota.getNetwork());
        result.setFloatingIpQuota(commonMetricInfo.getQuotaSets().getFloatingIps().getLimit());
        result.setSecurityGroupQuota(commonMetricInfo.getQuotaSets().getSecurityGroups().getLimit());
        result.setSecurityGroupRuleQuota(commonMetricInfo.getQuotaSets().getSecurityGroupRules().getLimit());

        return result;
    }



    /**
     * Network Usage 조회.
     * @param commonMetricInfo
     * @return
     */
    private UsageNetworkVo getSimpleUsageNetwork(ProjectQuotaMetricVo commonMetricInfo) {

        UsageNetworkVo result = new UsageNetworkVo();

        result.setFloatingIpUsed(commonMetricInfo.getQuotaSets().getFloatingIps().getInUse());
        result.setSecurityGroupUsed(commonMetricInfo.getQuotaSets().getSecurityGroups().getInUse());
        result.setSecurityGroupRuleUsed(commonMetricInfo.getQuotaSets().getSecurityGroupRules().getInUse());

        return result;
    }


    /**
     * Network Quota 조회.
     * @param projectId
     * @param networking
     * @return
     */
    private QuotaNetworkVo getQuotaNetwork(String projectId, NetworkingService networking) {
        NetQuota netQuota = networking.quotas().get(projectId);
        QuotaNetworkVo result = new QuotaNetworkVo();

        result.setSubnetQuota(netQuota.getSubnet());
        result.setRouterQuota(netQuota.getRouter());
        result.setPortQuota(netQuota.getPort());
        result.setNetworkQuota(netQuota.getNetwork());
        result.setFloatingIpQuota(netQuota.getFloatingIP());
        result.setSecurityGroupQuota(netQuota.getSecurityGroup());
        result.setSecurityGroupRuleQuota(netQuota.getSecurityGroupRule());

        return result;
    }

    private QuotaVolumnVo getQuotaVolumn(String projectId, BlockStorageService blockStorage) {
        BlockQuotaSet blockQuotaSet = blockStorage.quotaSets().get(projectId);

        QuotaVolumnVo result = new QuotaVolumnVo();
        result.setVolumeQuota(blockQuotaSet.getVolumes());
        result.setSnapshotQuota(blockQuotaSet.getSnapshots());
        result.setGigabyteQuota(blockQuotaSet.getGigabytes());

        return result;
    }

    private UsageVolumnVo getUsageVolumn(String projectId, BlockStorageService blockStorage) {
        BlockQuotaSetUsage blockQuotaSetUsage = blockStorage.quotaSets().usageForTenant(projectId);

        UsageVolumnVo result = new UsageVolumnVo();
        result.setVolumeUsed(blockQuotaSetUsage.getVolumes().getInUse());
        result.setSnapshotUsed(blockQuotaSetUsage.getSnapshots().getInUse());
        result.setGigabyteUsed(blockQuotaSetUsage.getGigabytes().getInUse());

        return result;
    }

    private void sendLog(String se, String code, String type, ProjectVo vo, com.okestro.symphony.dashboard.cmm.model.ProjectVo projectVo) {
        auditLogService.sendLog(se
                , vo.getProjectName()
                , projectVo.getProject()
                , messageSource.getMessage(
                        code
                        , new String[]{type, vo.getName()}, Locale.KOREA)
                , vo.getUserId());
    }
    private String getComputeUri(String bareMetalUrl) {

        String computeUri = "";

        computeUri = bareMetalUrl.substring(0, bareMetalUrl.lastIndexOf(":")) + ":8774/v2.1";

        return computeUri;
    }
}
