package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NetworkMetricVo {
    private List<Network> networks;
    private Network network;


    @Getter
    @Setter
    @ToString
    public static class Network {
        private String id;
//        @JsonProperty("self")
        private String cidr;
        private String netmask;
        private String gateway;
        private String broadcast;
        private String dns1;
        private String dns2;
        @JsonProperty("cidr_v6")
        private String cidrV6;
        @JsonProperty("gateway_v6")
        private String gatewayV6;
        private String label;
        @JsonProperty("netmask_v6")
        private String netmaskV6;
        @JsonProperty("created_at")
        private String createAt;
        @JsonProperty("update_at")
        private String updateAt;
        @JsonProperty("deleted_at")
        private String deletedAt;
        private String deleted;
        private String injected;
        private String bridge;
        private String vlan;
        @JsonProperty("vpn_public_address")
        private String vpnPublicAddress;
        @JsonProperty("vpn_public_port")
        private String vpnPublicPort;
        @JsonProperty("dhcp_start")
        private String dhcpStart;
        @JsonProperty("project_id")
        private String projectId;
        private String host;
        @JsonProperty("bridge_interface")
        private String bridgeInterface;
        @JsonProperty("multi_host")
        private String multiHost;
        private String priority;
        @JsonProperty("rxtx_base")
        private String rxtxBase;
        private String mtu;
        @JsonProperty("dhcp_server")
        private String dhcpServer;
        @JsonProperty("enable_dhcp")
        private String enableDhcp;
        @JsonProperty("share_address")
        private String shareAddress;
    }
}
