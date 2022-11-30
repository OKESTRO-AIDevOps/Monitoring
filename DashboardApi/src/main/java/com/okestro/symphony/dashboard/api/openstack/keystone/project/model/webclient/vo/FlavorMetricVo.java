package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.openstack.common.Metadata;

import java.util.List;

@Getter
@Setter
@ToString
public class FlavorMetricVo {
    private List<Flavor> flavors;
    private Flavor flavor;

    @Getter
    @Setter
    @ToString
    public class Flavor {
        private String id;
        private String name;
        private double ram;
        private double disk;
        private String swap;
        @JsonProperty("OS-FLV-EXT-DATA:ephemeral")
        private double ephemeral;
        @JsonProperty("OS-FLV-DISABLED:disabled")
        private boolean disabled;
        private double vcpus;
        @JsonProperty("s-flavor-access:is_public")
        private boolean isPublic;
        @JsonProperty("rxtx_factor")
        private double rxtxFactor;
        private List<Link> links;
    }

        @Getter
        @Setter
        @ToString
        public static class Link {
                private String rel;
                @JsonProperty("href")
                private String href;
        }
}
