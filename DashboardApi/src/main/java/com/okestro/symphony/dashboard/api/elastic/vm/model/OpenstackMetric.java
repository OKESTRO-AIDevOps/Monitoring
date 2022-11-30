/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model;


import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.HostsMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.HypervisorMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectDetailMetric;
import com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity.ProjectsMetric;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Slf4j
@Setter
@Getter
@NoArgsConstructor
@Document(indexName = "#{@elasticIndex.getIndex()}")
// @Document(indexName = "test_beats_metric_2021_02_02")
public class OpenstackMetric {


    @Id
    private String id;

    @Field(name = "@timestamp")
    private String timestamp;

    @Field(name = "hypervisor")
    private HypervisorMetric.Hypervisors hypervisor;

    @Field(name = "host")
    private HostsMetric.Hosts host;

    @Field(name = "project")
    private ProjectsMetric.Project project;

    @Field(name = "project_detail_info")
    private ProjectDetailMetric.ProjectDetailInfo projectDetailInfo;


    @Builder
    public OpenstackMetric(String timestamp, String id) {
        this.id = id;
        this.timestamp = timestamp;
    }

}
