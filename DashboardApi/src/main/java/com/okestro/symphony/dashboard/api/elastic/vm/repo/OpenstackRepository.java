/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.repo;

import com.okestro.symphony.dashboard.api.elastic.vm.model.OpenstackMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.VmMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.List;


public interface OpenstackRepository extends ElasticsearchRepository<OpenstackMetric, String> {

	@Query("{\"bool\": {\"must\": [{\"match\": {\"host.hostname\": \"loadtest-master\"}},{\"match\" : {\"_index\": \"?0\"}}]}}")
	Page<OpenstackMetric> findByIndexNameAndHostnames(String idxName, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}]}}")
	Page<OpenstackMetric> findByIndexName(String idxName, Pageable pageable);

	//gte --> 피연산자보다 크거나 같음 ,lte --> 피연산자보다 작거나 같음
	@Query("{\"bool\":{\"must\":[{\"match\":{\"_index\":\"?0\"}},{\"range\":{\"@timestamp\":{\"gte\":\"?1\",\"lte\":\"?2\"}}}]}}")
	Page<OpenstackMetric> findByIndexNameAndRange(String idxName, String gte, String lte, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}]}}")
	Page<OpenstackMetric> findByAllScopeIndexName(String idxName, Pageable pageable);



}
