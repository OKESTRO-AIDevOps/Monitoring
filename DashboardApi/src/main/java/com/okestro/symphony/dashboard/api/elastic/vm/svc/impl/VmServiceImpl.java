package com.okestro.symphony.dashboard.api.elastic.vm.svc.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.okestro.symphony.dashboard.api.elastic.vm.model.ElasticIndex;
import com.okestro.symphony.dashboard.api.elastic.vm.model.OpenstackMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo.*;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ComputeMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.HypervisorMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectDetailMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectsMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.*;
import com.okestro.symphony.dashboard.api.elastic.vm.repo.*;
import com.okestro.symphony.dashboard.api.elastic.vm.svc.VmService;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.config.elastic.RestClientConfiguration;
import com.okestro.symphony.dashboard.util.DateUtil;
import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import com.okestro.symphony.dashboard.util.EsUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.pipeline.SimpleValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Service
public class VmServiceImpl implements VmService {

	@Autowired
	OpenStackConnectionService openStackConnectionService;

	@Autowired
	OpenstackHypervisorRepository openstackHypervisorRepository;

	@Autowired
	OpenstackProjectsRepository openstackProjectsRepository;

	@Autowired
	OpenstackProjectDetailRepository openstackProjectDetailRepository;

	@Autowired
	OpenstackComputeRepository openstackComputeRepository;


	@Autowired
	OpenstackRepository openstackRepository;

	@Autowired
	VmRepository vmRepository;

	@Autowired
	RestClientConfiguration restClientConfiguration;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	ElasticIndex index;

	@Autowired
	EsUtil esUtil;

	@Value("${config.openstack.endpoint}")
	String endpoint = null;

	@Value("${config.openstack.domain}")
	String domain = null;

	@Value("${config.openstack.user}")
	String user = null;

	@Value("${config.openstack.passwd}")
	String password = null;


//	Double cpuOverCount = 8.0;					// cpu 오버커밋 배수
//	Double ramOverCount = 2.5;					// ram 오버커밋 배수


//	@Override
//	@Transactional
//	public boolean createIndex(String idxName) {
//		IndexQuery fooIdQuery = new IndexQueryBuilder();
//		return false;

//	}
	// ========================SAVE======================

	/**
	 * ES에 Hypervisor 없으면 생성 & 업데이트.
	 * @param hypervisorMetricVoList
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateHypervisorIndex(List<HypervisorMetricVo> hypervisorMetricVoList) {
		List<HypervisorMetric> hypervisorMetricList = new ArrayList<>();


		try {
		hypervisorMetricVoList.forEach(hypervisorMetricVo -> {
			HypervisorMetric hypervisorMetric = new HypervisorMetric();
			HypervisorMetric.Hypervisors hypervisorsInfo = new HypervisorMetric.Hypervisors();
			log.debug("## Y_TEST hypervisor host Name : "+hypervisorMetricVo.getName());

				// Hyperviosr Id & Timestamp Entity 셋팅.
//				hypervisorMetric.setId(hypervisorMetricVo.getId()); // Id는 ES기준 Id로 추후 어떻게 셋팅할지 고민.
				hypervisorMetric.setTimestamp(hypervisorMetricVo.getTimestamp());

				// Hyperviosr.Hypervisors Entity 셋팅.
				hypervisorsInfo.setHypervisorId(hypervisorMetricVo.getId());
				hypervisorsInfo.setHypervisorHostname(hypervisorMetricVo.getName());
				hypervisorsInfo.setType(hypervisorMetricVo.getType());
				hypervisorsInfo.setState(hypervisorMetricVo.getState());
				hypervisorsInfo.setStatus(hypervisorMetricVo.getStatus());
				hypervisorsInfo.setVcpus(hypervisorMetricVo.getVcpus());
				hypervisorsInfo.setVcpusUsed(hypervisorMetricVo.getVcpusUsed());
				hypervisorsInfo.setMemoryMb(hypervisorMetricVo.getMemoryMb());
				hypervisorsInfo.setMemoryMbUsed(hypervisorMetricVo.getMemoryMbUsed());
				hypervisorsInfo.setLocalGb(hypervisorMetricVo.getLocalGb());
				hypervisorsInfo.setLocalGbUsed(hypervisorMetricVo.getLocalGbUsed());
				hypervisorsInfo.setHypervisorVersion(hypervisorMetricVo.getHypervisorVersion());
				hypervisorsInfo.setFreeRamMb(hypervisorMetricVo.getFreeRamMb());
				hypervisorsInfo.setFreeDiskGb(hypervisorMetricVo.getFreeDiskGb());
				hypervisorsInfo.setCurrentWorkload(hypervisorMetricVo.getCurrentWorkload());
				hypervisorsInfo.setRunningVms(hypervisorMetricVo.getRunningVms());
				hypervisorsInfo.setDisAvailableLeast(hypervisorMetricVo.getDisAvailableLeast());
				hypervisorsInfo.setHostIp(hypervisorMetricVo.getHostIp());
				hypervisorsInfo.setCpuInfo(hypervisorMetricVo.getCpuInfo());
				if(hypervisorsInfo.getService() != null){
					hypervisorsInfo.getService().setId(hypervisorMetricVo.getService().getId());
					hypervisorsInfo.getService().setHost(hypervisorMetricVo.getService().getHost());
					hypervisorsInfo.getService().setDisabledReason(hypervisorMetricVo.getService().getDisabledReason());
				}


				// Hyperviosr 정보 셋팅.
				hypervisorMetric.setHypervisor(hypervisorsInfo);
				// hypervisorList에 hyperviosr 정보 add.
				hypervisorMetricList.add(hypervisorMetric);
		});
			log.debug("## Y_TEST hypervisorMetricList  Size : "+hypervisorMetricList.size());
			openstackHypervisorRepository.saveAll(hypervisorMetricList);
			return true;
		}catch (NoSuchIndexException e1){
			log.error("## ES Host Index is not Found! ["+e1.getMessage()+"]");
			e1.printStackTrace();
			return false;
		}catch (Exception e){
			log.error("## ES Host Index save Error Found! ["+e.getMessage()+"]");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ES에 Compute Index 없으면 생성 & 업데이트.
	 * @param computeMetricVo
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateComputeIndex(ComputeMetricVo computeMetricVo){
		ComputeMetric computeMetric = new ComputeMetric();

		if(computeMetricVo.getNovaName() != null){									// 넘겨받은 Vo가 null이 아닐때.
			log.debug("## Y_TEST Compute.Nova Name : "+ computeMetric.getNovaNm());

			try{
				// compute Id & Timestamp Entity 셋팅.
				computeMetric.setId(computeMetricVo.getId());
				computeMetric.setTimestamp(computeMetricVo.getTimestamp());

				if(computeMetric.getCpuQuota() != null){
					computeMetric.setCpuQuota(computeMetricVo.getCpuQuota());
					computeMetric.setCpuUsage(computeMetricVo.getCpuUsage());
				}
				if(computeMetric.getNetworkQuota() != null){
					computeMetric.setNetworkQuota(computeMetricVo.getNetworkQuota());
					computeMetric.setNetworkUsage(computeMetricVo.getNetworkUsage());
				}
				if(computeMetric.getInstanceQuota() != null){
					computeMetric.setInstanceQuota(computeMetricVo.getInstanceQuota());
					computeMetric.setInstanceUsage(computeMetricVo.getInstanceUsage());
				}

				openstackComputeRepository.save(computeMetric);

				return true;
			}catch (Exception e){
				e.printStackTrace();
				log.error("## Compute Index Update 간 에러발생. ["+e.getMessage()+"]");
				return false;
			}
		}else{															// 넘겨받은 Vo가 null 일때. 직접 ES projectDetail 인덱스 조회해서 인덱스 생성하기.
			RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
//			ComputeMetric computeAggregatedMetric = new ComputeMetric();
			OstProjectMetricVo ostProjectMetricVo = null; 	// 	조회를 위한 vo
			JSONObject resultJson = null;
			String resultStr = null;
			String metricType = null;
			ParsedTopHits topHits = null;
			SearchHit topHitValue = null;

			index.setIndex("ost-metric-projects-detail-"+index.getIndexDate());
			log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");


			SearchResponse response = null;
			String gte = String.valueOf(dateUtil.getBreforehourTimestamp());			// 1시간전 unix 시간.
			String lte = String.valueOf(System.currentTimeMillis());					// 현재 unix 시간.
			response = esUtil.selectMetricToEs(esClient, index.getIndex(), gte, lte);


			// ES 통계 쿼리 조회 결과값 Vo 셋팅.
			Terms terms = response.getAggregations().get("test_aggs_terms");
			Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();
			log.debug("## bucket Hits Size["+buckets.size()+"]");
			computeMetric.setTimestamp(System.currentTimeMillis());
			computeMetric.setCpuQuota(0.0);
			computeMetric.setCpuUsage(0.0);
			computeMetric.setNetworkQuota(0.0);
			computeMetric.setNetworkUsage(0.0);
			computeMetric.setInstanceQuota(0.0);
			computeMetric.setInstanceUsage(0.0);

			try{
				for (Terms.Bucket bucket : buckets) {														// project 마다 for문돌려서 compute정보 합산.
					topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
					topHitValue = topHits.getHits().getAt(0);
					ostProjectMetricVo = new OstProjectMetricVo();

					ostProjectMetricVo.setProjectName(bucket.getKeyAsString());

//						log.debug("## 1 hit result ["+topHitValue+"]");
					Gson gson = new Gson();
					JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
					Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
					prtGson.toJson(detailInfoJson);
					log.debug("## ProjectName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


					ostProjectMetricVo.setProjectId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_id").getAsString());
					ostProjectMetricVo.setUserId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("user_id").getAsString());
					ostProjectMetricVo.setDomain(detailInfoJson.get("domain").getAsString());
					ostProjectMetricVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());

					ostProjectMetricVo.setCpuQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("cores_quota").getAsInt());
					ostProjectMetricVo.setCpuUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("cores_usage").getAsInt());
					ostProjectMetricVo.setNetworkQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("network_quota").getAsInt());
					ostProjectMetricVo.setNetworkUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("network_usage").getAsInt());
					ostProjectMetricVo.setInstanceQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("instances_quota").getAsInt());
					ostProjectMetricVo.setInstanceUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("instances_usage").getAsInt());


					// compute관련 정보 합산.
					computeMetric.setCpuQuota(computeMetric.getCpuQuota() + (double)ostProjectMetricVo.getCpuQuota());
					computeMetric.setCpuUsage(computeMetric.getCpuUsage() + (double)ostProjectMetricVo.getCpuUsage());
//					computeMetric.setInstanceQuota(computeMetric.getInstanceQuota() + (double)ostProjectMetricVo.getInstanceQuota());
//					computeMetric.setInstanceUsage(computeMetric.getInstanceUsage() + (double)ostProjectMetricVo.getInstanceUsage());
					log.debug("## Y_TEST Instance Quota["+computeMetric.getInstanceQuota()+"]");
					log.debug("## Y_TEST Instance Usage["+computeMetric.getInstanceUsage()+"]");
				}

				OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
				computeMetric.setInstanceQuota(Double.valueOf(osClient.compute().servers().listAll(false).size()));	// 나중에 메타 데이터에 관리 되면 메타데이터 가져다가 셋팅하기.
//				computeMetric.setInstanceUsage();																			// 나중에 메타 데이터에 관리 되면 메타데이터 가져다가 셋팅하기.
				openstackComputeRepository.save(computeMetric);
			}catch (Exception e){
				e.printStackTrace();
				log.error("## Elasticsearch에서 project cpu 정보 setting중 에러발생. ["+e.getMessage()+"]");
				return false;
			}

			return true;
		}

	}


	/**
	 * ES에 Project Index 없으면 생성 & 업데이트.
	 * @param projectInfoVoList
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateProjectIndex(List<ProjectInfoVo> projectInfoVoList) {
		List<ProjectsMetric> projectsMetricList = new ArrayList<>();

		try {
			projectInfoVoList.forEach(ProjectInfoVo -> {
				ProjectsMetric projectsMetric = new ProjectsMetric();
				ProjectsMetric.Project projectInfo = new ProjectsMetric.Project();
				log.debug("## Y_TEST Project Name : "+ ProjectInfoVo.getName());

				// project Id & Timestamp Entity 셋팅.
//				projectsMetric.setId("TEST_ID");
				projectsMetric.setTimestamp(ProjectInfoVo.getTimestamp());

				// ProjectMetric.Project Entity 셋팅.
				projectInfo.setProjectId(ProjectInfoVo.getId());
				projectInfo.setProjectName(ProjectInfoVo.getName());
				projectInfo.setDomainId(ProjectInfoVo.getDomainId());
				projectInfo.setDescription(ProjectInfoVo.getDescription());
				projectInfo.setEnabled(ProjectInfoVo.isEnabled());
				projectInfo.setParentId(ProjectInfoVo.getParentId());
				projectInfo.setDomain(ProjectInfoVo.isDomain());
				if(ProjectInfoVo.getTags() != null){
					projectInfo.setTags(ProjectInfoVo.getTags());
				}
				if(ProjectInfoVo.getOptions() != null){
					projectInfo.setOptions(ProjectInfoVo.getOptions());
				}
				if(ProjectInfoVo.getLinks() != null){
					projectInfo.setLinks(ProjectInfoVo.getLinks());
				}

				// Hyperviosr 정보 셋팅.
				projectsMetric.setProject(projectInfo);
				// hypervisorList에 hyperviosr 정보 add.
				projectsMetricList.add(projectsMetric);
			});
			log.debug("## Y_TEST projectMetricList  Size : "+ projectsMetricList.size());
			openstackProjectsRepository.saveAll(projectsMetricList);
			return true;
		}catch (NoSuchIndexException e1){
			log.error("## ES Project Index is not Found! ["+e1.getMessage()+"]");
			e1.printStackTrace();
			return false;
		}catch (Exception e){
			log.error("## ES Prject Index save Error Found! ["+e.getMessage()+"]");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ES에 ProjectDetail Index 없으면 생성 & 업데이트.
	 * @param projectDetailMetricVoList
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateProjectDetailIndex(List<ProjectDetailMetricVo> projectDetailMetricVoList) {
		List<ProjectDetailMetric> projectDetailMetricList = new ArrayList<>();

		try {
			projectDetailMetricVoList.forEach(ProjectDetailMetricVo -> {
				ProjectDetailMetric projectDetailMetric = new ProjectDetailMetric();
//				ProjectDetailMetric.ProjectDetail projectDetailInfo = new ProjectDetailMetric.ProjectDetail();
				log.debug("## Y_TEST Project Name : "+ProjectDetailMetricVo.getProjectName());

				// projectDetail meta data Entity 셋팅.
				projectDetailMetric.setId(ProjectDetailMetricVo.getId());
//				projectDetailMetric.setDomain(ProjectDetailMetricVo.getDomain());
				projectDetailMetric.setDomain("default_2");
				long timestamp = ProjectDetailMetricVo.getTimestamp();
				Date date = new java.util.Date(timestamp);
				projectDetailMetric.setTimestamp(timestamp);

				ProjectDetailMetric.ProjectDetailInfo projectDetailInfo = new ProjectDetailMetric.ProjectDetailInfo();
				projectDetailMetric.setProjectDetailInfo(projectDetailInfo);
				projectDetailMetric.getProjectDetailInfo().setProjectId(ProjectDetailMetricVo.getProjectId());
				projectDetailMetric.getProjectDetailInfo().setProjectName(ProjectDetailMetricVo.getProjectName());
				projectDetailMetric.getProjectDetailInfo().setUserId(ProjectDetailMetricVo.getUserId());
				projectDetailMetric.getProjectDetailInfo().setGroupName(ProjectDetailMetricVo.getGroupName());

				ProjectDetailMetric.ProjectDetailInfo.ProjectQuotaSet projectQuotaSet = new ProjectDetailMetric.ProjectDetailInfo.ProjectQuotaSet();

				// ProjectDetailMetric.ProjectQuota Entity 셋팅.
				projectDetailMetric.getProjectDetailInfo().setProjectQuotaSet(projectQuotaSet);
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setCoresQuota(ProjectDetailMetricVo.getComputeQuota().getCoreQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setRamQuota(ProjectDetailMetricVo.getComputeQuota().getRamQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setInstancesQuota(ProjectDetailMetricVo.getComputeQuota().getInstanceQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setMetadataItemsQuota(ProjectDetailMetricVo.getComputeQuota().getMetadataItemsQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setInjectedFilesQuota(ProjectDetailMetricVo.getComputeQuota().getInjectFilesQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setInjectedFileContentBytesQuota(ProjectDetailMetricVo.getComputeQuota().getInjectedFileContentBytesQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setInjectedFilePathBytesQuota(ProjectDetailMetricVo.getComputeQuota().getInjectedFilePathBytesQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setKeyPairsQuota(ProjectDetailMetricVo.getComputeQuota().getKeyPairsQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setSubnetQuota(ProjectDetailMetricVo.getNetworkQuota().getSubnetQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setRouterQuota(ProjectDetailMetricVo.getNetworkQuota().getRouterQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setPortQuota(ProjectDetailMetricVo.getNetworkQuota().getPortQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setNetworkQuota(ProjectDetailMetricVo.getNetworkQuota().getNetworkQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setFixedIpsQuota(ProjectDetailMetricVo.getNetworkQuota().getFixedIpQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setFloatingIpQuota(ProjectDetailMetricVo.getNetworkQuota().getFloatingIpQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setSecurityGroupQuota(ProjectDetailMetricVo.getNetworkQuota().getSecurityGroupQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setSecurityGroupRuleQuota(ProjectDetailMetricVo.getNetworkQuota().getSecurityGroupRuleQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setVolumeQuota(ProjectDetailMetricVo.getVolumeQuota().getVolumeQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setSnapshotQuota(ProjectDetailMetricVo.getVolumeQuota().getSnapshotQuota());
				projectDetailMetric.getProjectDetailInfo().getProjectQuotaSet().setGigabyteQuota(ProjectDetailMetricVo.getVolumeQuota().getGigabyteQuota());

				// ProjectDetailMetric.ProjectUsage Entity 셋팅.
				ProjectDetailMetric.ProjectDetailInfo.ProjectUsages projectUsages = new ProjectDetailMetric.ProjectDetailInfo.ProjectUsages();
				projectDetailMetric.getProjectDetailInfo().setProjectUsage(projectUsages);
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setCoresUsage(ProjectDetailMetricVo.getComputeUsage().getCoreUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setRamUsage(ProjectDetailMetricVo.getComputeUsage().getRamUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setInstancesUsage(ProjectDetailMetricVo.getComputeUsage().getInstanceUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setMetadataItemsUsage(ProjectDetailMetricVo.getComputeUsage().getMetadataItemsUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setInjectedFilesUsage(ProjectDetailMetricVo.getComputeUsage().getInjectFilesUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setInjectedFileContentBytesUsage(ProjectDetailMetricVo.getComputeUsage().getInjectedFileContentBytesUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setInjectedFilePathBytesUsage(ProjectDetailMetricVo.getComputeUsage().getInjectedFilePathBytesUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setKeyPairsUsage(ProjectDetailMetricVo.getComputeUsage().getKeyPairsUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setSubnetUsage(ProjectDetailMetricVo.getNetworkUsage().getSubnetUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setRouterUsage(ProjectDetailMetricVo.getNetworkUsage().getRouterUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setPortUsage(ProjectDetailMetricVo.getNetworkUsage().getPortUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setNetworkUsage(ProjectDetailMetricVo.getNetworkUsage().getNetworkUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setFixedIpsUsage(ProjectDetailMetricVo.getNetworkUsage().getFixedIpUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setFloatingIpUsage(ProjectDetailMetricVo.getNetworkUsage().getFloatingIpUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setSecurityGroupUsage(ProjectDetailMetricVo.getNetworkUsage().getSecurityGroupUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setSecurityGroupRuleUsage(ProjectDetailMetricVo.getNetworkUsage().getSecurityGroupRuleUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setVolumeUsage(ProjectDetailMetricVo.getVolumeUsage().getVolumeUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setSnapshotUsage(ProjectDetailMetricVo.getVolumeUsage().getSnapshotUsed());
				projectDetailMetric.getProjectDetailInfo().getProjectUsage().setGigabyteUsage(ProjectDetailMetricVo.getVolumeUsage().getGigabyteUsed());



				// ProjectDetail 정보 셋팅. (projectQuotaSet(변수명) == ProjectDetail(클래스명))
//				projectDetailMetric.setProjectQuotaSet(projectDetailInfo);
				// ProjectDetailList에 ProjectDetail 정보 add.
				projectDetailMetricList.add(projectDetailMetric);
			});
			log.debug("## Y_TEST projectMetricList  Size : "+projectDetailMetricList.size());
			openstackProjectDetailRepository.saveAll(projectDetailMetricList);
			return true;
		}catch (NoSuchIndexException e1){
			log.error("## ES ProjcetDetail Index is not Found! ["+e1.getMessage()+"]");
			e1.printStackTrace();
			return false;
		}catch (Exception e){
			log.error("## ES ProjectDetail Index save Error Found! ["+e.getMessage()+"]");
			e.printStackTrace();
			return false;
		}
	}



	// ========================SELECT======================

	/**
	 * ES에서 Openstack관련 모든 메트릭 리스트를 메트릭 type을 보고 범위(gte&lte) 조회
	 * @param batchQueryRequestVo
	 * @return
	 */
	@Override
	@Transactional
	public List<OstProjectMetricVo> retrieveProjectMetricToRange(BatchQueryRequestVo batchQueryRequestVo) {
		OstProjectMetricVo ostProjectMetricVo = new OstProjectMetricVo();
		List<OstProjectMetricVo> ostProjectMetricVoList = new ArrayList<>();
		long curUnixTime = System.currentTimeMillis()/1000;

		index.setIndex("ost-metric-projects-detail"+"*");
		log.debug("## ProjectMetric Select Logic IN!! metricNm["+batchQueryRequestVo.getMetricName()+"]");
		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		SearchResponse response = null;
		Terms terms = null;
		Collection<Terms.Bucket> buckets = null;
		ParsedTopHits topHits = null;
		SearchHit topHitValue = null;
		long gte = dateUtil.getBreforeTimestamp(batchQueryRequestVo.getDateType(),Integer.valueOf(batchQueryRequestVo.getDateUnit()));
		long lte = System.currentTimeMillis();


		switch (batchQueryRequestVo.getMetricName().toUpperCase()){
			case "CPU" :
				response = esUtil.selectMetricToEs(esClient, index.getIndex(), "project_detail_info.project_usage.cores_usage" ,"desc" ,gte, lte);

				// ES 통계 쿼리 조회 결과값 Vo 셋팅.
				terms = response.getAggregations().get("test_aggs_terms");
				buckets = (Collection<Terms.Bucket>) terms.getBuckets();
				log.debug("## bucket Hits Size["+buckets.size()+"]");
				try{
					for (Terms.Bucket bucket : buckets) {
						topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
						topHitValue = topHits.getHits().getAt(0);
						ostProjectMetricVo = new OstProjectMetricVo();

						ostProjectMetricVo.setProjectName(bucket.getKeyAsString());

//						log.debug("## 1 hit result ["+topHitValue+"]");
						Gson gson = new Gson();
						JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
						Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
						prtGson.toJson(detailInfoJson);
						log.debug("## ProjectName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


						ostProjectMetricVo.setProjectId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_id").getAsString());
						ostProjectMetricVo.setUserId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("user_id").getAsString());
						ostProjectMetricVo.setDomain(detailInfoJson.get("domain").getAsString());
						ostProjectMetricVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());

						ostProjectMetricVo.setCpuQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("cores_quota").getAsInt());
						ostProjectMetricVo.setCpuUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("cores_usage").getAsInt());

						ostProjectMetricVoList.add(ostProjectMetricVo);
					}
				}catch (Exception e){
					e.printStackTrace();
					log.error("## Elasticsearch에서 project cpu 정보 setting중 에러발생. ["+e.getMessage()+"]");
				}

				log.debug("## buckets.size() ["+buckets.size()+"]");
				log.debug("## aggregation toString["+response.getInternalResponse().aggregations().get("tmp_term_test_result")+"]");
				log.debug("###################################################");


				index.setIndex(esUtil.getIdxName(batchQueryRequestVo.getMetricName())+"*");
				log.debug("## Y_TEST size : "+batchQueryRequestVo.getNumSeries());
				log.debug("## Y_TEST index name : "+index.getIndex());
				log.debug("## Y_TEST voList Size : "+ ostProjectMetricVoList.size());
				log.debug("## Y_TEST gte : "+gte);
				log.debug("## Y_TEST lte : "+lte);

				return ostProjectMetricVoList;

			case "RAM" :
				response = esUtil.selectMetricToEs(esClient, index.getIndex(), "project_detail_info.project_usage.ram_usage","desc",gte, lte);

				// ES 쿼리 조회 결과값 Vo 셋팅.
				terms = response.getAggregations().get("test_aggs_terms");
				buckets = (Collection<Terms.Bucket>) terms.getBuckets();
				log.debug("## bucket Hits Size["+buckets.size()+"]");
				try{
					for (Terms.Bucket bucket : buckets) {
						topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
						topHitValue = topHits.getHits().getAt(0);
						ostProjectMetricVo = new OstProjectMetricVo();

						ostProjectMetricVo.setProjectName(bucket.getKeyAsString());

//						log.debug("## 1 hit result ["+topHitValue+"]");
						Gson gson = new Gson();
						JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
						Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
						prtGson.toJson(detailInfoJson);
						log.debug("## ProjectName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


						ostProjectMetricVo.setProjectId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_id").getAsString());
						ostProjectMetricVo.setUserId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("user_id").getAsString());
						ostProjectMetricVo.setDomain(detailInfoJson.get("domain").getAsString());
						ostProjectMetricVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());

						ostProjectMetricVo.setMemoryQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("ram_quota").getAsInt());
						ostProjectMetricVo.setMemoryUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("ram_usage").getAsInt());

						ostProjectMetricVoList.add(ostProjectMetricVo);
					}
				}catch (Exception e){
					e.printStackTrace();
					log.error("## Elasticsearch에서 project ram 정보 setting중 에러발생. ["+e.getMessage()+"]");
				}

				log.debug("## buckets.size() ["+buckets.size()+"]");
				log.debug("## aggregation toString["+response.getInternalResponse().aggregations().get("tmp_term_test_result")+"]");
				log.debug("###################################################");


				index.setIndex(esUtil.getIdxName(batchQueryRequestVo.getMetricName())+"*");
				log.debug("## Y_TEST size : "+batchQueryRequestVo.getNumSeries());
				log.debug("## Y_TEST index name : "+index.getIndex());
				log.debug("## Y_TEST voList Size : "+ ostProjectMetricVoList.size());
				log.debug("## Y_TEST gte : "+gte);
				log.debug("## Y_TEST lte : "+lte);

				return ostProjectMetricVoList;

			case "INSTANCE" :
				response = esUtil.selectMetricToEs(esClient, index.getIndex(),"project_detail_info.project_usage.instances_usage" ,"desc", gte, lte);

				// ES 통계 쿼리 조회 결과값 Vo 셋팅.
				terms = response.getAggregations().get("test_aggs_terms");
				buckets = (Collection<Terms.Bucket>) terms.getBuckets();
				log.debug("## bucket Hits Size["+buckets.size()+"]");
				try{
					for (Terms.Bucket bucket : buckets) {
						topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
						topHitValue = topHits.getHits().getAt(0);
						ostProjectMetricVo = new OstProjectMetricVo();

						ostProjectMetricVo.setProjectName(bucket.getKeyAsString());

//						log.debug("## 1 hit result ["+topHitValue+"]");
						Gson gson = new Gson();
						JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
						Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
						prtGson.toJson(detailInfoJson);
						log.debug("## ProjectName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


						ostProjectMetricVo.setProjectId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_id").getAsString());
						ostProjectMetricVo.setUserId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("user_id").getAsString());
						ostProjectMetricVo.setDomain(detailInfoJson.get("domain").getAsString());
						ostProjectMetricVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());

						ostProjectMetricVo.setInstanceQuota(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("instances_quota").getAsInt());
						ostProjectMetricVo.setInstanceUsage(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("instances_usage").getAsInt());

						ostProjectMetricVoList.add(ostProjectMetricVo);
					}
				}catch (Exception e){
					e.printStackTrace();
					log.error("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
				}

				log.debug("## buckets.size() ["+buckets.size()+"]");
				log.debug("## aggregation toString["+response.getInternalResponse().aggregations().get("tmp_term_test_result")+"]");
				log.debug("###################################################");


				index.setIndex(esUtil.getIdxName(batchQueryRequestVo.getMetricName())+"*");
				log.debug("## Y_TEST size : "+batchQueryRequestVo.getNumSeries());
				log.debug("## Y_TEST index name : "+index.getIndex());
				log.debug("## Y_TEST voList Size : "+ ostProjectMetricVoList.size());
				log.debug("## Y_TEST gte : "+gte);
				log.debug("## Y_TEST lte : "+lte);

				return ostProjectMetricVoList;
			case "VOLUME" :
				response = esUtil.selectMetricToEs(esClient, index.getIndex(), "project_detail_info.project_usage.volume_usage" ,"desc", gte, lte);

				// ES 통계 쿼리 조회 결과값 Vo 셋팅.
				terms = response.getAggregations().get("test_aggs_terms");
				buckets = (Collection<Terms.Bucket>) terms.getBuckets();
				log.debug("## bucket Hits Size["+buckets.size()+"]");
				try{
					for (Terms.Bucket bucket : buckets) {
						topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
						topHitValue = topHits.getHits().getAt(0);
						ostProjectMetricVo = new OstProjectMetricVo();

						ostProjectMetricVo.setProjectName(bucket.getKeyAsString());

						Gson gson = new Gson();
						JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
						Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
						prtGson.toJson(detailInfoJson);
						log.debug("## ProjectName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


						ostProjectMetricVo.setProjectId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_id").getAsString());
						ostProjectMetricVo.setUserId(detailInfoJson.get("project_detail_info").getAsJsonObject().get("user_id").getAsString());
						ostProjectMetricVo.setDomain(detailInfoJson.get("domain").getAsString());
						ostProjectMetricVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());

						ostProjectMetricVo.setVolumeQuota(Double.valueOf(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_quota_set").getAsJsonObject().get("volume_quota").getAsInt()));
						ostProjectMetricVo.setVolumeUsage(Double.valueOf(detailInfoJson.get("project_detail_info").getAsJsonObject().get("project_usage").getAsJsonObject().get("volume_usage").getAsInt()));

						ostProjectMetricVoList.add(ostProjectMetricVo);
					}
				}catch (Exception e){
					e.printStackTrace();
					log.error("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
				}

				log.debug("## buckets.size() ["+buckets.size()+"]");
				log.debug("## aggregation toString["+response.getInternalResponse().aggregations().get("tmp_term_test_result")+"]");
				log.debug("###################################################");


				index.setIndex(esUtil.getIdxName(batchQueryRequestVo.getMetricName())+"*");
				log.debug("## Y_TEST size : "+batchQueryRequestVo.getNumSeries());
				log.debug("## Y_TEST index name : "+index.getIndex());
				log.debug("## Y_TEST voList Size : "+ ostProjectMetricVoList.size());
				log.debug("## Y_TEST gte : "+gte);
				log.debug("## Y_TEST lte : "+lte);

				return ostProjectMetricVoList;

			default:
				log.debug("조회할 메트릭 타입을 확인해주세요.");
				return ostProjectMetricVoList;
		}


	}

	/**
	 * ES에서 Openstack관련 모든 메트릭 리스트를 메트릭 type을 보고 범위(gte&lte) 조회
	 * @param metricName
	 * @param gte
	 * @param lte
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public Page<OpenstackMetric> retrieveAllMetricTypeToRange(String metricName, String gte, String lte, int size) {

//		index.setIndex(esUtil.getIdxName(metricName)+"-"+index.getIndexDate());
		index.setIndex(esUtil.getIdxName(metricName)+"*");
		log.debug("## Y_TEST index name : "+index.getIndex());
		log.debug("## Y_TEST size : "+size);
		log.debug("## Y_TEST gte : "+gte);
		log.debug("## Y_TEST lte : "+lte);

		return openstackRepository.findByIndexNameAndRange(index.getIndex(), gte, lte, PageRequest.of(0, size));
	}

	/**
	 * 조회시점 기준 현재 compute 정보 조회.
	 * @param metricName
	 * @param dateType
	 * @param dateUnit
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public OstComputeMetricVo retrieveComputeMetricToRange (String metricName, String dateType, String dateUnit, int size){

		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstComputeMetricVo aggregatedVo = new OstComputeMetricVo();
		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		ParsedTopHits topHits = null;
		SearchHit topHitValue = null;
		long gte = dateUtil.getBreforeTimestamp(dateType,Integer.valueOf(dateUnit));
		long lte = System.currentTimeMillis();
		long curUnixTime = System.currentTimeMillis()/1000;

		switch (metricName.toUpperCase()){
			case "CPU" :
				index.setIndex("ost-service-compute-"+index.getIndexDate());
				metricType = "vcpus";
				log.debug("## Y_TEST MetricType is CPU["+metricType+"]");
				break;
			case "INSTANCE" :
				index.setIndex("ost-service-compute-"+index.getIndexDate());
				metricType = "memory_mb";
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;


			default:
				index.setIndex("ost-service-compute-"+index.getIndexDate());
				log.debug("조회할 ComputeService metric Type(CPU,NETWORK,INSTANCE)을 확인해주세요. ");
				break;
		}

		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");


		SearchResponse response = null;
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex()+"*");


		QueryBuilder query = QueryBuilders.boolQuery()
				.must(matchQuery)
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)
						.lte(lte));

		try{
			response = esClient.search(new SearchRequest()
					.source(new SearchSourceBuilder()
							.query(query)
							.aggregation(
									AggregationBuilders.terms("class_term_result").field("_class.keyword").size(1000)
											.subAggregation(
													AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp", SortOrder.DESC)
											))
							.size(0)
							.trackTotalHits(true)
							.sort(SortBuilders.scoreSort())
					), RequestOptions.DEFAULT);
			log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(response.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
			e.printStackTrace();
		}
		log.debug("## Y_TEST gte["+gte+"]");
		log.debug("## Y_TEST lte["+lte+"]");

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Terms terms = response.getAggregations().get("class_term_result");
		Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();

		log.debug("## bucket Hits Size["+buckets.size()+"]");
		try{
			for (Terms.Bucket bucket : buckets) {
				topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
				topHitValue = topHits.getHits().getAt(0);
				aggregatedVo = new OstComputeMetricVo();

				aggregatedVo.setNovaName(bucket.getKeyAsString());

				Gson gson = new Gson();				// topHitValue 들어온 값 json 형태로 추출하기위해.
				JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
				Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
				prtGson.toJson(detailInfoJson);
				log.debug("## NovaName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


				aggregatedVo.setId(detailInfoJson.get("id").getAsString());


				if(metricName.equalsIgnoreCase("cpu")){				// cpu 조회일때.
					aggregatedVo.setCpuQuota((double)detailInfoJson.get("cpu_quota").getAsInt());
					aggregatedVo.setCpuUsage((double)detailInfoJson.get("cpu_usage").getAsInt());
				}else if(metricName.equalsIgnoreCase("network")){	// network 조회일때.
					aggregatedVo.setNetworkQuota((double)detailInfoJson.get("network_quota").getAsInt());
					aggregatedVo.setNetworkUsage((double)detailInfoJson.get("network_usage").getAsInt());
				}else if(metricName.equalsIgnoreCase("instance")){		// instance 조회일때.
					aggregatedVo.setInstanceQuota((double)detailInfoJson.get("instance_quota").getAsInt());
					aggregatedVo.setInstanceUsage((double)detailInfoJson.get("instance_usage").getAsInt());
				}else{															// cpu와 ram조회일때.
					log.debug("## else로 빠짐. 확인요망.");
					aggregatedVo.setCpuQuota((double)detailInfoJson.get("cpu_quota").getAsInt());
					aggregatedVo.setCpuUsage((double)detailInfoJson.get("cpu_usage").getAsInt());
					aggregatedVo.setNetworkQuota((double)detailInfoJson.get("network_quota").getAsInt());
					aggregatedVo.setNetworkUsage((double)detailInfoJson.get("network_usage").getAsInt());
					aggregatedVo.setInstanceQuota((double)detailInfoJson.get("instance_quota").getAsInt());
					aggregatedVo.setInstanceUsage((double)detailInfoJson.get("instance_usage").getAsInt());
				}
				aggregatedVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());
			}

		}catch (Exception e){
			e.printStackTrace();
			log.error("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
		}

		return aggregatedVo;
	}

	/**
	 * ES에서 Openstack Hypervisor N(시간,일) 범위동안 Cpu,Ram 사용량 통계 값 조회 (Top N). 하이퍼바이저는 지난주대비가 아니기 때문에 일반 범위조회.
	 * @param batchQueryRequestVo
	 * @return
	 */
	@Override
	@Transactional
	public List<OstHypervisorAggregatedVo>  retrieveAggregateHypervisorMetricToRange(BatchQueryRequestVo batchQueryRequestVo){
		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstHypervisorAggregatedVo aggregatedVo = new OstHypervisorAggregatedVo();
		List<OstHypervisorAggregatedVo> voList = new ArrayList<OstHypervisorAggregatedVo>();
		String metricName = batchQueryRequestVo.getMetricName();
		String dateType = batchQueryRequestVo.getDateType();
		String dateUnit = batchQueryRequestVo.getDateUnit();
		int numSeries = batchQueryRequestVo.getNumSeries();

		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		ParsedTopHits topHits = null;
		SearchHit topHitValue = null;
		long curUnixTime = System.currentTimeMillis();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		   // dateType의 timestamp를 longType으로 셋팅.
		Date date = null;
		long gte = dateUtil.getBreforeTimestamp(dateType,Integer.valueOf(dateUnit)); // unixtimestamp기준  타입기준 입력받은 dateUnit만큼 전 시간
		long lte = curUnixTime;
		DateHistogramInterval interval = null;
		log.debug("## Y_TEST gte ["+gte+"]");
		log.debug("## Y_TEST lte ["+lte+"]");


		switch (metricName.toUpperCase()){
			case "CPU" :
				index.setIndex("ost-metric-hypervisors-"+index.getIndexDate());
				metricType = "vcpus";
				log.debug("## Y_TEST MetricType is CPU["+metricType+"]");
				break;
			case "RAM" :
				index.setIndex("ost-metric-hypervisors-"+index.getIndexDate());
				metricType = "memory_mb";
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;
			case "INSTANCE" :
				index.setIndex("ost-metric-hypervisors-"+index.getIndexDate());
				metricType = "runningvms";
				log.debug("## Y_TEST MetricType is VMS["+metricType+"]");
				break;

			default:
				index.setIndex("ost-metric-hypervisors-"+index.getIndexDate());
				log.debug("조회할 Hypervisor metric Type(CPU,RAM)을 확인해주세요. 설정값이 올바르지않아 CPU & RAM 모두 조회합니다.");
				break;
		}

		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");

		SearchResponse response = null;
//		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex()+"*");


		QueryBuilder query = QueryBuilders.boolQuery()
//				.must(matchQuery)
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)
						.lte(lte));

		try{
			response = esClient.search(new SearchRequest(index.getIndex()+"*")
					.source(new SearchSourceBuilder()
							.query(query)
							.aggregation(
									AggregationBuilders.terms("name_term_result").field("hypervisor.hypervisor_hostname.keyword").size(numSeries)			// N개 조회.
											.subAggregation(
													AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp", SortOrder.DESC)
											))
							.size(0)
							.trackTotalHits(true)
							.sort(SortBuilders.scoreSort())
					), RequestOptions.DEFAULT);
			log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(response.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
			e.printStackTrace();
		}

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Terms terms = response.getAggregations().get("name_term_result");
		Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();


		log.debug("## bucket Hits Size["+buckets.size()+"]");
		try{
			for (Terms.Bucket bucket : buckets) {
				topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
				topHitValue = topHits.getHits().getAt(0);
				aggregatedVo = new OstHypervisorAggregatedVo();

				aggregatedVo.setHypervisorName(bucket.getKeyAsString());

				Gson gson = new Gson();				// topHitValue 들어온 값 json 형태로 추출하기위해.
				JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
				Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
				prtGson.toJson(detailInfoJson);
				log.debug("## HypervisorName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


				aggregatedVo.setHypervisorId(detailInfoJson.get("hypervisor").getAsJsonObject().get("hypervisor_id").getAsString());
				aggregatedVo.setHypervisorName(detailInfoJson.get("hypervisor").getAsJsonObject().get("hypervisor_hostname").getAsString());
				aggregatedVo.setCurStatus(detailInfoJson.get("hypervisor").getAsJsonObject().get("status").getAsString());

				if(metricName.equalsIgnoreCase("cpu")){				// cpu 조회일때.
					aggregatedVo.setVcpuQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble());
					aggregatedVo.setVcpuUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble());
//					aggregatedVo.setVcpuUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble() * cpuOverCount)) * 100.0);
					aggregatedVo.setVcpuUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble())) * 100.0);
				}else if(metricName.equalsIgnoreCase("ram")){		// ram 조회일때.
					aggregatedVo.setMemoryQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble());
					aggregatedVo.setMemoryUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble());
//					aggregatedVo.setMemoryUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble() * ramOverCount)) * 100.0);
					aggregatedVo.setMemoryUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble())) * 100.0);
				}else if(metricName.equalsIgnoreCase("instance")) {        // ram 조회일때.
					aggregatedVo.setRunningvms(detailInfoJson.get("hypervisor").getAsJsonObject().get("running_vms").getAsInt());
				}else{															// cpu와 ram조회일때.
					log.debug("## else로 빠짐. 확인요망.");
					aggregatedVo.setVcpuQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble());
					aggregatedVo.setVcpuUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble());
//					aggregatedVo.setVcpuUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble() * cpuOverCount)) * 100.0);
					aggregatedVo.setVcpuUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble())) * 100.0);

					aggregatedVo.setMemoryQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble());
					aggregatedVo.setMemoryUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble());
//					aggregatedVo.setMemoryUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble() * ramOverCount)) * 100.0);
					aggregatedVo.setMemoryUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble())) * 100.0);
					aggregatedVo.setRunningvms(detailInfoJson.get("hypervisor").getAsJsonObject().get("running_vms").getAsInt());
				}


				log.debug("## Y_TEST vcpu percent ["+(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble())) * 100.0+"]");
				log.debug("## Y_TEST memory percent ["+(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble())) * 100.0+"]");
				log.debug("## Y_TEST running ["+aggregatedVo.getRunningvms()+"]");

				aggregatedVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());
				voList.add(aggregatedVo);
			}
			log.debug("## Y_TEST metircName["+metricName+"]");

			if(metricName.equalsIgnoreCase("cpu")){			// cpu 기준 내림차순.
				Collections.sort(voList, new Comparator<OstHypervisorAggregatedVo>() {
					@Override
					public int compare(OstHypervisorAggregatedVo o1, OstHypervisorAggregatedVo o2) {
						return o1.getVcpuUsagePercent().compareTo(o2.getVcpuUsagePercent());
					}
				});
				Collections.reverse(voList);
			}else if(metricName.equalsIgnoreCase("ram")){			// cpu 기준 내림차순.
				Collections.sort(voList, new Comparator<OstHypervisorAggregatedVo>() {
					@Override
					public int compare(OstHypervisorAggregatedVo o1, OstHypervisorAggregatedVo o2) {
						return o1.getMemoryUsagePercent().compareTo(o2.getMemoryUsagePercent());
					}
				});
				Collections.reverse(voList);
			}else{														// instance 기준 내림차순.
				Collections.sort(voList, new Comparator<OstHypervisorAggregatedVo>() {
					@Override
					public int compare(OstHypervisorAggregatedVo o1, OstHypervisorAggregatedVo o2) {
						return o1.getRunningvms().compareTo(o2.getRunningvms());
					}
				});
				Collections.reverse(voList);
			}

		}catch (Exception e){
			e.printStackTrace();
			log.error("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
			return null;
		}

		return voList;

	}

	/**
	 * ES에서 Openstack Hypervisor Cpu,Ram, Instance 사용량 그래프 통계 값 조회.
	 * @param batchQueryRequestVo
	 * @return
	 */
	@Override
	@Transactional
	public OstHypervisorGraphAggregatedVo  retrieveGraphAggregateHypervisorMetricToRange(BatchQueryRequestVo batchQueryRequestVo){
		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstHypervisorGraphAggregatedVo graphAggregatedVo = new OstHypervisorGraphAggregatedVo();
		OstHypervisorAggregatedVo aggregatedVo = new OstHypervisorAggregatedVo();
		List<OstHypervisorAggregatedVo> voList = new ArrayList<OstHypervisorAggregatedVo>();

		ArrayList<ArrayList<Long>> timesList = new ArrayList<>();
		ArrayList<ArrayList<Double>> valuesList = new ArrayList<>();

		ArrayList<Long> timeArr  = new ArrayList<>();
		ArrayList<Double> valueArr = new ArrayList<>();
		ArrayList<String> seriesArr = new ArrayList<>();

		ArrayList<String> hostTopList = batchQueryRequestVo.getTopList();
		String metricName = batchQueryRequestVo.getMetricName();
		String dateType = batchQueryRequestVo.getDateType();
		String dateUnit = batchQueryRequestVo.getDateUnit();
		int numSeries = batchQueryRequestVo.getNumSeries();
		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		Date date = null;
		String gte = null;
		String lte = null;
		DateHistogramInterval interval = null;



		log.debug("## Y_TEST timesList.Size["+timesList.size()+"]");
		log.debug("## Y_TEST valuesList.Size["+valuesList.size()+"]");

		if(dateType.equalsIgnoreCase("day") || dateType.equalsIgnoreCase("d")){
			gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/d";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
		}else if(dateType.equalsIgnoreCase("hour") || dateType.equalsIgnoreCase("h")){
			gte = "now-"+dateUnit+"h/h";																// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/h";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
		}else{
			gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간, 입력안받았으면 기본 1
			lte = "now/d";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));	 					// 20초로 고정. 추후 설정값으로 받아야함.
		}

		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		String usageMetricField = null;
		String quotaMetricField = null;


		switch (metricName.toUpperCase()){
			case "CPU" :
				metricType = "cores";
				quotaMetricField = "system.cpu.cores";
				usageMetricField = "system.cpu.total.norm.pct";
				index.setIndex("sym-metric-cpu-");
				log.debug("## Y_TEST MetricType is CPU["+metricType+"]");
				break;
			case "RAM" :
				metricType = "memory";
				quotaMetricField = "system.memory.actual.free";
				usageMetricField = "system.memory.actual.used.pct";
				index.setIndex("sym-metric-memory-");
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;

			default:
				index.setIndex("sym-metric-cpu-");
				log.debug("조회할 Instance metric Type(CPU,RAM)을 확인해주세요.. ");
		}

		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");

		// bucketPath
		Map<String, String> bucketsPathsMap = new HashMap<String, String>();
		bucketsPathsMap.put("total_"+metricName.toLowerCase()+"_usage_avg","total_"+metricName.toLowerCase()+"_usage_avg");
		Script usageScript = new Script("params.total_"+metricName.toLowerCase()+"_usage_avg * 100");

		// sort관련 정보 셋팅
		List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().ASC));	// 오름차순
		sorts.add(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order().DESC)); // 내림차순

		SearchResponse response = null;


		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("host.hostname.keyword",hostTopList))				// defualt top 5개 호스트명 셋팅.
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)
						.lte(lte))
				.mustNot(QueryBuilders.existsQuery("cloud"));

		try{
			response =
					esClient.search(new SearchRequest(index.getIndex()+"*")
							.source(new SearchSourceBuilder()
									.query(query)
									.aggregation(
											AggregationBuilders.dateHistogram("total_agg").field("@timestamp").fixedInterval(interval)
													.subAggregation(
															AggregationBuilders.terms("term_agg_result").field("host.hostname.keyword").size(numSeries)				// 설정 사이즈 만큼 추출
																	.subAggregation(
																			AggregationBuilders.avg("total_"+metricName.toLowerCase()+"_usage_avg").field(usageMetricField)
																	).subAggregation(
																	PipelineAggregatorBuilders.bucketScript("total_"+metricName.toLowerCase()+"_usage_avg_percent",bucketsPathsMap,usageScript)
															).subAggregation(
																	PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
															)
													)
									)
									.size(0)
									.trackTotalHits(true)
									.sort(SortBuilders.scoreSort())
							), RequestOptions.DEFAULT);

			log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(response.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			e.printStackTrace();
			log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
		}

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Histogram histogram = response.getAggregations().get("total_agg");
		Collection<Histogram.Bucket> histogramBuckets = (Collection<Histogram.Bucket>) histogram.getBuckets();
		Terms terms = null;
		Collection<Terms.Bucket> bucketsList = null;


		SimpleValue usageBucket = null;
		String termInstanceNm = null;
		log.debug("## histogram bucket Hits Size["+histogramBuckets.size()+"]");

		try{
			for (Histogram.Bucket bucket : histogramBuckets) {											// 기간 통계 voList에 담음.

				usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

				terms = bucket.getAggregations().get("term_agg_result");
				bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

				date = transFormat.parse(bucket.getKeyAsString());    // dateType의 timestamp를 longType으로 셋팅.

				for(Terms.Bucket termsBucket : bucketsList){			// 결과값 Vo 셋팅.
					aggregatedVo = new OstHypervisorAggregatedVo();

					termInstanceNm = termsBucket.getKeyAsString();
					usageBucket = (SimpleValue) termsBucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

					aggregatedVo.setTimestamp(date.getTime());
					aggregatedVo.setHypervisorName(termInstanceNm);

					if(metricType.equalsIgnoreCase("cores")){
						aggregatedVo.setVcpuUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
					}else if(metricType.equalsIgnoreCase("memory")){
						aggregatedVo.setMemoryUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
					}
					voList.add(aggregatedVo);
				}
			}

			// 추출한 voList를 Top5 물리 호스트 별로 times, values, series 에 담기. 이때 정렬은 따로안하고 애초에 전달받은 물리 호스트가 Top 5 순서대로 전달받아야함.
			for(String tmpInstanceNm : hostTopList){
				timeArr = new ArrayList<>();
				valueArr = new ArrayList<>();
				for(OstHypervisorAggregatedVo tmpVo : voList ){
					if(tmpInstanceNm.equalsIgnoreCase(tmpVo.getHypervisorName())){
						timeArr.add(tmpVo.getTimestamp());
						if(metricType.equalsIgnoreCase("cores")){
							valueArr.add(tmpVo.getVcpuUsagePercent());
						}else if(metricType.equalsIgnoreCase("memory")){
							valueArr.add(tmpVo.getMemoryUsagePercent());
						}
					}
				}
				if(valueArr.size() != 0 && timeArr.size() != 0){				// 위 for문에서 물리 호스트 네임이 존재할때만 추가.
					timesList.add(timeArr);
					valuesList.add(valueArr);
					seriesArr.add(tmpInstanceNm);
				}

			}

			graphAggregatedVo.setTimes(timesList);
			graphAggregatedVo.setValues(valuesList);
			graphAggregatedVo.setSeries(seriesArr);

		}catch (Exception e){
			e.printStackTrace();
			log.error("## Elasticsearch에서 Instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
			return null;
		}


		return graphAggregatedVo;
	}

	/**
	 * ES에서 Openstack Project Cpu,Ram,Instance 지난 주 대비 사용량 통계 값 조회.
	 * @param batchQueryRequestVo
	 * @return
	 */
	@Override
	@Transactional
	public List<OstProjectAggregatedVo> retrieveAggregateProjectMetricToRange(BatchQueryRequestVo batchQueryRequestVo) {

		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstProjectAggregatedVo aggregatedVo = new OstProjectAggregatedVo();
		List<OstProjectAggregatedVo> weekAggVoList = new ArrayList<OstProjectAggregatedVo>();
		List<OstProjectAggregatedVo> hourAggVoList = new ArrayList<OstProjectAggregatedVo>();
		List<OstProjectAggregatedVo> compareAggVoList = new ArrayList<OstProjectAggregatedVo>();

		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		long gte = dateUtil.getBreforeWeekTimestamp(batchQueryRequestVo.getDateType(),Integer.valueOf(batchQueryRequestVo.getDateUnit()));
		long lte = System.currentTimeMillis();
		long curUnixTime = System.currentTimeMillis()/1000;

		switch (batchQueryRequestVo.getMetricName().toUpperCase()){
			case "CPU" :
				index.setIndex("ost-metric-projects-detail-"+index.getIndexDate());
				metricType = "cores";
				break;
			case "RAM" :
				index.setIndex("ost-metric-projects-detail-"+index.getIndexDate());
				metricType = "ram";
				break;
			case "INSTANCE" :
				index.setIndex("ost-metric-projects-detail-"+index.getIndexDate());
				metricType = "instances";
				break;

			default:
				index.setIndex("ost-metric-projects-detail-"+index.getIndexDate());
				log.debug("조회할 Project metric Type을 확인해주세요.");
				break;
		}

		Map<String, String> bucketsPathsMap = new HashMap<String, String>();
		bucketsPathsMap.put("total_quota_avg","total_quota_avg");
		bucketsPathsMap.put("total_usage_avg","total_usage_avg");


		Script script = new Script("params.total_quota_avg - params.total_usage_avg");
		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");

		SearchResponse weekResponse = null;
		SearchResponse hourResponse = null;
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex()+"*");
		PipelineAggregatorBuilders.bucketScript("tmp_pct",bucketsPathsMap,script);
		List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_pct").order(new FieldSortBuilder("tmp_pct").order().ASC));	// 오름차순
		sorts.add(new FieldSortBuilder("tmp_pct").order(new FieldSortBuilder("tmp_pct").order().DESC)); // 내림차순



		QueryBuilder query = QueryBuilders.boolQuery()
				.must(matchQuery)
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)		// 1주일+1시간전 unix 시간.
						.lte(lte));				// 현재 unix 시간.


		try{													// 지난 1주일+(기준시간)간 사용량 통계.
			weekResponse = getAggQueryResponse(esClient,query,metricType,bucketsPathsMap,script,sorts,0);

			log.debug("## elasticsearch response: {"+weekResponse.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(weekResponse.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
			e.printStackTrace();
		}


		query = QueryBuilders.boolQuery()
				.must(matchQuery)
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(dateUtil.getBreforeTimestamp(batchQueryRequestVo.getDateType(), Integer.valueOf(batchQueryRequestVo.getDateUnit())))		// default 1시간전 unix 시간.
						.lte(lte));				// 현재 unix 시간.

		try{													// 지난 1시간 사용량 통계 top 8. 내림차순기준
			hourResponse = getAggQueryResponse(esClient,query,metricType,bucketsPathsMap,script,sorts,8);

			log.debug("## elasticsearch response: {"+hourResponse.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(hourResponse.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
			e.printStackTrace();
		}

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Terms weekTerms = weekResponse.getAggregations().get("name_term_result");
		Terms hourTerms = hourResponse.getAggregations().get("name_term_result");

		Collection<Terms.Bucket> weekBuckets = (Collection<Terms.Bucket>) weekTerms.getBuckets();
		Collection<Terms.Bucket> hourBuckets = (Collection<Terms.Bucket>) hourTerms.getBuckets();

		ParsedAvg quotaAvg = null;
		ParsedAvg usageAvg = null;
		SimpleValue minQuotaUsage = null;

		for (Terms.Bucket bucket : weekBuckets) {											// 1주일 통계 voList에 담음.
			aggregatedVo = new OstProjectAggregatedVo();
			quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_quota_avg");
			usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_usage_avg");
			minQuotaUsage = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_pct");

			aggregatedVo.setProjectName(bucket.getKeyAsString());
			log.debug("## Y_TEST metricName is ["+batchQueryRequestVo.getMetricName()+"]");
			log.debug("## Y_TEST metricName.toUpperCase is ["+batchQueryRequestVo.getMetricName().toUpperCase()+"]");

			if(metricType.equalsIgnoreCase("cores")){
				aggregatedVo.setCpuQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setCpuUsageAvg(usageAvg.getValue());
				aggregatedVo.setCpuAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
			}else if(metricType.equalsIgnoreCase("ram")){
				aggregatedVo.setRamQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setRamUsageAvg(usageAvg.getValue());
				aggregatedVo.setRamAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
			}else if(metricType.equalsIgnoreCase("instances")){
				aggregatedVo.setInstanceQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setInstanceUsageAvg(usageAvg.getValue());
				aggregatedVo.setInstanceAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
			}

			aggregatedVo.setTimestamp(curUnixTime);

			weekAggVoList.add(aggregatedVo);
			log.debug("key:"+bucket.getKey()+", quotaAvg["+quotaAvg.getValue()+"], usageAvg["+usageAvg.getValue()+"], quotaAvg-usageAvg["+Double.parseDouble(minQuotaUsage.getValueAsString())+"]");
		}



		log.debug("## Y_TEST hourBucket["+hourBuckets.size()+"]");

		for (Terms.Bucket bucket : hourBuckets) {											// 1시간 통계 voList에 담음.
			aggregatedVo = new OstProjectAggregatedVo();
			quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_quota_avg");
			usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_usage_avg");
			minQuotaUsage = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_pct");

			aggregatedVo.setProjectName(bucket.getKeyAsString());


			if(metricType.equalsIgnoreCase("cores")){		// cpu 조회 일때.
				aggregatedVo.setCpuQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setCpuUsageAvg(usageAvg.getValue());
				aggregatedVo.setCpuAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setCpuUsageCompareAvg(0.0);
			}else if(metricType.equalsIgnoreCase("ram")){	// ram 조회 일때.
				aggregatedVo.setRamQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setRamUsageAvg(usageAvg.getValue());
				aggregatedVo.setRamAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setRamUsageCompareAvg(0.0);
			}else if(metricType.equalsIgnoreCase("instances")){// instance 조회 일때.
				aggregatedVo.setInstanceQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setInstanceUsageAvg(usageAvg.getValue());
				aggregatedVo.setInstanceAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setInstanceUsageCompareAvg(0.0);
			}else{														// metricType이 그외일때. 모두 셋팅.
				aggregatedVo.setCpuQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setCpuUsageAvg(usageAvg.getValue());
				aggregatedVo.setCpuAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setRamQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setRamUsageAvg(usageAvg.getValue());
				aggregatedVo.setRamAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setInstanceQuotaAvg(quotaAvg.getValue());
				aggregatedVo.setInstanceUsageAvg(usageAvg.getValue());
				aggregatedVo.setInstanceAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
				aggregatedVo.setCpuUsageCompareAvg(0.0);
				aggregatedVo.setRamUsageCompareAvg(0.0);
				aggregatedVo.setInstanceUsageCompareAvg(0.0);
			}

			aggregatedVo.setTimestamp(curUnixTime);

			for(OstProjectAggregatedVo aggVo : weekAggVoList){
				if(aggVo.getProjectName().equalsIgnoreCase(aggregatedVo.getProjectName())){			// 1시간 top 8 프로젝트명이 1주일 통계 vo에 있을때.
					log.debug("## Y_TEST 프로젝트 찾음. ["+aggVo.getProjectName()+"]");
					OstProjectAggregatedVo tmpCompareVo = new OstProjectAggregatedVo();
					tmpCompareVo = aggregatedVo;
					if(metricType.equalsIgnoreCase("cores")){							// cpu 사용량 비교 값 셋팅.
						tmpCompareVo.setCpuUsageCompareAvg(aggregatedVo.getCpuAvgMin() - aggVo.getCpuUsageAvg());
					}else if(metricType.equalsIgnoreCase("ram")){						// ram 사용량 비교 값 셋팅.
						tmpCompareVo.setRamUsageCompareAvg(aggregatedVo.getRamAvgMin() - aggVo.getRamUsageAvg());
					}else if(metricType.equalsIgnoreCase("instances")){					// instance 사용량 비교 값 셋팅.
						tmpCompareVo.setInstanceUsageCompareAvg(aggregatedVo.getInstanceAvgMin() - aggVo.getInstanceUsageAvg());
					}
					compareAggVoList.add(tmpCompareVo);
					break;
				}
			}

			hourAggVoList.add(aggregatedVo);
			log.debug("key:"+bucket.getKey()+", quotaAvg["+quotaAvg.getValue()+"], usageAvg["+usageAvg.getValue()+"], quotaAvg-usageAvg["+Double.parseDouble(minQuotaUsage.getValueAsString())+"]");
		}


		log.debug("###################################################");


//		index.setIndex(esUtil.getIdxName(batchQueryRequestVo.getMetricName())+"*");
		log.debug("## Y_TEST index name : "+index.getIndex());
		log.debug("## Y_TEST gte : "+gte);
		log.debug("## Y_TEST lte : "+lte);
		log.debug("## Y_TEST voList Size : "+compareAggVoList.size());

		return compareAggVoList;
	}


	/**
	 * ES에서 Openstack Instance 관련 메트릭(cpu,memory) 범위 통계 조회 (동적 limit 설정).
	 * @param batchQueryRequestVo
	 * @return
	 */
	@Override
	@Transactional
	public List<OstInstanceAggregatedVo> retrieveAggregateInstanceMetricToRange(BatchQueryRequestVo batchQueryRequestVo){
		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstInstanceAggregatedVo aggregatedVo = new OstInstanceAggregatedVo();
		List<OstInstanceAggregatedVo> voList = new ArrayList<OstInstanceAggregatedVo>();
		Map<String, List<OstInstanceAggregatedVo>> aggVoListMap = new HashMap<>();
		String gte = null;
		String lte = null;

		if(batchQueryRequestVo.getDateType().equalsIgnoreCase("day") || batchQueryRequestVo.getDateType().equalsIgnoreCase("d")){
			gte = "now-"+batchQueryRequestVo.getDateUnit()+"d/d";	// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/d";				// date 타입기준 현재시간
		}else if(batchQueryRequestVo.getDateType().equalsIgnoreCase("hour") || batchQueryRequestVo.getDateType().equalsIgnoreCase("h")){
			gte = "now-"+batchQueryRequestVo.getDateUnit()+"h/h";	// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/h";				// date 타입기준 현재시간
		}else{
			gte = "now-"+batchQueryRequestVo.getDateUnit()+"d/d";	// date 타입기준 입력받은 dateUnit만큼 전 시간, 입력안받았으면 기본 1
			lte = "now/d";				// date 타입기준 현재시간
		}

		// NumberSeries 체크
		if(batchQueryRequestVo.getNumSeries() == 0 || batchQueryRequestVo.getNumSeries() == null){
			batchQueryRequestVo.setNumSeries(10);													// default Top 10
		}

		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		String usageMetricField = null;
		String quotaMetricField = null;

		ParsedTopHits topHits = null;
		SearchHit topHitValue = null;
		long curUnixTime = System.currentTimeMillis()/1000;

		switch (batchQueryRequestVo.getMetricName().toUpperCase()){
			case "CPU" :
				metricType = "cores";
				quotaMetricField = "system.cpu.cores";
				usageMetricField = "system.cpu.total.norm.pct";
				index.setIndex("sym-metric-cpu-");
				log.debug("## Y_TEST MetricType is CPU["+metricType+"]");
				break;
			case "RAM" :
				metricType = "memory";
				quotaMetricField = "system.memory.actual.free";
				usageMetricField = "system.memory.actual.used.pct";
				index.setIndex("sym-metric-memory-");
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;

			default:
				index.setIndex("sym-metric-cpu-");
				log.debug("조회할 Instance metric Type(CPU,RAM)을 확인해주세요.. ");
				return null;
		}

		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");

		// bucketPath
		Map<String, String> bucketsPathsMap = new HashMap<String, String>();
		bucketsPathsMap.put("total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg","total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg");
		Script quotaScript = new Script("100.0 - (params.total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg * 100)");
		Script usageScript = new Script("params.total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg * 100");

		SearchResponse response = null;

		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)
						.lte(lte));

		try{
			response =
					esClient.search(new SearchRequest(index.getIndex()+"*")
							.source(new SearchSourceBuilder()
									.query(query)
									.aggregation(
											AggregationBuilders.terms("term_agg_result").field("cloud.instance.name.keyword").size(batchQueryRequestVo.getNumSeries())
													.subAggregation(
															AggregationBuilders.avg("total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_quota_avg")
																	.field(quotaMetricField)
													).subAggregation(
													AggregationBuilders.avg("total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg")
															.field(usageMetricField)
											).subAggregation(
													PipelineAggregatorBuilders.bucketScript("tmp_quotaAvg",bucketsPathsMap,quotaScript)
											).subAggregation(
													PipelineAggregatorBuilders.bucketScript("tmp_usageAvg",bucketsPathsMap,usageScript)
											))
									.size(0)
									.trackTotalHits(true)
									.sort(SortBuilders.scoreSort())
							), RequestOptions.DEFAULT);
			log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(response.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			e.printStackTrace();
			log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
			return null;
		}

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Terms terms = response.getAggregations().get("term_agg_result");
		Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();


		log.debug("## bucket Hits Size["+buckets.size()+"]");


		Collection<Terms.Bucket> bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

		ParsedAvg quotaAvg = null;
		ParsedAvg usageAvg = null;
		SimpleValue quotaBucket = null;
		SimpleValue usageBucket = null;

		log.debug("## bucket Hits Size["+buckets.size()+"]");
		try{
			for (Terms.Bucket bucket : bucketsList) {											// 기간 통계 voList에 담음.
				aggregatedVo = new OstInstanceAggregatedVo();
				quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_quota_avg");
				usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_"+batchQueryRequestVo.getMetricName().toLowerCase()+"_usage_avg");
				quotaBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_quotaAvg");
				usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_usageAvg");


				aggregatedVo.setInstanceName(bucket.getKeyAsString());
				log.debug("## Y_TEST metricName is ["+batchQueryRequestVo.getMetricName()+"]");
				log.debug("## Y_TEST metricName.toUpperCase is ["+batchQueryRequestVo.getMetricName().toUpperCase()+"]");
				log.debug("## Y_TEST Key_as_String["+bucket.getKeyAsString()+"]");
				log.debug("## Y_TEST quotaAvg["+quotaAvg.getValue()+"]");
				log.debug("## Y_TEST usageAvg["+usageAvg.getValue()+"]");
				log.debug("## Y_TEST quotaBucket["+quotaBucket.getValueAsString()+"]");
				log.debug("## Y_TEST usageBucket["+usageBucket.getValueAsString()+"]");


				if(metricType.equalsIgnoreCase("cores")){
					aggregatedVo.setCpuQuotaAvg(Double.parseDouble(quotaBucket.getValueAsString()));
					aggregatedVo.setCpuUsageAvg(Double.parseDouble(usageBucket.getValueAsString()));
				}else if(metricType.equalsIgnoreCase("memory")){
					aggregatedVo.setRamQuotaAvg(Double.parseDouble(quotaBucket.getValueAsString()));
					aggregatedVo.setRamUsageAvg(Double.parseDouble(usageBucket.getValueAsString()));
				}

				aggregatedVo.setTimestamp(curUnixTime);

				voList.add(aggregatedVo);
			}

		}catch (Exception e){
			e.printStackTrace();
			log.error("## Elasticsearch에서 Instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
			return null;
		}

		return voList;
	}


	/**
	 * ES에서 Openstack Instance 관련 메트릭 범위 그래프 통계 조회.
	 * @param metricName
	 * @param dateType
	 * @param dateUnit
	 * @return
	 */
	@Override
	@Transactional
	public OstInstanceGraphAggregatedVo retrieveGraphAggregateInstanceMetricToRange(String metricName, String dateType, String dateUnit, int numSeries, ArrayList<String> instanceTopList){
		RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();
		OstInstanceGraphAggregatedVo graphAggregatedVo = new OstInstanceGraphAggregatedVo();
		OstInstanceAggregatedVo aggregatedVo = new OstInstanceAggregatedVo();
		List<OstInstanceAggregatedVo> voList = new ArrayList<OstInstanceAggregatedVo>();

		ArrayList<ArrayList<Long>> timesList = new ArrayList<>();
		ArrayList<ArrayList<Double>> valuesList = new ArrayList<>();


		log.debug("## Y_TEST timesList.Size["+timesList.size()+"]");
		log.debug("## Y_TEST valuesList.Size["+valuesList.size()+"]");

		ArrayList<Long> timeArr  = new ArrayList<>();
		ArrayList<Double> valueArr = new ArrayList<>();
		ArrayList<String> seriesArr = new ArrayList<>();

		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
		Date date = null;
		String gte = null;
		String lte = null;
		DateHistogramInterval interval = null;


		if(dateType.equalsIgnoreCase("day") || dateType.equalsIgnoreCase("d")){
			gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/d";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
		}else if(dateType.equalsIgnoreCase("hour") || dateType.equalsIgnoreCase("h")){
			gte = "now-"+dateUnit+"h/h";																// date 타입기준 입력받은 dateUnit만큼 전 시간
			lte = "now/h";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
		}else{
			gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간, 입력안받았으면 기본 1
			lte = "now/d";																				// date 타입기준 현재시간
			interval = DateHistogramInterval.seconds(Integer.parseInt("20"));	 					// 20초로 고정. 추후 설정값으로 받아야함.
		}

		JSONObject resultJson = null;
		String resultStr = null;
		String metricType = null;
		String usageMetricField = null;
		String quotaMetricField = null;


		switch (metricName.toUpperCase()){
			case "CPU" :
				metricType = "cores";
				quotaMetricField = "system.cpu.cores";
				usageMetricField = "system.cpu.total.norm.pct";
				index.setIndex("sym-metric-cpu-");
				log.debug("## Y_TEST MetricType is CPU["+metricType+"]");
				break;
			case "RAM" :
				metricType = "memory";
				quotaMetricField = "system.memory.actual.free";
				usageMetricField = "system.memory.actual.used.pct";
				index.setIndex("sym-metric-memory-");
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;

			case "DISK" :																	// 개발해야함. 2021-06-28
				metricType = "disk";
				quotaMetricField = "system.diskio.read.bytes";
				usageMetricField = "system.diskio.write.bytes";
				index.setIndex("sym-metric-memory-");
				log.debug("## Y_TEST MetricType is MEMORY["+metricType+"]");
				break;

			default:
				index.setIndex("sym-metric-cpu-");
				log.debug("조회할 Instance metric Type(CPU,RAM)을 확인해주세요.. ");
		}

		log.debug("## Y_TEST IndexName ["+index.getIndex()+"]");

		// bucketPath
		Map<String, String> bucketsPathsMap = new HashMap<String, String>();
		bucketsPathsMap.put("total_"+metricName.toLowerCase()+"_usage_avg","total_"+metricName.toLowerCase()+"_usage_avg");
		Script usageScript = new Script("params.total_"+metricName.toLowerCase()+"_usage_avg * 100");

		// sort관련 정보 셋팅
		List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().ASC));	// 오름차순
		sorts.add(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order().DESC)); // 내림차순

		SearchResponse response = null;


		QueryBuilder query = QueryBuilders.boolQuery()
				.must(QueryBuilders.termsQuery("cloud.instance.name.keyword",instanceTopList))				// top 5개 인스턴스명 셋팅.
				.must(QueryBuilders.rangeQuery("@timestamp")
						.gte(gte)
						.lte(lte));

		try{
			response =
					esClient.search(new SearchRequest(index.getIndex()+"*")
							.source(new SearchSourceBuilder()
									.query(query)
									.aggregation(
											AggregationBuilders.dateHistogram("total_agg").field("@timestamp").fixedInterval(interval)
													.subAggregation(
															AggregationBuilders.terms("term_agg_result").field("cloud.instance.name.keyword").size(numSeries)				// 설정 사이즈 만큼 추출
																	.subAggregation(
																			AggregationBuilders.avg("total_"+metricName.toLowerCase()+"_usage_avg").field(usageMetricField)
																	).subAggregation(
																	PipelineAggregatorBuilders.bucketScript("total_"+metricName.toLowerCase()+"_usage_avg_percent",bucketsPathsMap,usageScript)
															).subAggregation(
																	PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
															)
													)
									)
									.size(0)
									.trackTotalHits(true)
									.sort(SortBuilders.scoreSort())
							), RequestOptions.DEFAULT);

			log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
			resultJson = new JSONObject(response.toString());
			resultStr = resultJson.toString(1);
			log.debug("## elasticsearch response: ["+resultStr+"] toString");
		}catch (Exception e){
			e.printStackTrace();
			log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
		}

		// ES 통계 쿼리 조회 결과값 Vo 셋팅.
		Histogram histogram = response.getAggregations().get("total_agg");
		Collection<Histogram.Bucket> histogramBuckets = (Collection<Histogram.Bucket>) histogram.getBuckets();
		Terms terms = null;
		Collection<Terms.Bucket> bucketsList = null;


		SimpleValue usageBucket = null;
		String termInstanceNm = null;
		log.debug("## histogram bucket Hits Size["+histogramBuckets.size()+"]");

		try{
			for (Histogram.Bucket bucket : histogramBuckets) {											// 기간 통계 voList에 담음.

				usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

				terms = bucket.getAggregations().get("term_agg_result");
				bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

				date = transFormat.parse(bucket.getKeyAsString());    // dateType의 timestamp를 longType으로 셋팅.

				for(Terms.Bucket termsBucket : bucketsList){			// 결과값 Vo 셋팅.
					aggregatedVo = new OstInstanceAggregatedVo();

					termInstanceNm = termsBucket.getKeyAsString();
					usageBucket = (SimpleValue) termsBucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

					aggregatedVo.setTimestamp(date.getTime());
					aggregatedVo.setInstanceName(termInstanceNm);

					if(metricType.equalsIgnoreCase("cores")){
						aggregatedVo.setCpuUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
					}else if(metricType.equalsIgnoreCase("memory")){
						aggregatedVo.setMemoryUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
					}

					voList.add(aggregatedVo);
				}
			}

			// 추출한 voList를 Top5 인스턴스 별로 times, values, series 에 담기. 이때 정렬은 따로안하고 애초에 전달받은 인스턴스가 Top 5 순서대로 전달받아야함.
			for(String tmpInstanceNm : instanceTopList){
				timeArr = new ArrayList<>();
				valueArr = new ArrayList<>();
				for(OstInstanceAggregatedVo tmpVo : voList ){
					if(tmpInstanceNm.equalsIgnoreCase(tmpVo.getInstanceName())){
						timeArr.add(tmpVo.getTimestamp());
						if(metricType.equalsIgnoreCase("cores")){
							valueArr.add(tmpVo.getCpuUsagePercent());
						}else if(metricType.equalsIgnoreCase("memory")){
							valueArr.add(tmpVo.getMemoryUsagePercent());
						}
					}
				}
				if(valueArr.size() != 0 && timeArr.size() != 0){				// 위 for문에서 인스턴스 네임이 존재할때만 추가.
					timesList.add(timeArr);
					valuesList.add(valueArr);
					seriesArr.add(tmpInstanceNm);
				}

			}

			graphAggregatedVo.setTimes(timesList);
			graphAggregatedVo.setValues(valuesList);
			graphAggregatedVo.setSeries(seriesArr);

		}catch (Exception e){
			e.printStackTrace();
			log.error("## Elasticsearch에서 Instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
			return null;
		}

		return graphAggregatedVo;
	}



	/**
	 * ES에서 Openstack관련 모든 메트릭 리스트 범위(gte&lte) 조회
	 * @param idxName
	 * @param gte
	 * @param lte
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public Page<OpenstackMetric> retrieveAllScopeIndexMetricsRange(String idxName, String gte, String lte, int size) {
		index.setIndex(idxName);
		log.debug("## Y_TEST index name : "+idxName);
		log.debug("## Y_TEST size : "+size);
		log.debug("## Y_TEST gte : "+gte);
		log.debug("## Y_TEST lte : "+lte);

		return openstackRepository.findByIndexNameAndRange(idxName, gte, lte, PageRequest.of(0, size));
	}


	/**
	 * ES에서 Openstack관련 모든 메트릭 리스트 조회
	 * @param idxName
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public Page<OpenstackMetric> retrieveAllScopeIndexMetrics(String idxName, int size) {
		index.setIndex(idxName);
		log.debug("## Y_TEST index name : "+idxName);
		log.debug("## Y_TEST size : "+size);

		return openstackRepository.findByAllScopeIndexName(idxName, PageRequest.of(0, size));
	}


	/**
	 * ES에서 Project 리스트 조회
	 * @param idxName
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public Page<ProjectsMetric> retrieveProjectIndexMetricsTest(String idxName, int size) {
		index.setIndex(idxName);
		log.debug("## Y_TEST index name : "+idxName);
		log.debug("## Y_TEST size : "+size);

		return openstackProjectsRepository.findByProjectIndexNameTest(idxName, PageRequest.of(0, size));
	}



	/**
	 * ES에서 Hypervisor 조회
	 * @param idxName
	 * @param size
	 * @return
	 */
	@Override
	@Transactional
	public Page<HypervisorMetric> retrieveHypervisorIndexMetricsTest(String idxName, int size) {
		index.setIndex(idxName);
		log.debug("## Y_TEST index name : "+idxName);
		log.debug("## Y_TEST size : "+size);

		return openstackHypervisorRepository.findByIndexNameTest(idxName, PageRequest.of(0, size));
	}


	@Override
	@Transactional
	public Page<VmMetric> retrieveHostMetrics(String idxName, String hostName) {
		index.setIndex(idxName);
		return vmRepository.findByIndexNameAndHostname(idxName, hostName, PageRequest.of(0, 20));
	}

	/**
	 * Index & Host 명으로 조회 테스트 중. (2021-04-02)
	 * @param idxName
	 * @param hostName
	 * @return
	 */
	@Override
	@Transactional
	public Page<VmMetric> retrieveIndexAndHostMetricsService(String idxName, String hostName) {
		index.setIndex(idxName);
		return vmRepository.findByIndexNameAndHostnames(idxName, PageRequest.of(0, 20));
	}

	@Override
	@Transactional
	public Page<VmMetric> retrieveIndexAllMetrics(String idxName) {
		index.setIndex(idxName);
		return vmRepository.findByIndexNameAll(idxName, PageRequest.of(0, 20));
	}

	@Override
	@Transactional
	public Page<VmMetric> retrieveIndexMetrics(String idxName, int size) {
		index.setIndex(idxName);
		return vmRepository.findByIndexName(idxName, PageRequest.of(0, size));
	}

	@Override
	@Transactional
	public Page<VmMetric> retrieveMetrics(String name, String metricSet, int size) {
		return vmRepository.findByNameAndMetricSet(name, metricSet,PageRequest.of(0, size));
	}

	@Override
	@Transactional
	public Page<VmMetric> retrieveMetrics(String host, String date, String name, String metricSet, int size) {
		index.setIndex(host+date);
		return vmRepository.findByNameAndMetricSet(name, metricSet,PageRequest.of(0, size));
	}

	@Override
	@Transactional
	public List<Page<VmMetric>> multiRetrieveMetrics(String host, String startDate, String endDate, String name, String metricSet, int size) throws ParseException {
		long period = dateUtil.calPeroid(startDate,endDate);
		List<Page<VmMetric>> pages = new ArrayList<Page<VmMetric>>();

		for (int i = 0;i <= period;i++) {
			String date = dateUtil.calDate(startDate,i);
			index.setIndex(host+date);
			Page<VmMetric> metrics = vmRepository.findByNameAndMetricSet(name, metricSet,PageRequest.of(0, size));
			pages.add(metrics);
		}
		return pages;
	}


	@Override
	@Transactional
	public Page<VmMetric> retrieveMetricsAndCpuUsageRange(String host, String date, String name, String timeGte, String timeLte, String cpuGte, String cpuLte, int size) {
		index.setIndex(host+date);
		return vmRepository.findByNameAndMetricSetAndCpuUsageRange(name, timeGte, timeLte, cpuGte, cpuLte, PageRequest.of(0, size));
	}

	@Override
	@Transactional
	public Page<VmMetric> retrieveMetricsAndRange(String host, String date, String name, String metricSet, String gte, String lte, int size) {
		index.setIndex(host+date);
		return vmRepository.findByNameAndMetricSetAndRange(name, metricSet, gte, lte, PageRequest.of(0, size));
	}

	@Override
	public Page<VmMetric> retriveMetricsByTerms(String host, String date, String metricSet, int size) {
		index.setIndex(host+date);

		String[] metrics = metricSet.split("-");
		String param ="";

		for (int i = 0;i < metrics.length; i++) {
			param += "\"" + metrics[i] + "\"";

			if(i != (metrics.length-1)) {
				param += ",";
			}
		}
		return vmRepository.findByMetricSet(param, PageRequest.of(0, size));
	}

	/**
	 * ES에 범위 평균 통계 쿼리 날려서 응답받음.
	 * @param esClient
	 * @param query
	 * @param metricType
	 * @param bucketsPathsMap
	 * @param script
	 * @param sorts
	 * @param limit
	 * @return
	 * @throws IOException
	 */
	public SearchResponse getAggQueryResponse(RestHighLevelClient esClient, QueryBuilder query, String metricType, Map<String, String> bucketsPathsMap, Script script, List<FieldSortBuilder> sorts, int limit) throws IOException {


		SearchResponse response = null;

		if(limit == 0){
			response =
					esClient.search(new SearchRequest()
							.source(new SearchSourceBuilder()
									.query(query)
									.aggregation(
											AggregationBuilders.terms("name_term_result").field("project_detail_info.project_name.keyword")
													.subAggregation(
															AggregationBuilders.avg("total_quota_avg")
																	.field("project_detail_info.project_quota_set."+metricType+"_quota")
													).subAggregation(
													AggregationBuilders.avg("total_usage_avg")
															.field("project_detail_info.project_usage."+metricType+"_usage")
											).subAggregation(
													PipelineAggregatorBuilders.bucketScript("tmp_pct",bucketsPathsMap,script)
											).subAggregation(
													PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
											))
									.size(0)
									.trackTotalHits(true)
									.sort(SortBuilders.scoreSort())
							), RequestOptions.DEFAULT);
		}else{
			response =
					esClient.search(new SearchRequest()
							.source(new SearchSourceBuilder()
									.query(query)
									.aggregation(
											AggregationBuilders.terms("name_term_result").field("project_detail_info.project_name.keyword").size(limit)
													.subAggregation(
															AggregationBuilders.avg("total_quota_avg")
																	.field("project_detail_info.project_quota_set."+metricType+"_quota")
													).subAggregation(
													AggregationBuilders.avg("total_usage_avg")
															.field("project_detail_info.project_usage."+metricType+"_usage")
											).subAggregation(
													PipelineAggregatorBuilders.bucketScript("tmp_pct",bucketsPathsMap,script)
											).subAggregation(
													PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
											))
									.size(0)
									.trackTotalHits(true)
									.sort(SortBuilders.scoreSort())
							), RequestOptions.DEFAULT);
		}



		return  response;
	}

}
