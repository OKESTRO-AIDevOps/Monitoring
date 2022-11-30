/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.repo;

import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface VmRepository extends ElasticsearchRepository<VmMetric, String> {

	@Query("{\"bool\": {\"must\": [{\"match\": {\"host.hostname\": \"loadtest-master\"}},{\"match\" : {\"_index\": \"?0\"}}]}}")
	Page<VmMetric> findByIndexNameAndHostnames(String idxName, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}]}}")
	Page<VmMetric> findByIndexName(String idxName, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}]}}")
	Page<VmMetric> findByIndexNameAll(String idxName, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}, {\"match\": {\"host.name\": \"?1\"}}]}}")
	Page<VmMetric> findByIndexNameAndHostname(String idxName, String hostName, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"host.name\": \"?0\"}}, {\"match\": {\"metricset.name\": \"?1\"}}]}}")
	Page<VmMetric> findByNameAndMetricSet(String name, String metricSet, Pageable pageable);

	@Query("{\"bool\":{\"must\":[{\"match\":{\"host.name\": \"?0\"}},{\"match\":{\"metricset.name\": \"cpu\"}},{\"range\":{\"@timestamp\":{\"gte\":\"?1\", \"lte\": \"?2\"," +
			"\"format\": \"yyyy-MM-dd HH:mm:ss\"}}},{\"range\":{\"system.cpu.system.norm.pct\":{\"gte\":\"?3\",\"lte\":\"?4\"}}}]}}")
	Page<VmMetric> findByNameAndMetricSetAndCpuUsageRange(String name, String timeGte, String timeLte, String cpuGte, String cpuLte, Pageable pageable);

	@Query("{\"bool\":{\"must\":[{\"match\":{\"host.name\": \"?0\"}},{\"match\":{\"metricset.name\": \"?1\"}},{\"range\":{\"@timestamp\":{\"gte\":\"?2\", \"lte\": \"?3\",\"format\": \"yyyy-MM-dd HH:mm:ss\"}}}]}}}")
	Page<VmMetric> findByNameAndMetricSetAndRange(String name, String metricSet, String gte, String lte, Pageable pageable);

	@Query("{\"terms\": {\"metricset.name\": [?0]}}")
	Page<VmMetric> findByMetricSet(String metricSet, Pageable pageable);

}
