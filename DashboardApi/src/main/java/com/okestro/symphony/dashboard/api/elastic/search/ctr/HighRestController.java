/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-15 10:40:10
 */


package com.okestro.symphony.dashboard.api.elastic.search.ctr;

import com.okestro.symphony.dashboard.api.elastic.vm.model.ElasticIndex;
import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.svc.VmService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@Slf4j
@CrossOrigin("*")
//@Api(value = "elasticSearchMetricCollectData", description = "ElasticSearch Metric 수집 데이터 조회 리스트")
//@RequestMapping("/es")
@RestController
public class HighRestController {
	@Autowired
	VmService vmService;

	@Autowired
	ElasticIndex index;


	/**
	 * 메트릭 조건 조회를 위한 현재 관리중인 시스템 환경정보를 인데스명으로 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/init
	 * @returnㅑ8
	 */
//	@GetMapping("/metrics/init")
	public Page<VmMetric> retrieveIndexMetrics() {
		return vmService.retrieveIndexAllMetrics(index.getIndex());
	}


	/**
	 * Index & Host 명으로 최근 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/hab-metric-demo24-1-2021.02.09/host/demo24-1
	 * @param idxName
	 * @param hostName
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/host/{hostName}")
	public Page<VmMetric> retrieveHostMetrics(@PathVariable("idxName") String idxName,
											  @PathVariable("hostName") String hostName) {
		return vmService.retrieveHostMetrics(idxName, hostName);
	}


	/**
	 * Index 명으로 최근 데이터 조회
	 * url ex)  localhost:8081/symphony/dashboard/api/vm/metrics/index/hab-metric-demo24-1-2021.02.09/size/20
	 * @param idxName
	 * @param size
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/size/{size}")
	public Page<VmMetric> retrieveIndexMetrics(@PathVariable("idxName") String idxName,
											   @PathVariable("size") int size) {

		return vmService.retrieveIndexMetrics(idxName, size);
	}

	/**
	 * Index & Host 명으로 최근 데이터 조회 (2021.04.02 테스트중)
	 * url ex)  localhost:8081/symphony/dashboard/api/vm//metrics/index/sym-metric-cpu/host/loadtest-master
	 * @param idxName
	 * @param hostName
	 * @return
	 */
//	@GetMapping("/metrics/index/{idxName}/hostName/{hostName}")
	public Page<VmMetric> retrieveIndexAndHostMetrics(@PathVariable("idxName") String idxName,
											   @PathVariable("hostName") String hostName) {

		return vmService.retrieveIndexAndHostMetricsService(idxName, hostName);
	}


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
	public Page<VmMetric> retrieveMetricsAndCpuUsageRange(@PathVariable("host") String host,
												  @PathVariable("date") String date,
												  @PathVariable("name") String name,
												  @PathVariable("timeGte") String timeGte,
												  @PathVariable("timeLte") String timeLte,
												  @PathVariable("cpuGte") String cpuGte,
												  @PathVariable("cpuLte") String cpuLte,
												  @PathVariable("size") int size){

		Page<VmMetric> metrics = vmService.retrieveMetricsAndCpuUsageRange(host, date, name, timeGte, timeLte, cpuGte, cpuLte, size);

		return metrics;
	}

	/**
	 * host명 & metricSet 으로 size 만큼 데이터 조회
	 * @param name
	 * @param metricSet
	 * @param size
	 * @return
	 */
//	@GetMapping("/metrics/{name}/{metricset}/size/{size}")
	public Page<VmMetric> retrieveMetricsTest(@PathVariable("name") String name,
											  @PathVariable("metricset") String metricSet,
											  @PathVariable("size") int size) {

		index.setIndex("hab-metric-demo24-1-2021.02.09");
		Page<VmMetric> metrics1 = vmService.retrieveMetrics(name, metricSet, size);
		List<VmMetric> met1 = metrics1.getContent();
		index.setIndex("hab-metric-demo24-1-2021.02.09");

		metricSet = "cpu";
		Page<VmMetric> metrics2 = vmService.retrieveMetrics(name, metricSet, size);
		List<VmMetric> met2 = metrics2.getContent();
		VmMetric vm2 = met2.get(0);
		return null;
	}


	/**
	 * host명 & 일자 & metricSet 으로 size 만큼 데이터 조회
	 * @param host
	 * @param date
	 * @param name
	 * @param metricSet
	 * @param size
	 * @return
	 */
//	@GetMapping("/{host}/date/{date}/metrics/{name}/{metricset}/size/{size}")
	public Page<VmMetric> retrieveMetrics(@PathVariable("host") String host,
											   @PathVariable("date") String date,
											   @PathVariable("name") String name,
											   @PathVariable("metricset") String metricSet,
											   @PathVariable("size") int size) {

		return vmService.retrieveMetrics(host, date, name, metricSet, size);

	}


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
	public List<Page<VmMetric>> retriveeMetrics(@PathVariable("host") String host,
													 @PathVariable("start") String startDate,
													 @PathVariable("end") String endDate,
													 @PathVariable("name") String name,
													 @PathVariable("metricset") String metricSet,
													 @PathVariable("size") int size) throws ParseException {

		List<Page<VmMetric>> pages = vmService.multiRetrieveMetrics(host, startDate, endDate, name, metricSet, size);

		return pages;
	}

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
	public Page<VmMetric> retrieveMetricsAndRange(@PathVariable("host") String host,
											 	  @PathVariable("date") String date,
												  @PathVariable("name") String name,
												  @PathVariable("metricset") String metricSet,
												  @PathVariable("gte") String gte,
												  @PathVariable("lte") String lte,
												  @PathVariable("size") int size){

		Page<VmMetric> metrics = vmService.retrieveMetricsAndRange(host, date, name, metricSet, gte, lte, size);

		return metrics;
	}

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
	public Page<VmMetric> retriveMetricsByTerms(@PathVariable("host") String host,
												@PathVariable("date") String date,
												@PathVariable("metricset") String metricSet,
												@PathVariable("size") int size) {

		Page<VmMetric> metrics = vmService.retriveMetricsByTerms(host, date, metricSet, size);

		return metrics;
	}

}
