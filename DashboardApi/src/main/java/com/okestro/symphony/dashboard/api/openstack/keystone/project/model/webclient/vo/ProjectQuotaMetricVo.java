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
public class ProjectQuotaMetricVo {

    @JsonProperty("quota_set")
    private QuotaSet quotaSets;

    @Getter
    @Setter
    @ToString
    public static class QuotaSet{
        private String id;
        private String name;
        private Cores cores;
        @JsonProperty("fixed_ips")
        private FixedIps fixedIps;
        @JsonProperty("floating_ips")
        private FloatingIps floatingIps;
        @JsonProperty("injected_file_content_bytes")
        private InjectedFileContentBytes injectedFileContentBytes;
        @JsonProperty("injected_file_path_bytes")
        private InjectedFilePathBytes injectedFilePathBytes;
        @JsonProperty("injected_files")
        private InjectedFiles injectedFiles;
        private Instances instances;
        @JsonProperty("key_pairs")
        private KeyPairs keyPairs;
        @JsonProperty("metadata_items")
        private MetadataItems metadataItems;
        private Ram ram;
        @JsonProperty("security_group_rules")
        private SecurityGroupRules securityGroupRules;
        @JsonProperty("security_groups")
        private SecurityGroups securityGroups;
        @JsonProperty("server_group_members")
        private ServerGroupMembers serverGroupMembers;
        @JsonProperty("server_groups")
        private ServerGroups serverGroups;



        @Getter
        @Setter
        @ToString
        public class Cores {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class FixedIps {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class FloatingIps {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class InjectedFileContentBytes {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class InjectedFilePathBytes {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class InjectedFiles {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class Instances {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class KeyPairs {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class MetadataItems {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class Ram {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class SecurityGroupRules {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class SecurityGroups {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class ServerGroupMembers {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }

        @Getter
        @Setter
        @ToString
        public class ServerGroups {

            private int limit;
            @JsonProperty("in_use")
            private int inUse;
            private int reserved;

        }
    }


}
