package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.SecGroupVo;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openstack4j.model.compute.Address;
import org.openstack4j.model.compute.Addresses;
import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.Image;
import org.openstack4j.openstack.common.Metadata;
import org.openstack4j.openstack.compute.domain.NovaSecGroupExtension;

import java.util.*;

@Getter
@Setter
@ToString
public class ServerMetricVo {

    @JsonProperty("servers")
    private List<Server> servers;

    @Getter
    @Setter
    @ToString
    public static class Server {
        private String id;
        private String name;
        private String status;
        @JsonProperty("tenant_id")
        private String tenantId;
        @JsonProperty("user_id")
        private String userId;
        private Metadata metadata;              // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.
        private String hostId;
        private Image image;                    // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.
        private Flavor flavor;                  // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.
        private Date created;
        private Date updated;
        private AddressesDetail addresses;            // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.
        @JsonProperty("accessIPv4")
        private String accessIpv4;
        @JsonProperty("accessIPv6")
        private String accessIpv6;
        private List<Links> links;
        @JsonProperty("OS-DCF:diskConfig")
        private String diskConfig;
        private int progress;
        @JsonProperty("OS-EXT-AZ:availability_zone")
        private String availabilityZone;
        @JsonProperty("configDrive")
        private String configDrive;
        @JsonProperty("key_name")
        private String keyName;
        @JsonProperty("OS-SRV-USG:launched_at")
        private Date launchedAt;
        @JsonProperty("OS-SRV-USG:terminated_at")
        private String terminatedAt;
        @JsonProperty("OS-EXT-SRV-ATTR:host")
        private String osHost;
        @JsonProperty("OS-EXT-SRV-ATTR:instance_name")
        private String instanceName;
        @JsonProperty("OS-EXT-SRV-ATTR:hypervisor_hostname")
        private String hypervisorHostname;
        @JsonProperty("OS-EXT-STS:task_state")
        private String taskState;
        @JsonProperty("OS-EXT-STS:vm_state")
        private String vmState;
        @JsonProperty("OS-EXT-STS:power_state")
        private int powerState;
        @JsonProperty("os-extended-volumes:volumes_attached")
        private List<ExtenedVolumes> extenedVolumes;
        private List<SecGroupVo> securityGroups;  // openstack4j에 정의되어있는 Vo쓴거라서 에러나면 직접만들어서사용.

        public void setImage(Object image) {            // 응답으로 ""올때가있어서 구현.
            if(image instanceof Image){
                this.image = (Image) image;
            }else{
                this.image = null;
            }
        }

    }




    @Data
    public static class ExtenedVolumes{
        private String id;
    }

//    @Data
//    public class Metadata{
//
//    }

    @Data
    public static class AddressesDetail implements Addresses {
    Map<String, List<? extends Address>> addressListMap = new HashMap<>();
    List<Address> addressList = new ArrayList<>();

        @Override
        public void add(String key, Address value) {

            addressList = (List<Address>) addressListMap.get(key);
            addressList.add(value);
            addressListMap.put(key, addressList);

        }

        @Override
        public Map<String, List<? extends Address>> getAddresses() {

            return addressListMap;
        }

        @Override
        public List<? extends Address> getAddresses(String type) {
            return addressList;
        }
    }


    @Getter
    @Setter
    @ToString
    public static class Flavor{
        private String id;
        @JsonProperty("links")
        private List<Links> links;


    }

    @Getter
    @Setter
    @ToString
    public static class Image{
        private String id;
        @JsonProperty("links")
        private List<Links> links;
    }

        @Getter
        @Setter
        @ToString
        public static class Links {
            private String rel;
            @JsonProperty("href")
            private String href;
        }
}
