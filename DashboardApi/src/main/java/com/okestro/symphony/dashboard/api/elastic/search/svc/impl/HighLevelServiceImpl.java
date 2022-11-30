package com.okestro.symphony.dashboard.api.elastic.search.svc.impl;

import com.okestro.symphony.dashboard.api.elastic.search.svc.HighLevelService;
import com.okestro.symphony.dashboard.api.elastic.vm.model.ElasticIndex;
import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.repo.VmRepository;
import com.okestro.symphony.dashboard.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class HighLevelServiceImpl implements HighLevelService {


	@Autowired
	VmRepository vmRepository;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	ElasticIndex index;

	ElasticsearchRestTemplate elasticsearchRestTemplate;

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

	/**
	 * Index명으로 조회 테스트
	 * @param idxName
	 * @return
	 */
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
	public List<Page<VmMetric>> retrieveMetrics(String host, String name, String metricSet, String gte, String lte, int size) throws ParseException {
		return null;
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




}