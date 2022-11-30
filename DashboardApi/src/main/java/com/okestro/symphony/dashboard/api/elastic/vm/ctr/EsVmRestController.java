/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-15 10:40:10
 */


package com.okestro.symphony.dashboard.api.elastic.vm.ctr;

import com.okestro.symphony.dashboard.api.elastic.vm.model.ElasticIndex;
import com.okestro.symphony.dashboard.api.elastic.vm.model.OpenstackMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo.*;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ComputeMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.HypervisorMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ProjectDetailMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ProjectInfoVo;
import com.okestro.symphony.dashboard.api.elastic.vm.svc.VmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@Slf4j
@CrossOrigin("*")
@Api(value = "elasticSearchMetricCollectData", description = "ElasticSearch Metric 수집 데이터 조회 리스트")
@RequestMapping("/es")
@RestController
public class EsVmRestController {

	@Autowired
	VmService vmService;

	@Autowired
	ElasticIndex index;

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;




	//================================SAVE===============================
	/**
	 * Hypervisor 인덱스 없으면 생성 & 데이터 업데이트
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/hypervisor
	 * 관련인덱스 : [ost-metric-hypervisors-yyyy-mm-dd]
	 * @return
	 */
	@ApiOperation(value = "ES에 Hypervisor 정보 삽입", notes = "[ost-metric-hypervisors-yyyy-mm-dd] 인덱스 없으면 생성 & 데이터 업데이트")
	@RequestMapping(value = {"/index/hypervisor"}, method = RequestMethod.POST)
	public boolean updateHypervisorMetricIndex(@RequestBody List<HypervisorMetricVo> hypervisorMetricVoList) {
		for(HypervisorMetricVo tmpVo : hypervisorMetricVoList){
			log.debug("Vo received. Vo Id["+tmpVo.getId()+"]");
		}

		return vmService.updateHypervisorIndex(hypervisorMetricVoList);
	}

	/**
	 * Project 인덱스 없으면 생성 & 데이터 업데이트
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/projects
	 * 관련인덱스 : [ost-metric-projects-yyyy-mm-dd]
	 * @return
	 */
	@ApiOperation(value = "ES에 Project 정보 삽입", notes = "[ost-metric-projects-yyyy-mm-dd] 인덱스 없으면 생성 & 데이터 업데이트.  *참고사항 : 현재는 projectDetail 인덱스 하나로 다처리하고 있기때문에 호출하지 않고있음.")
	@RequestMapping(value = {"/index/projects"}, method = RequestMethod.POST)
	public boolean updateProjectMetricIndex(@RequestBody List<ProjectInfoVo> projectInfoVoList) {

		for(ProjectInfoVo tmpVo : projectInfoVoList){
			log.debug("Vo received. Vo Name["+tmpVo.getName()+"]");
		}

		return vmService.updateProjectIndex(projectInfoVoList);
	}

	/**
	 * ProjectDetail 인덱스 없으면 생성 & 데이터 업데이트
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/projectDetail
	 * 관련인덱스 : [ost-metric-projects-detail-yyyy-mm-dd]
	 * @return
	 */
	@ApiOperation(value = "ES에 ProjectDetail 정보 삽입", notes = "[ost-metric-projects-detail-yyyy-mm-dd] 인덱스 없으면 생성 & 데이터 업데이트.")
	@RequestMapping(value = {"/index/projectDetail"}, method = RequestMethod.POST)
	public boolean updateProjectDetailMetricIndex(@RequestBody List<ProjectDetailMetricVo> projectDetailMetricVoList) {

		for(ProjectDetailMetricVo tmpVo : projectDetailMetricVoList){
			log.debug("Vo received. Project Name["+tmpVo.getProjectName()+"]");
			log.debug("####Project Domain name ["+tmpVo.getDomain()+"]");
			log.debug("####Project @timestamp ["+tmpVo.getTimestamp()+"]");
		}

		return vmService.updateProjectDetailIndex(projectDetailMetricVoList);
	}

	/**
	 * Compute 인덱스 없으면 생성 & 데이터 업데이트
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/compute
	 * 관련인덱스 : [ost-service-compute-yyyy-mm-dd]
	 * @return
	 */
	@ApiOperation(value = "ES에 Compute 정보 삽입", notes = "[ost-service-compute-yyyy-mm-dd] 인덱스 없으면 생성 & 데이터 업데이트  *호출시 참고사항 : body에 Vo셋팅안하고 넘겨주면 알아서 모든 프로젝트들의 인스턴스 사용량 집계해서 인덱스 생성&삽입.")
	@RequestMapping(value = {"/index/compute"}, method = RequestMethod.POST)
	public boolean updateProjectDetailMetricIndex(@RequestBody ComputeMetricVo computeMetricVo) {

		if(computeMetricVo != null){
			log.debug("Vo received. Compute.Nova Name["+computeMetricVo.getNovaName()+"]");
			log.debug("####Project @timestamp ["+computeMetricVo.getTimestamp()+"]");
		}

		return vmService.updateComputeIndex(computeMetricVo);
	}

	//================================SELECT===============================
	/**
	 * [Meta] unixTime 현재 기준 모든 Project들의 instance 사용/할당량 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/compute/metricType/조회할 인덱스 메트릭타입/dateType/{얼마나 전시간을 기준으로할지 단위 일,시간}/dateUnit/{값}
	 * url ex)  localhost:8081/symphony/dashboard/api/es/index/compute/metricType/cpu/dateType/hour/dateUnit/1
	 * 관련인덱스 : [ost-service-compute-yyyy-mm-dd]
	 * @param metricName
	 * @param size
	 * @return
	 */
	@ApiOperation(value = "ES에서 모든 프로젝트들의 cpu,instance 사용/할당량 집계 조회.", notes = "[Meta] unixTime 현재 기준 모든 Project들의 cpu,instance 사용/할당량 데이터 조회 " +
																					"*필수전제조건 : ES[ost-service-compute-yyyy-mm-dd] 인덱스 안에 데이터가 있어야함.")
	@GetMapping(value = {"/index/compute/metricType/{metricName}","/index/compute/metricType/{metricName}/dateType/{dateType}","/index/compute/metricType/{metricName}/dateType/{dateType}/dateUnit/{dateUnit}"})
	public OstComputeMetricVo retrieveIndexComputeMetricToRange(@PathVariable(value = "metricName") String metricName,
																@PathVariable(value = "dateType", required = false) String dateType,
																@PathVariable(value = "dateUnit", required = false) String dateUnit,
																@PathVariable(value = "size", required = false) Integer size) {


		// 기본 조회 사이즈 설정.
		if(size == null){
			size = 20;
		}

		if(dateType == null ){
			dateType = "hour";
			dateUnit = String.valueOf(1);
		}

		log.debug("## Y_TEST MetricType ["+metricName+"]");

		return vmService.retrieveComputeMetricToRange(metricName, dateType, dateUnit, size);
	}

	/**
	 * [Meta] unixTime 기준 gte,lte로 Project 최근 cpu,ram,instance,volume 데이터 조회
	 * 	  url ex)  localhost:8081/symphony/dashboard/api/es/index/top/proejct
	 * 	  관련인덱스 : [ost-metric-projects-detail-yyyy-mm-dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Meta] ES에서 Projecte cpu,ram,instance,volume 현재 사용량 집계 조회.", notes = "[Meta] unixTime 기준 gte,lte로 Project 최근 cpu,ram,instance,volume 사용량 데이터 조회")
	@PostMapping(value = {"/index/top/proejct"})
	public List<OstProjectMetricVo> retrieveIndexProjectMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {


		// 기본 조회 사이즈 설정.
		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");

		return vmService.retrieveProjectMetricToRange(batchQueryRequestVo);
	}

	/**
	 * [Aggregation] gte,lte로 ProjectDetail Index에서 Cpu,Ram,Instance 값 통계 데이터 조회 (지난 주 대비 프로젝트별 ram,core,instance 사용량 통계)
	 * 	 url ex)  localhost:8081/symphony/dashboard/api/es/agg/top/index/project"
	 * 	 관련인덱스 : [ost-metric-projects-detail-yyyy-mm-dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Agg] ES에서 ProjectDetail cpu,ram,instance 지난주 대비 사용량 통계 데이터 조회 (Top 8).", notes = "[Aggregation] gte,lte로 ProjectDetail Index에서 Cpu,Ram,Instance 값 통계 데이터 조회 (지난 주 대비 프로젝트별 ram,core,instance 사용량 통계)")
	@PostMapping(value = {"/agg/top/index/project"})
	public List<OstProjectAggregatedVo> retrieveAggregateProjectMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {

		// 기본 조회 사이즈 설정.

		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");

		return vmService.retrieveAggregateProjectMetricToRange(batchQueryRequestVo);
	}

	/**
	 * [Aggregation] gte,lte로 Hypervisor Index에서 Cpu,Ram 값 통계 데이터 조회 (ram,core,instance 사용량 통계 조회), 하이퍼바이저는 지난주대비가 아니기 때문에 범위조회.
	 *  url ex)  localhost:8081/symphony/dashboard/api/es/agg/top/index/hypervisor
	 *  관련인덱스 : [ost-metric-hypervisors-yyyy-mm-dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Agg] ES에서 Hypervisor cpu,ram,instance 사용량 통계 데이터 조회 (Top 8).", notes = "[Aggregation] Hypervisor Index에서 Cpu,Ram,Instance 값 통계 데이터 조회")
	@PostMapping(value = {"/agg/top/index/hypervisor"})
	public List<OstHypervisorAggregatedVo> retrieveAggregateHypervisorMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {

		// 기본 조회 사이즈 설정.
		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");

		return vmService.retrieveAggregateHypervisorMetricToRange(batchQueryRequestVo);
	}

	/**
	 * [Aggregation] Hypervisor Index에서 Cpu,Ram 값 통계 그래프 시계열 데이터 조회 (ram,core,instance 사용량 통계 조회),
	 *  url ex)  localhost:8081/symphony/dashboard/api/es/agg/graph/index/hypervisor/
	 *  관련인덱스 : [ost-metric-hypervisors-yyyy-mm-dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Agg] ES에서 Hypervisor cpu,ram 사용량 통계 그래프 시계열 데이터 조회 (Top 5).", notes = "[Aggregation] Hypervisor Index에서 Cpu,Ram,Instance 값 통계 그래프 시계열 데이터 조회")
	@PostMapping(value = {"/agg/graph/index/hypervisor/"})
	public OstHypervisorGraphAggregatedVo retrieveGraphAggregateHypervisorMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {

		// 기본 조회 사이즈 설정.
		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		if(batchQueryRequestVo.getTopList().size() == 0 || (batchQueryRequestVo.getTopList().size() == 1 && batchQueryRequestVo.getTopList().get(0) == "")){										// 테스트용 조회 인스턴스 설정.
			batchQueryRequestVo.setTopList(new ArrayList<>());
			batchQueryRequestVo.getTopList().add("contrabass01");
			batchQueryRequestVo.getTopList().add("contrabass02");
			batchQueryRequestVo.getTopList().add("contrabass03");
			batchQueryRequestVo.getTopList().add("contrabass04");
			batchQueryRequestVo.getTopList().add("contrabass05");
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");

		return vmService.retrieveGraphAggregateHypervisorMetricToRange(batchQueryRequestVo);
	}


	/**
	 * [Aggregation] gte,lte로 Instance Index에서 Cpu,Ram 값 통계 데이터 조회 (인스턴스별 ram,core 사용량 통계 Top N개)
	 * 	 url ex)  localhost:8081/symphony/dashboard/api/es/agg/top/index/instance/
	 * 	 관련인덱스 : [sym-metric-cpu-yyyy.mm.dd],[sym-metric-memory-yyyy.mm.dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Agg] ES에서 Instance cpu,ram 사용량 통계 데이터 조회 (Top N).", notes = "[Aggregation] Instance Index에서 Cpu,Ram 값 통계 데이터 조회 (인스턴스별 ram,core 사용량 통계 기본 Top 10, limit 설정 하면 Top N개)")
	@PostMapping(value = {"/agg/top/index/instance/"})
	public List<OstInstanceAggregatedVo> retrieveAggregateInstanceMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {

		// 기본 조회 사이즈 설정.
		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");

			return vmService.retrieveAggregateInstanceMetricToRange(batchQueryRequestVo);
	}


	/**
	 * [Aggregation] gte,lte로 Instance Index에서 Cpu,Ram 값 통계 그래프 시계열 데이터 조회 (인스턴스별 ram,core 사용량 그래프 통계 Top 5)
	 * 	 url ex)  localhost:8081/symphony/dashboard/api/es/agg/graph/index/instance/
	 * 	관련인덱스 : [sym-metric-cpu-yyyy.mm.dd],[sym-metric-memory-yyyy.mm.dd]
	 * @param batchQueryRequestVo
	 * @return
	 */
	@ApiOperation(value = "[Agg] ES에서 Instance cpu,ram 사용량 통계 그래프 시계열 데이터 조회 (Top 5).", notes = "[Aggregation] Instance Index에서 Cpu,Ram 값 통계 그래프 시계열 데이터 조회 (인스턴스별 ram,core 사용량 그래프 통계 Top 5)")
	@PostMapping(value = {"/agg/graph/index/instance/"})
	public OstInstanceGraphAggregatedVo retrieveGraphAggregateInstanceMetricToRange( @RequestBody BatchQueryRequestVo batchQueryRequestVo ) {

		// 기본 조회 사이즈 설정.
		if(batchQueryRequestVo.getDateType() == null || batchQueryRequestVo.getDateType() == "" ){
			batchQueryRequestVo.setDateType("hour");
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}else if(batchQueryRequestVo.getDateUnit() == null || batchQueryRequestVo.getDateUnit()  == "" ){
			batchQueryRequestVo.setDateUnit(String.valueOf(1));
		}

		if(batchQueryRequestVo.getTopList().size() == 0 || (batchQueryRequestVo.getTopList().size() == 1 && batchQueryRequestVo.getTopList().get(0) == "")){										// 테스트용 조회 인스턴스 설정.
			batchQueryRequestVo.setTopList(new ArrayList<>());
			batchQueryRequestVo.getTopList().add("bigdata-elasticsearch-master-1.novalocal");
			batchQueryRequestVo.getTopList().add("platform-consul-3.novalocal");
			batchQueryRequestVo.getTopList().add("platform-haproxy.novalocal");
			batchQueryRequestVo.getTopList().add("platform-ptlmail.novalocal");
			batchQueryRequestVo.getTopList().add("platform-beat-test.novalocal");
		}

		log.debug("## Y_TEST MetricType ["+batchQueryRequestVo.getMetricName()+"]");
		log.debug("## Y_TEST instanceTopListSize ["+batchQueryRequestVo.getTopList().size()+"]");


		return vmService.retrieveGraphAggregateInstanceMetricToRange(batchQueryRequestVo.getMetricName(), batchQueryRequestVo.getDateType(), batchQueryRequestVo.getDateUnit(), batchQueryRequestVo.getNumSeries(), batchQueryRequestVo.getTopList());
	}


	/**
	 * [Meta] metricType & gte,lte로 Hypervisor,Project,ProjectDetail 최근 데이터 조회 TEST
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/metricType/조회할 인덱스 메트릭타입/gte/{unix timestamp 시작시간}
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/metricType/projectDetail/gte/1620581947
	 * @param metricName
	 * @param gte
	 * @param lte
	 * @param size
	 * @return
	 */
//	@GetMapping(value = {"/metrics/index/metricType/{metricName}","/metrics/index/metricType/{metricName}/gte/{gte}","/metrics/index/metricType/{metricName}/gte/{gte}/lte/{lte}","/metrics/index/metricType/{metricName}/gte/{gte}/lte/{lte}/size/{size}"})
	public Page<OpenstackMetric> retrieveAllScopeIndexMetricTypeRange(@PathVariable(value = "metricName") String metricName,
																 	  @PathVariable(value = "gte", required = false) String gte,
																	  @PathVariable(value = "lte", required = false) String lte,
																	  @PathVariable(value = "size", required = false) Integer size) {
		// 기본 조회 사이즈 설정.
		if(size == null){
			size = 20;
		}
		if((gte == null && lte == null)){
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
//			cal.add(Calendar.DATE, -1);
			cal.add(Calendar.HOUR, -3);// 3 시간전 셋팅.

			gte = String.valueOf(cal.getTimeInMillis()); //  prints a Unix timestamp in milliseconds
			lte = String.valueOf(System.currentTimeMillis()); //  prints a Unix timestamp in milliseconds
		}else if((lte == null && gte != null)){
			lte = String.valueOf(System.currentTimeMillis());
		}
		else if((gte == null && lte != null)){
			Long timestamp = Long.parseLong(lte);
			Date date = new Date(timestamp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.HOUR,-3);

			gte = String.valueOf(cal.getTimeInMillis()); //  prints a Unix timestamp in milliseconds
		}
		log.debug("## Y_TEST MetricType ["+metricName+"]");

		return vmService.retrieveAllMetricTypeToRange(metricName, gte, lte, size);
	}


	/**
	 * Index 명 & gte,lte로 Hypervisor,Project,ProjectDetail 최근 데이터 조회 TEST
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/인덱스명/size/20/test
	 * @param idxName
	 * @param size
	 * @return
	 */
//	@GetMapping(value = {"/metrics/index/{idxName}/gte/{gte}","/metrics/index/{idxName}/gte/{gte}/lte/{lte}","/metrics/index/{idxName}/gte/{gte}/lte/{lte}/size/{size}/allTest"})
//	public Page<OpenstackMetric> retrieveAllScopeIndexMetricsRange(@PathVariable(value = "idxName") String idxName,
//																   @PathVariable(value = "gte") String gte,
//																   @PathVariable(value = "lte", required = false) String lte,
//																   @PathVariable(value = "size", required = false) Integer size) {
//		SimpleDateFormat beforeDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
//		SimpleDateFormat afterDate = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
//		Date gteDate = null;
//		Date lteDate = null;
//
//		// ES조회 형식에 맞게 날짜 데이터 변환.
//		try {
//			gteDate = beforeDate.parse(gte);
//			gte = afterDate.format(gteDate);
//			if(lte == null){
//				lte = afterDate.format(new Date());
//			}else{
//				lteDate = beforeDate.parse(lte);
//				lte = afterDate.format(lteDate);
//			}
//
//			log.debug("## gte is ["+gte+"]");
//			log.debug("## lte is ["+lte+"]");
//
//		}catch (Exception e){
//			log.error("## 날짜 변환간 오류 발생! 오류내용["+e.getMessage()+"]");
//			e.printStackTrace();
//		}
//
//		// 기본 조회 사이즈 설정.
//		if(size == null){
//			size = 20;
//		}
//
//		return vmService.retrieveAllScopeIndexMetricsRange(idxName, gte, lte, size);
//	}

	/**
	 * Index 명으로 Hypervisor,Project,ProjectDetail 최근 데이터 조회 TEST
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/인덱스명/size/20/test
	 * @param idxName
	 * @param size
	 * @return
	 */
//	@GetMapping(value = {"/metrics/index/{idxName}","/metrics/index/{idxName}/size/{size}/allTest"})
//	public Page<OpenstackMetric> retrieveAllScopeIndexMetrics(@PathVariable("idxName") String idxName,
//															  @PathVariable(value = "size", required = false) Integer size) {
//
//		if(size == null){
//			size = 20;
//		}
//
//		return vmService.retrieveAllScopeIndexMetrics(idxName, size);
//	}


	/**
	 * Index 명으로 Hypervisor 최근 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/ost-metric-hypervisor-2021-04-14/size/20/test
	 * @param idxName
	 * @param size
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/size/{size}/test")
//	public Page<HypervisorMetric> retrieveHypervisorIndexMetricsTest(@PathVariable("idxName") String idxName,
//																	 @PathVariable("size") int size) {
//
//		return vmService.retrieveHypervisorIndexMetricsTest(idxName, size);
//	}


	/**
	 * 메트릭 조건 조회를 위한 현재 관리중인 시스템 환경정보를 인데스명으로 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/init
	 * @return
	 */
//	@GetMapping("/metrics/init")
//	public Page<VmMetric> retrieveIndexMetrics() {
//		return vmService.retrieveIndexAllMetrics(index.getIndex());
//	}


	/**
	 * Index & Host 명으로 최근 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/hab-metric-demo24-1-2021.02.09/host/demo24-1
	 * @param idxName
	 * @param hostName
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/host/{hostName}")
//	public Page<VmMetric> retrieveHostMetrics(@PathVariable("idxName") String idxName,
//											  @PathVariable("hostName") String hostName) {
//		return vmService.retrieveHostMetrics(idxName, hostName);
//	}


	/**
	 * Index 명으로 최근 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/hab-metric-demo24-1-2021.02.09/size/20
	 * @param idxName
	 * @param size
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/size/{size}")
//	public Page<VmMetric> retrieveIndexMetrics(@PathVariable("idxName") String idxName,
//											   @PathVariable("size") int size) {
//
//		return vmService.retrieveIndexMetrics(idxName, size);
//	}



	/**
	 * host명 & 일자 & gte(검색시작시간범위), lte(검색종료시간범위) & cpu사용 범위 조건 설정 후 size 만큼 데이터 조회
	 * url ex) localhost:8081/symphony/dashboard/api/vm/hab-metric-demo24-1-/date/2021.02.09/metrics/demo24-1/cpu/time-range/2021-02-02 10:00:00/now/cpu-range/0.002/1.0/size/20
	 * @param host
	 * @param date
	 * @param name
	 * @param timeGte
	 * @param timeLte
	 * @param cpuGte
	 * @param cpuLte
	 * @param size
	 * @return
	 */
//	@GetMapping("/{host}/date/{date}/metrics/{name}/cpu/time-range/{timeGte}/{timeLte}/cpu-range/{cpuGte}/{cpuLte}/size/{size}")
//	public Page<VmMetric> retrieveMetricsAndCpuUsageRange(@PathVariable("host") String host,
//												  @PathVariable("date") String date,
//												  @PathVariable("name") String name,
//												  @PathVariable("timeGte") String timeGte,
//												  @PathVariable("timeLte") String timeLte,
//												  @PathVariable("cpuGte") String cpuGte,
//												  @PathVariable("cpuLte") String cpuLte,
//												  @PathVariable("size") int size){
//
//		Page<VmMetric> metrics = vmService.retrieveMetricsAndCpuUsageRange(host, date, name, timeGte, timeLte, cpuGte, cpuLte, size);
//
//		return metrics;
//	}






	/**
	 * 인덱스명 범위 조회
	 * @param host
	 * @param startDate
	 * @param endDate
	 * @param name
	 * @param metricSet
	 * @param size
	 * @return
	 * @throws ParseException
	 */
//	@GetMapping("/{host}/date/{start}/{end}/metrics/{name}/{metricset}/size/{size}")
//	public List<Page<VmMetric>> retriveeMetrics(@PathVariable("host") String host,
//													 @PathVariable("start") String startDate,
//													 @PathVariable("end") String endDate,
//													 @PathVariable("name") String name,
//													 @PathVariable("metricset") String metricSet,
//													 @PathVariable("size") int size) throws ParseException {
//
//		List<Page<VmMetric>> pages = vmService.multiRetrieveMetrics(host, startDate, endDate, name, metricSet, size);
//
//		return pages;
//	}

	/**
	 * host명 & 일자 & metricSet 으로 gte(검색시작범위), lte(검색종료범위) 조건 설정 후 size 만큼 데이터 조회
	 * url ex) localhost:8081/symphony/dashboard/api/vm/hab-metric-demo24-1-/date/2021.02.09/metrics/demo24-1/cpu/range/2021-02-02 10:00:00/now/size/20
	 * @param host
	 * @param date
	 * @param name
	 * @param metricSet
	 * @param gte
	 * @param lte
	 * @param size
	 * @return
	 */
//	@GetMapping("/{host}/date/{date}/metrics/{name}/{metricset}/range/{gte}/{lte}/size/{size}")
//	public Page<VmMetric> retrieveMetricsAndRange(@PathVariable("host") String host,
//											 	  @PathVariable("date") String date,
//												  @PathVariable("name") String name,
//												  @PathVariable("metricset") String metricSet,
//												  @PathVariable("gte") String gte,
//												  @PathVariable("lte") String lte,
//												  @PathVariable("size") int size){
//
//		Page<VmMetric> metrics = vmService.retrieveMetricsAndRange(host, date, name, metricSet, gte, lte, size);
//
//		return metrics;
//	}

	/**
	 * Terms Query 조회 - field는 metricset
	 * metricset parsing parmeter: '-'
	 * url ex) localhost:8081/symphony/dashboard/api/vm/hab-metric-demo24-1-/date/2021.02.09/metrics/cpu-network/size/100
	 * @param host
	 * @param date
	 * @param metricSet
	 * @param size
	 * @return
	 */
//	@GetMapping("/{host}/date/{date}/metrics/{metricset}/size/{size}")
//	public Page<VmMetric> retriveMetricsByTerms(@PathVariable("host") String host,
//												@PathVariable("date") String date,
//												@PathVariable("metricset") String metricSet,
//												@PathVariable("size") int size) {
//
//		Page<VmMetric> metrics = vmService.retriveMetricsByTerms(host, date, metricSet, size);
//
//		return metrics;
//	}

}
