package com.okestro.symphony.dashboard.api.elastic.vm.svc;

import com.okestro.symphony.dashboard.api.elastic.vm.model.OpenstackMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo.*;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.HypervisorMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectsMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ComputeMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.HypervisorMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ProjectDetailMetricVo;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo.ProjectInfoVo;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;



public interface VmService {

	boolean updateHypervisorIndex(List<HypervisorMetricVo> hypervisorMetricVoList);

	boolean updateProjectIndex(List<ProjectInfoVo> projectInfoVoList);

	boolean updateProjectDetailIndex(List<ProjectDetailMetricVo> projectDetailMetricVoList);

	boolean updateComputeIndex(ComputeMetricVo computeMetricVo);

	// metric meta Data select
	Page<OpenstackMetric> retrieveAllMetricTypeToRange(String metricName, String gte, String lte, int size);

	List<OstProjectMetricVo> retrieveProjectMetricToRange(BatchQueryRequestVo batchQueryRequestVo);

	Page<OpenstackMetric> retrieveAllScopeIndexMetricsRange(String idxName, String gte, String lte, int size);

	Page<OpenstackMetric> retrieveAllScopeIndexMetrics(String idxName, int size);

	Page<ProjectsMetric> retrieveProjectIndexMetricsTest(String idxName, int size);

	Page<HypervisorMetric> retrieveHypervisorIndexMetricsTest(String idxName, int size);


	// metric Data Select
//	List<OstProjectCpuVo> retrieveProjectCpuMetric(String metricName, int size);

	// metric aggregation Data select
	List<OstProjectAggregatedVo> retrieveAggregateProjectMetricToRange(BatchQueryRequestVo batchQueryRequestVo);

	List<OstHypervisorAggregatedVo>  retrieveAggregateHypervisorMetricToRange(BatchQueryRequestVo batchQueryRequestVo);

	OstHypervisorGraphAggregatedVo  retrieveGraphAggregateHypervisorMetricToRange(BatchQueryRequestVo batchQueryRequestVo);

	List<OstInstanceAggregatedVo> retrieveAggregateInstanceMetricToRange(BatchQueryRequestVo batchQueryRequestVo);

	OstInstanceGraphAggregatedVo retrieveGraphAggregateInstanceMetricToRange(String metricName, String dateType, String dateUnit, int numSeries ,ArrayList<String> instanceTopList);




	// service Data Select
	OstComputeMetricVo retrieveComputeMetricToRange (String metricName, String dateType, String dateUnit, int size);






	Page<VmMetric> retrieveIndexAndHostMetricsService(String idxName, String hostName);

	Page<VmMetric> retrieveHostMetrics(String idxName, String hostName);

	Page<VmMetric> retrieveIndexAllMetrics(String idxName);

	Page<VmMetric> retrieveIndexMetrics(String idxName, int size);

	Page<VmMetric> retrieveMetrics(String name, String metricSet, int size);

	Page<VmMetric> retrieveMetrics(String host, String date, String name, String metricSet, int size);

	List<Page<VmMetric>> multiRetrieveMetrics(String host, String startDate, String endDate, String name, String metricSet, int size) throws ParseException;

	Page<VmMetric> retrieveMetricsAndCpuUsageRange(String host, String date, String name, String timeGte, String timeLte, String cpuGte, String cpuLte, int size);

	Page<VmMetric> retrieveMetricsAndRange(String host, String date, String name, String metricSet, String gte, String lte, int size);

	Page<VmMetric> retriveMetricsByTerms(String host, String date, String metricSet, int size);

}