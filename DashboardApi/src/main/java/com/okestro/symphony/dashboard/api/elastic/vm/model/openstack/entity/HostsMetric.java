/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;


@Getter
@Document(indexName = "ost-metric-hosts-#{@elasticIndex.getIndexDate()}")
// @Document(indexName = "ost-metric-hypervisor-2021-04-14")
public class HostsMetric {


    @Id
    private String id;

    @Field(name = "@timestamp")
    private String timestamp;

    @Field(name = "host")
    private Hosts host;


    @Builder
    public HostsMetric(String id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;

    }

    @Getter
    public class Hosts {
        @Field(name = "host_name")
        private String hostName;
        private String service;
        private String zone;
    }

}
