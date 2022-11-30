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
public class HostAggregateMetricVo {

    @JsonProperty("aggregates")
    private List<HostAggregates> hostAggregates;

    @Getter
    @Setter
    @ToString
    public static class HostAggregates {

        private int id;
        private String name;
        private String[] hosts;
        private String status;
        private Metadata metadata;

        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
        @JsonProperty("deleted_at")
        private String deletedAt;
        private String deleted;
        @JsonProperty("availability_zone")
        private String availabilityZone;

        @Getter
        @Setter
        @ToString
        public static class Metadata {

            @JsonProperty("availability_zone")
            private String availabilityZone;

        }
    }

}
