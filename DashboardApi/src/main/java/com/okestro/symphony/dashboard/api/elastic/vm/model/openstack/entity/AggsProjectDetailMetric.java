/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "ost-metric-projects-detail-#{@elasticIndex.getIndexDate()}")
//@Document(indexName = "#{@elasticIndex.getIndex()}")
// @Document(indexName = "ost-metric-projects-detail-2021-04-14")
public class AggsProjectDetailMetric implements Serializable {

    @Id
    private String id;
    @Field(name = "@timestamp", type = FieldType.Date)
    private Date timestamp;
    @Field(name = "project_detail_info")
    private ProjectDetailInfo projectDetailInfo;

    @Getter
    @Setter
    @ToString
    public static class ProjectDetailInfo {

        @Field(name = "project_id")
        private String projectId;
        @Field(name = "project_name")
        private String projectName;
        @Field(name = "user_id")
        private String userId;
        @Field(name = "group_name")
        private String groupName;

        @Field(name = "project_quota_set")
        private ProjectQuotaSet projectQuotaSet;
        @Field(name = "project_usage")
        private ProjectUsages projectUsage;
        @Field(name = "project_minus")
        private ProjectUsages projectMinus;


        @Getter
        @Setter
        @ToString
        public static class ProjectQuotaSet {

            @Field(name = "instances_quota")
            private int  instancesQuota;
            @Field(name = "cores_quota")
            private int coresQuota;
            @Field(name = "ram_quota")
            private int ramQuota;
            @Field(name = "metadata_items_quota")
            private int  metadataItemsQuota;
            @Field(name = "injected_files_qouta")
            private int  injectedFilesQuota;
            @Field(name = "injected_file_content_bytes_qouta")
            private int  injectedFileContentBytesQuota;
            @Field(name = "injected_file_path_bytes_quota")
            private int  injectedFilePathBytesQuota;
            @Field(name = "key_pairs_quota")
            private int  keyPairsQuota;
            @Field(name = "subnet_quota")
            private int  subnetQuota;
            @Field(name = "router_quota")
            private int  routerQuota;
            @Field(name = "port_quota")
            private int  portQuota;
            @Field(name = "network_quota")
            private int  networkQuota;
            @Field(name = "fixed_ip_quota")
            private int  fixedIpsQuota;
            @Field(name = "floating_ip_quota")
            private int  floatingIpQuota;
            @Field(name = "security_group_quota")
            private int securityGroupQuota;
            @Field(name = "security_group_rule_quota")
            private int  securityGroupRuleQuota;
            @Field(name = "volume_quota")
            private int volumeQuota;
            @Field(name = "snapshot_quota")
            private int snapshotQuota;
            @Field(name = "gigabyte_quota")
            private int gigabyteQuota;

        }

        @Getter
        @Setter
        @ToString
        public static class ProjectUsages {
            @Field(name = "instances_usage")
            private int  instancesUsage;
            @Field(name = "cores_usage")
            private int coresUsage;
            @Field(name = "ram_usage")
            private int ramUsage;
            @Field(name = "metadata_items_usage")
            private int  metadataItemsUsage;
            @Field(name = "injected_files_usage")
            private int  injectedFilesUsage;
            @Field(name = "injected_file_content_bytes_usage")
            private int  injectedFileContentBytesUsage;
            @Field(name = "injected_file_path_bytes_usage")
            private int  injectedFilePathBytesUsage;
            @Field(name = "key_pairs_usage")
            private int  keyPairsUsage;
            @Field(name = "subnet_usage")
            private int  subnetUsage;
            @Field(name = "router_usage")
            private int  routerUsage;
            @Field(name = "port_usage")
            private int  portUsage;
            @Field(name = "network_usage")
            private int  networkUsage;
            @Field(name = "fixed_ip_usage")
            private int  fixedIpsUsage;
            @Field(name = "floating_ip_usage")
            private int  floatingIpUsage;
            @Field(name = "security_group_usage")
            private int securityGroupUsage;
            @Field(name = "security_group_rule_usage")
            private int  securityGroupRuleUsage;
            @Field(name = "volume_usage")
            private int volumeUsage;
            @Field(name = "snapshot_usage")
            private int snapshotUsage;
            @Field(name = "gigabyte_usage")
            private int gigabyteUsage;
        }

        @Getter
        @Setter
        @ToString
        public static class ProjectMinus {
            @Field(name = "instances_usage")
            private int  instancesUsage;
            @Field(name = "cores_usage")
            private int coresUsage;
            @Field(name = "ram_usage")
            private int ramUsage;
            @Field(name = "metadata_items_usage")
            private int  metadataItemsUsage;
            @Field(name = "injected_files_usage")
            private int  injectedFilesUsage;
            @Field(name = "injected_file_content_bytes_usage")
            private int  injectedFileContentBytesUsage;
            @Field(name = "injected_file_path_bytes_usage")
            private int  injectedFilePathBytesUsage;
            @Field(name = "key_pairs_usage")
            private int  keyPairsUsage;
            @Field(name = "subnet_usage")
            private int  subnetUsage;
            @Field(name = "router_usage")
            private int  routerUsage;
            @Field(name = "port_usage")
            private int  portUsage;
            @Field(name = "network_usage")
            private int  networkUsage;
            @Field(name = "fixed_ip_usage")
            private int  fixedIpsUsage;
            @Field(name = "floating_ip_usage")
            private int  floatingIpUsage;
            @Field(name = "security_group_usage")
            private int securityGroupUsage;
            @Field(name = "security_group_rule_usage")
            private int  securityGroupRuleUsage;
            @Field(name = "volume_usage")
            private int volumeUsage;
            @Field(name = "snapshot_usage")
            private int snapshotUsage;
            @Field(name = "gigabyte_usage")
            private int gigabyteUsage;
        }
    }



}
