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
import java.util.LinkedHashMap;


@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "ost-metric-projects-#{@elasticIndex.getIndexDate()}") // 현재날짜 셋팅.
// @Document(indexName = "ost-metric-projects-2021-04-14")
public class ProjectsMetric implements Serializable {


    @Id
    private String id;

    @Field(name = "@timestamp")
    private String timestamp;

    @Field(name = "project")
    private Project project;


    @Builder
    public ProjectsMetric(String id, String timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    @Getter
    @Setter
    @ToString
    public static class Project {
        private String projectId;
        private String projectName;
        @Field(name = "domain_id")
        private String domainId;
        private String description;
        private boolean enabled;
        private String parentId;
        @Field(name = "is_domain")
        private boolean isDomain;
        private LinkedHashMap<String,Object> tags;
        private LinkedHashMap<String,Object> options;
        private LinkedHashMap<String,Object> links;
    }

}
