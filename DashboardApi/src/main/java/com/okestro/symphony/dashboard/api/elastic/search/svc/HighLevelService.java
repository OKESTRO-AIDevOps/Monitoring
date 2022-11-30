package com.okestro.symphony.dashboard.api.elastic.search.svc;

import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;


public interface HighLevelService {


	Page<VmMetric> retrieveIndexAndHostMetricsService(String idxName, String hostName);

	Page<VmMetric> retrieveHostMetrics(String idxName, String hostName);

	Page<VmMetric> retrieveIndexAllMetrics(String idxName);

	Page<VmMetric> retrieveIndexMetrics(String idxName, int size);

	Page<VmMetric> retrieveMetrics(String name, String metricSet, int size);

	Page<VmMetric> retrieveMetrics(String host, String date, String name, String metricSet, int size);

	List<Page<VmMetric>> multiRetrieveMetrics(String host, String startDate, String endDate, String name, String metricSet, int size) throws ParseException;

	List<Page<VmMetric>> retrieveMetrics(String host, String name, String metricSet, String gte, String lte, int size) throws ParseException;

	Page<VmMetric> retrieveMetricsAndCpuUsageRange(String host, String date, String name, String timeGte, String timeLte, String cpuGte, String cpuLte, int size);

	Page<VmMetric> retrieveMetricsAndRange(String host, String date, String name, String metricSet, String gte, String lte, int size);

	Page<VmMetric> retriveMetricsByTerms(String host, String date, String metricSet, int size);

}