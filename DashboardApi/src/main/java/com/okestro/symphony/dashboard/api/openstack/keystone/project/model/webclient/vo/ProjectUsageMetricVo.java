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
public class ProjectUsageMetricVo {

    @JsonProperty("tenant_id")
    private String tenantId;
    @JsonProperty("server_usages")
    private List<InstanceUsage> instancesUsage;

    @JsonProperty("total_local_gb_usage")
    private double totalLocalGbUsage;
    @JsonProperty("total_vcpus_usage")
    private double totalVcpusUsage;
    @JsonProperty("total_memory_mb_usage")
    private double totalMemoryMbUsage;
    @JsonProperty("total_hours")
    private double totalHours;
    private String start;
    private String stop;


    @Data
    public static class InstanceUsage {
        private double hours;
        private String flavor;
        @JsonProperty("instance_id")
        private String instanceId;
        private String name;
        @JsonProperty("tenant_id")
        private String tenantId;
        @JsonProperty("memory_mb")
        private double memoryMb;
        @JsonProperty("local_gb")
        private double localGb;
        private double vcpus;
        @JsonProperty("started_at")
        private String startedAt;
        @JsonProperty("ended_at")
        private String endedAt;
        private String state;
        private double uptime;

    }
}
