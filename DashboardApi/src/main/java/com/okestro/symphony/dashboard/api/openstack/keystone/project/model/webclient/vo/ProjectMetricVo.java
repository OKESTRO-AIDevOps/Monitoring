package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ProjectMetricVo {
    private List<Project> projects;
    private Links links;

    @Getter
    @Setter
    @ToString
    public static class Project {
        private String id;
        private String name;
        @JsonProperty("description")
        private String desc;
        private boolean enabled;
        @JsonProperty("parent_id")
        private String parentId;
        @JsonProperty("is_domain")
        private boolean isDomain;
        @JsonProperty("domain_id")
        private String domainId;
        private String[] tags;
        //    private Options options;
        private Links links;


        @Getter
        @Setter
        @ToString
        public static class Links {
            @JsonProperty("self")
            private String self;

            @JsonProperty("enabled")
            private String enabled;

        }
    }


    @Getter
    @Setter
    @ToString
    public static class Links {
        private String next;
        @JsonProperty("self")
        private String self;
        private String previous;
    }
}
