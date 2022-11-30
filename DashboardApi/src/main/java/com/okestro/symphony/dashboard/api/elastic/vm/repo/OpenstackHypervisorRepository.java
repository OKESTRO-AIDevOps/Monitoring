/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.repo;

import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.HypervisorMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface OpenstackHypervisorRepository extends ElasticsearchRepository<HypervisorMetric, String> {


	@Query("{\"bool\": {\"must\": [{\"match\": {\"_index\": \"?0\"}}]}}")
	Page<HypervisorMetric> findByIndexNameTest(String idxName, Pageable pageable);

}
