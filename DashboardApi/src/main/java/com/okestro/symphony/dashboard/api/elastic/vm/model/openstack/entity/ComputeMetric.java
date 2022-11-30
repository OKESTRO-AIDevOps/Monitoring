/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "ost-service-compute-#{@elasticIndex.getIndexDate()}")
public class ComputeMetric implements Serializable {

    @Id
    private String id;

    @Field(name = "@timestamp")
    private long timestamp;

    @Field(name = "nova_name")
    private String novaNm;

    @Field(name = "cpu_quota")
    private Double cpuQuota;
    @Field(name = "cpu_usage")
    private Double cpuUsage;
    @Field(name = "network_quota")
    private Double networkQuota;
    @Field(name = "network_usage")
    private Double networkUsage;
    @Field(name = "instance_quota")
    private Double instanceQuota;
    @Field(name = "instance_usage")
    private Double instanceUsage;

}
