//package com.okestro.symphony.dashboard.api.openstack.dashboard.svc.impl;
//
//import com.okestro.symphony.dashboard.api.openstack.dashboard.model.*;
//import com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity.*;
//import com.okestro.symphony.dashboard.api.openstack.dashboard.repo.*;
//import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;
//import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostEntity;
//import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.repo.HostRepository;
//import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.svc.impl.HypervisorServiceImpl;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.*;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectQuotaEntity;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.repo.ProjectQuotaRepository;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.repo.ProjectRepository;
//import com.okestro.symphony.dashboard.api.openstack.keystone.project.svc.impl.ProjectServiceImpl;
//import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
//import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
//import com.okestro.symphony.dashboard.api.openstack.dashboard.svc.DashboardService;
//import lombok.extern.slf4j.Slf4j;
//import org.openstack4j.api.OSClient;
//import org.openstack4j.model.compute.Server;
//import org.openstack4j.model.identity.v3.Project;
//import org.openstack4j.model.network.NetQuota;
//import org.openstack4j.model.storage.block.BlockQuotaSet;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.net.ConnectException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Slf4j
//@Service
//public class DashboardServiceImpl implements DashboardService {
//
//	@Autowired
//	OpenStackConnectionService openStackConnectionService;
//
//	@Autowired
//	WebsocketService websocketService;
//
//	@Autowired
//	HypervisorServiceImpl hypervisorService;
//
//	@Autowired
//	ProjectServiceImpl projectService;
//
////	@Autowired
////	HostRepository hostRepository;
////
////	@Autowired
////	ProjectRepository projectRepository;
////
////	@Autowired
////	ProjectQuotaRepository projectQuotaRepository;
////
////	@Autowired
////	VVmLastedRepository vVmLastedRepository;
////
////	@Autowired
////	VProjectLastedRepository vProjectLastedRepository;
////
////	@Autowired
////	VProjectQuotaLastedRepository vProjectQuotaLastedRepository;
////
////	@Autowired
////	VHostLastedRepository vHostLastedRepository;
////
////	@Autowired
////	VHostAggregateLastedRepository vHostAggregateLastedRepository;
////
////	@Autowired
////	VHostAggregateHostLastedRepository vHostAggregateHostLastedRepository;
//
//	List<VProjectLastedEntity> vProjectLastedEntityList;
//
//	List<VProjectQuotaLastedEntity> vProjectQuotaLastedEntityList;
//
//	List<VHostLastedEntity> vHostLastedEntityList;
//
//	List<VHostAggregateLastedEntity> vHostAggregateLastedEntityList;
//
//	List<VHostAggregateHostLastedEntity> vHostAggregateHostLastedEntityList;
//
//	List<VVmLastedEntity> vVmLastedEntityList;
//
//
//	@Value("${config.openstack.endpoint}")
//	String endpoint = null;
//
//	@Value("${config.openstack.domain}")
//	String domain = null;
//
//	@Value("${config.openstack.user}")
//	String user = null;
//
//	@Value("${config.openstack.passwd}")
//	String password = null;
//
//
//	/**
//	 * retrieve hostAggregateHost
//	 * @return VHostAggregateHostLastedVo
//	 */
//	@Override
//	public List<VHostAggregateHostLastedVo> retrieveLastedHostsAggregatesHosts(){
//		List<VHostAggregateHostLastedVo> vHostAggregateHostLastedVoList = new ArrayList<>();
//		VHostAggregateHostLastedVo vHostAggregateHostLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////		vHostAggregateHostLastedEntityList = vHostAggregateHostLastedRepository.findAll();
////
////		for(VHostAggregateHostLastedEntity tmpEntity : vHostAggregateHostLastedEntityList){
////			vHostAggregateHostLastedVo = new VHostAggregateHostLastedVo();
////
////			vHostAggregateHostLastedVo.setId(tmpEntity.getHostId());
////			vHostAggregateHostLastedVo.setName(tmpEntity.getHostName());
////			vHostAggregateHostLastedVo.setHostAggregateId(tmpEntity.getHostAggregateId());
////			vHostAggregateHostLastedVo.setProviderId(tmpEntity.getProviderId());
////			vHostAggregateHostLastedVo.setProviderNm(tmpEntity.getProviderName());
////			vHostAggregateHostLastedVo.setAvZoneNm(tmpEntity.getAzName());
////			vHostAggregateHostLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////
////
////			vHostAggregateHostLastedVoList.add(vHostAggregateHostLastedVo);
////		}
//
//		return vHostAggregateHostLastedVoList;
//	}
//
//	/**
//	 * retrieve hostAggregates
//	 * @return VHostAggregateLastedVo
//	 */
//	@Override
//	public List<VHostAggregateLastedVo> retrieveLastedHostsAggregates(){
//		List<VHostAggregateLastedVo> vHostAggregateLastedVoList = new ArrayList<>();
//		VHostAggregateLastedVo vHostAggregateLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////		vHostAggregateLastedEntityList = vHostAggregateLastedRepository.findAll();
////
////		for(VHostAggregateLastedEntity tmpEntity : vHostAggregateLastedEntityList){
////			vHostAggregateLastedVo = new VHostAggregateLastedVo();
////
////			vHostAggregateLastedVo.setId(tmpEntity.getHostAggregateId());
////			vHostAggregateLastedVo.setHostAggregateNm(tmpEntity.getHostAggregateName());
////			vHostAggregateLastedVo.setProviderId(tmpEntity.getProviderId());
////			vHostAggregateLastedVo.setProviderNm(tmpEntity.getProviderName());
////			vHostAggregateLastedVo.setAvZoneNm(tmpEntity.getAzName());
////			vHostAggregateLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////
////
////			vHostAggregateLastedVoList.add(vHostAggregateLastedVo);
////		}
//
//		return vHostAggregateLastedVoList;
//	}
//
//	/**
//	 * retrieve Vms
//	 * @return VVmLastedVo
//	 */
//	@Override
//	public List<VVmLastedVo> retrieveLastedVms(){
//		List<VVmLastedVo> vVmLastedVoList = new ArrayList<>();
//		VVmLastedVo vVmLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////		vVmLastedEntityList = vVmLastedRepository.findAll();
////
////		for(VVmLastedEntity tmpEntity : vVmLastedEntityList){
////			vVmLastedVo = new VVmLastedVo();
////
////			vVmLastedVo.setId(tmpEntity.getVmId());
////			vVmLastedVo.setName(tmpEntity.getVmName());
////			vVmLastedVo.setProjectId(tmpEntity.getProjectId());
////			vVmLastedVo.setProjectName(tmpEntity.getProjectName());
////			vVmLastedVo.setHostId(tmpEntity.getHostId());
////			vVmLastedVo.setHostName(tmpEntity.getHostName());
////			vVmLastedVo.setProviderId(tmpEntity.getProviderId());
////			vVmLastedVo.setProviderName(tmpEntity.getProviderName());
////			vVmLastedVo.setCreateDt(transFormat.format(tmpEntity.getCreatDt()));
////			vVmLastedVo.setAvZoneNm(tmpEntity.getAzName());
////			vVmLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////			vVmLastedVo.setPowerStatus(tmpEntity.getPowerState());
////			vVmLastedVo.setVmStatus(tmpEntity.getVmState());
////			vVmLastedVo.setCpu(tmpEntity.getCpu());
////			vVmLastedVo.setRam(tmpEntity.getRam());
////			vVmLastedVo.setDisk(tmpEntity.getDisk());
////
////
////
////			vVmLastedVoList.add(vVmLastedVo);
////		}
//
//		return vVmLastedVoList;
//	}
//
//
//	/**
//	 * retrieve Hosts
//	 * @return VHostLastedVo
//	 */
//	@Override
//	public List<VHostLastedVo> retrieveLastedHosts(){
//		List<VHostLastedVo> vHostLastedVoList = new ArrayList<>();
//		VHostLastedVo vHostLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////		vHostLastedEntityList = vHostLastedRepository.findAll();
////
////		for(VHostLastedEntity tmpEntity : vHostLastedEntityList){
////			vHostLastedVo = new VHostLastedVo();
////
////			vHostLastedVo.setId(tmpEntity.getHostId());
////			vHostLastedVo.setName(tmpEntity.getHostName());
////			vHostLastedVo.setVirtualCPU(tmpEntity.getVcpuTotal());
////			vHostLastedVo.setVirtualUsedCPU(tmpEntity.getVcpuUsage());
////			vHostLastedVo.setLocalMemory(tmpEntity.getRamTotal());
////			vHostLastedVo.setLocalMemoryUsed(tmpEntity.getRamUsage());
////			vHostLastedVo.setLocalDisk(tmpEntity.getLocalStorageTotal());
////			vHostLastedVo.setLocalDiskUsed(tmpEntity.getLocalStorageUsage());
////			vHostLastedVo.setProviderId(tmpEntity.getProviderId());
////			vHostLastedVo.setProviderNm(tmpEntity.getProviderName());
////			vHostLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////			vHostLastedVo.setRunningVM(tmpEntity.getVmNum());
////			vHostLastedVo.setState(tmpEntity.getState());
////
////
////			vHostLastedVoList.add(vHostLastedVo);
////		}
//
//
//		return vHostLastedVoList;
//	}
//
//	/**
//	 * retrieve ProjectQuota
//	 * @return VProjectQuotaLastedVo
//	 */
//	@Override
//	public List<VProjectQuotaLastedVo> retrieveLastedProjectQuota(){
//		List<VProjectQuotaLastedVo> vProjectQuotaLastedVoList = new ArrayList<>();
//		VProjectQuotaLastedVo vProjectQuotaLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
////		vProjectQuotaLastedEntityList = vProjectQuotaLastedRepository.findAll();
////
////		for(VProjectQuotaLastedEntity tmpEntity : vProjectQuotaLastedEntityList){
////			vProjectQuotaLastedVo = new VProjectQuotaLastedVo();
////
////			vProjectQuotaLastedVo.setId(tmpEntity.getProjectId());
////			vProjectQuotaLastedVo.setName(tmpEntity.getProjectName());
////			vProjectQuotaLastedVo.setProviderId(tmpEntity.getProviderId());
////			vProjectQuotaLastedVo.setProviderNm(tmpEntity.getProviderName());
////			vProjectQuotaLastedVo.setInstanceQuota(tmpEntity.getInstanceQuota());
////			vProjectQuotaLastedVo.setInstanceUsed(tmpEntity.getInstanceUsage());
////			vProjectQuotaLastedVo.setCoreQuota(tmpEntity.getCpuQuota());
////			vProjectQuotaLastedVo.setCoreUsed(tmpEntity.getCpuUsage());
////			vProjectQuotaLastedVo.setRamQuota(tmpEntity.getRamQuota());
////			vProjectQuotaLastedVo.setRamUsed(tmpEntity.getRamUsage());
////			vProjectQuotaLastedVo.setVolumeQuota(tmpEntity.getVolumeQuota());
////			vProjectQuotaLastedVo.setVolumeUsed(tmpEntity.getVolumeUsage());
////			vProjectQuotaLastedVo.setSnapshotQuota(tmpEntity.getSnapshotQuota());
////			vProjectQuotaLastedVo.setSnapshotUsed(tmpEntity.getSnapshotUsage());
////			vProjectQuotaLastedVo.setNetworkQuota(tmpEntity.getNetworkQuota());
////			vProjectQuotaLastedVo.setNetworkUsed(tmpEntity.getNetworkUsage());
////			vProjectQuotaLastedVo.setFloatingIpQuota(tmpEntity.getFloatingIpQuota());
////			vProjectQuotaLastedVo.setFloatingIpUsed(tmpEntity.getFloatingIpUsage());
////			vProjectQuotaLastedVo.setSecurityGroupQuota(tmpEntity.getSecurityGroupQuota());
////			vProjectQuotaLastedVo.setSecurityGroupUsed(tmpEntity.getSecurityGroupUsage());
////			vProjectQuotaLastedVo.setSecurityGroupRuleQuota(tmpEntity.getSecurityRuleQuota());
////			vProjectQuotaLastedVo.setPortQuota(tmpEntity.getPortQuota());
////			vProjectQuotaLastedVo.setPortUsed(tmpEntity.getPortUsage());
////			vProjectQuotaLastedVo.setRouterQuota(tmpEntity.getRouterQuota());
////			vProjectQuotaLastedVo.setRouterUsed(tmpEntity.getRouterUsage());
////			vProjectQuotaLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////
////
////			vProjectQuotaLastedVoList.add(vProjectQuotaLastedVo);
////		}
//
//		return vProjectQuotaLastedVoList;
//	}
//
//
//	/**
//	 * retrieve Project
//	 * @return VProjectLastedVo
//	 */
//	@Override
//	public List<VProjectLastedVo> retrieveLastedProject(){
//
//		List<VProjectLastedVo> vProjectLastedVoList = new ArrayList<>();
//		VProjectLastedVo vProjectLastedVo;
//		SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//
//
//
////		vProjectLastedEntityList = vProjectLastedRepository.findAll();
////
////		for(VProjectLastedEntity tmpEntity : vProjectLastedEntityList){
////			vProjectLastedVo = new VProjectLastedVo();
////			vProjectLastedVo.setId(tmpEntity.getProjectId());
////			vProjectLastedVo.setName(tmpEntity.getProjectName());
////			vProjectLastedVo.setProviderId(tmpEntity.getProviderId());
////			vProjectLastedVo.setProviderNm(tmpEntity.getProviderName());
////			vProjectLastedVo.setCollectDt(transFormat.format(tmpEntity.getCollectDt()));
////			vProjectLastedVo.setState(tmpEntity.getState());
////
////			vProjectLastedVoList.add(vProjectLastedVo);
////		}
//
//		return vProjectLastedVoList;
//	}
//
//
//	/**
//	 * retrieve openstackAPI 호출 테스트
//	 * @return SummaryVo
//	 */
//	@Override
//	public SummaryVo retrieveSummary() {
//		SummaryVo summary = new SummaryVo();
//		HostVo hostVo = new HostVo();
//		QuotaProjectVo projectQuotaVo = new QuotaProjectVo();
//		SummaryVo.ProjectMainQuota projectMainQuota = new SummaryVo.ProjectMainQuota();
//		List<SummaryVo.ProjectMainQuota> projectMainQuotaList = new ArrayList<>();
//		List<HostVo> hostVoList = new ArrayList<>();
//		List<QuotaProjectVo> projectQuotaVoList = new ArrayList<>();
//		List<HostEntity> hostEntityList = new ArrayList<>();
//		List<ProjectQuotaEntity> projectQuotaEntityArrayList = new ArrayList<>();
//		List<? extends Project> projectList = new ArrayList<>();
//		List<ProjectQuotaEntity> projectQuotaEntityList = new ArrayList<>();
//		ProjectQuotaEntity projectQuotaEntity = null;
//
//		String projectId = null;
//
//		long startTime = System.currentTimeMillis();
//
//		try {
//			int vCpus = 0, ram = 0, disk = 0, vmCnt = 0, runningVmCnt = 0;
//			int usageVcpus = 0, usageRam = 0, usageDisk = 0;
//
//
//			// connect to openstack
//			OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);
//			OSClient.OSClientV3 osProjectClient = null;
//
//			//임시고 실제로는 인사이트에 프로젝트단위로 검색함. QuotaProject를 가져와야함.
//			hostVoList = hypervisorService.listAllHypervisor(false);
//			projectQuotaVoList = projectService.getAllProjectQuotaList(false);
//
//			// 하나의 openstack이 관리하는 각 호스트 마다 vCpu,ram,disk,vm 계산.
//			for(HostVo tmpHostVo : hostVoList){
//				// vCpu 사용량 계산.
// 				vCpus += tmpHostVo.getVcpus();
//				usageVcpus += tmpHostVo.getVcpusUsed();
//				// ram 사용량 계산
//				ram += tmpHostVo.getMemoryMb();
//				usageRam += tmpHostVo.getMemoryMbUsed();
//				// disk 사용량 계산
//				disk += tmpHostVo.getFreeDiskGb();
//				usageDisk += tmpHostVo.getLocalGbUsed();
//				// 해당 host에 해당하는 vm 갯수 계산.
//				runningVmCnt += tmpHostVo.getRunningVms();
//
//				log.debug("## Y_TEST host("+tmpHostVo.getName()+") vCpu ["+tmpHostVo.getVcpus()+"] vCpu Usage["+tmpHostVo.getVcpusUsed()+"]");
//				log.debug("## Y_TEST host("+tmpHostVo.getName()+") ram ["+tmpHostVo.getMemoryMb()+"] ram Usage["+tmpHostVo.getMemoryMbUsed()+"]");
//				log.debug("## Y_TEST host("+tmpHostVo.getName()+") Vm 갯수["+tmpHostVo.getRunningVms()+"]");
//			}
//			log.debug("## Y_TEST 사용가능한 vCpu["+((vCpus * 16)- usageVcpus)+"]");
//			log.debug("## Y_TEST 사용가능한 ram["+((ram * 1.5)- usageRam)+"]");
//			log.debug("## Y_TEST 사용가능한 disk["+(disk - usageDisk)+"]");
//
//			log.debug("## Y_TEST 총 Vm 갯수["+runningVmCnt+"]");
//
//
//			projectList = osClient.identity().projects().list();
//			log.debug("## Y_TEST 프로젝트 총 사이즈 ["+projectList.size()+"]");
//
//			// project List 를 for문 돌려서 Vm size 측정.
//			for (Project tmpProject : projectList){
//				projectMainQuota.setProjectId(tmpProject.getId());
//				projectMainQuota.setProjectName(tmpProject.getName());
//
//				projectMainQuota.setCpu(projectQuotaVo.getComputeQuota().getCoreQuota());
//				projectMainQuota.setUsageCpu(projectQuotaVo.getComputeUsage().getCoreUsed());
//				projectMainQuota.setRam(projectQuotaVo.getComputeQuota().getRamQuota());
//				projectMainQuota.setUsageRam(projectQuotaVo.getComputeUsage().getRamUsed());
//				projectMainQuota.setInstance(projectQuotaVo.getComputeQuota().getInstanceQuota());
//				projectMainQuota.setInstanceUsage(projectQuotaVo.getComputeUsage().getInstanceUsed());
//
//
//				projectId = tmpProject.getId();
//				log.debug("## Y_TEST project ID ["+projectId+"]");
//				log.debug("## Y_TEST project Name ["+tmpProject.getName()+"]");
//
//				// 프로젝트 scope으로 API조회.
//				osProjectClient = openStackConnectionService.connect(endpoint,domain,tmpProject.getName(),user,password);
//				log.debug("## Y_TEST project vm Size ["+osProjectClient.compute().servers().list().size()+"]");
//
//				// 전원 상태가 Running이 아닌 애들도 다 카운트.
//				vmCnt += osProjectClient.compute().servers().list().size();
//			}
//
//
//			// get servers
//			List<? extends Server> servers = osClient.compute().servers().list();
//			log.debug("## Y_TEST project server List Size["+servers.size()+"]");
//			for (Server server : servers) {
//				vCpus = vCpus + server.getFlavor().getVcpus();
//				ram = ram + server.getFlavor().getRam();
//			}
//
//			log.debug("## Y_TEST vcpu["+vCpus+"]");
//			log.debug("## Y_TEST rem["+ram+"]");
//
//			// get compute quota
//			summary.setInstances(vmCnt);											// 인스턴스 총 개수
//			summary.setInstancesUsed(runningVmCnt);									// 동작 중인 인스턴스 개수
//			summary.setVcpus(vCpus);												// vCpu 할당량
//			summary.setVcpusUsed(usageVcpus);										// vCpu 사용량
//			summary.setRam(ram);													// ram 할당량
//			summary.setRamUsed(usageRam);											// ram 사용량수
//
//
//
//			// get network quota
//			NetQuota netQuota = osClient.networking().quotas().get(projectId);
//			summary.setIps(netQuota.getFloatingIP());
//
//			// get block storage quota
//			BlockQuotaSet blockQuotaSet = osClient.blockStorage().quotaSets().get(projectId);
//			summary.setVolumes(blockQuotaSet.getVolumes());
//			summary.setVolumesUsed(osClient.blockStorage().volumes().list().size());
//			summary.setGigabytes(blockQuotaSet.getGigabytes());
//			summary.setGigabytesUsed(osClient.blockStorage().getLimits().getAbsolute().getTotalGigabytesUsed());
//
//			//이걸 최근 데이터 1개, 1주일전 데이터 1개. set, DB조회해야함(repo등록해서)
//			Date now = new Date();
//
//			for(HostEntity tmpEntity : hostEntityList){
//				hostVo = new HostVo();
//
//				hostVo.setId(tmpEntity.getHostId());
//				hostVo.setLocalGb(tmpEntity.getLocalStorageTotal());
//				hostVo.setMemoryMb(tmpEntity.getRamTotal());
//				hostVo.setLocalGbUsed(tmpEntity.getLocalStorageUsage());
//				hostVo.setMemoryMbUsed(tmpEntity.getLocalStorageUsage());
//
//				hostVoList.add(hostVo);
//			}
//
//			for(ProjectQuotaEntity tmpEntity : projectQuotaEntityArrayList){
//				projectQuotaVo = new QuotaProjectVo();
//				QuotaComputeVo quotaComputeVo = new QuotaComputeVo();
//				QuotaNetworkVo quotaNetworkVo = new QuotaNetworkVo();
//				QuotaVolumnVo quotaVolumnVo = new QuotaVolumnVo();
//				UsageComputeVo usageComputeVo = new UsageComputeVo();
//				UsageNetworkVo usageNetworkVo = new UsageNetworkVo();
//				UsageVolumnVo usageVolumnVo = new UsageVolumnVo();
//
//				projectQuotaVo.setProjectId(tmpEntity.getProjectId());
//
//				// ComputeVo Set!
//				quotaComputeVo.setCoreQuota(tmpEntity.getCpuQuota());
//				usageComputeVo.setCoreUsed(tmpEntity.getCpuUsage());
//				quotaComputeVo.setInstanceQuota(tmpEntity.getInstanceQuota());
//				usageComputeVo.setInstanceUsed(tmpEntity.getInstanceUsage());
//				quotaComputeVo.setRamQuota(tmpEntity.getRamQuota());
//				usageComputeVo.setRamUsed((tmpEntity.getRamUsage()));
//
//				projectQuotaVo.setComputeQuota(quotaComputeVo);
//				projectQuotaVo.setComputeUsage(usageComputeVo);
//
//				// NetworkVo Set!
//				quotaNetworkVo.setNetworkQuota(tmpEntity.getNetworkQuota());
//				usageNetworkVo.setNetworkUsed(tmpEntity.getNetworkUsage());
//				quotaNetworkVo.setRouterQuota(tmpEntity.getRouterQuota());
//				usageNetworkVo.setRouterUsed(tmpEntity.getRouterUsage());
//				quotaNetworkVo.setFloatingIpQuota(tmpEntity.getFloatingIpQuota());
//				usageNetworkVo.setFloatingIpUsed(tmpEntity.getFloatingIpUsage());
//				quotaNetworkVo.setPortQuota(tmpEntity.getPortQuota());
//				usageNetworkVo.setPortUsed(tmpEntity.getPortUsage());
//				quotaNetworkVo.setSecurityGroupQuota(tmpEntity.getSecurityGroupQuota());
//				usageNetworkVo.setSecurityGroupUsed(tmpEntity.getSecurityGroupUsage());
//				quotaNetworkVo.setSecurityGroupRuleQuota(tmpEntity.getSecurityRuleQuota());
//				usageNetworkVo.setSecurityGroupRuleUsed(tmpEntity.getSecurityRuleUsage());
//
//				projectQuotaVo.setNetworkQuota(quotaNetworkVo);
//				projectQuotaVo.setNetworkUsage(usageNetworkVo);
//
//				// VolumeVo Set!
//				quotaVolumnVo.setVolumeQuota(tmpEntity.getVolumeQuota());
//				usageVolumnVo.setVolumeUsed(tmpEntity.getVolumeUsage());
//				quotaVolumnVo.setSnapshotQuota(tmpEntity.getSnapshotQuota());
//				usageVolumnVo.setSnapshotUsed(tmpEntity.getSnapshotUsage());
//
//				projectQuotaVo.setVolumeQuota(quotaVolumnVo);
//				projectQuotaVo.setVolumeUsage(usageVolumnVo);
//
//
//
//				projectQuotaVoList.add(projectQuotaVo);
//			}
//
//			summary.setRctHostVoList(hostVoList);
//
//			summary.setRctProjectQuotaList(projectQuotaVoList);
//
//			long endTime = System.currentTimeMillis();
//			log.debug("## Y_TEST SummaryVo toString["+summary.toString()+"]");
//			log.debug("## Y_TEST 경과시간 ["+(endTime-startTime)/1000+"]초");
//
//		} catch (ConnectException e)  {
//			// log
//			log.error(e.getMessage());
//		}
//
//		return summary;
//	}
//
//
//
//	/**
//	 * connect to openstack
//	 */
//	private OSClient.OSClientV3 connect(String projectName, String userId) throws ConnectException {
//		return openStackConnectionService.connect(projectName, userId);
//	}
//}