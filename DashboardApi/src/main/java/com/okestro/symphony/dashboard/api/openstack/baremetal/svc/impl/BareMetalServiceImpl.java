/*
 * Developed by bhhan@okestro.com on 2020-07-17
 * Last modified 2020-07-16 20:34:33
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.svc.impl;

import com.google.gson.*;
import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.BareMetalVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.DriverVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.MapperVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.NodeVo;
import com.okestro.symphony.dashboard.api.openstack.baremetal.svc.BareMetalService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class BareMetalServiceImpl implements BareMetalService {
    @Autowired
    OpenStackConnectionService openStackConnectionService;

    /**
     * check admin role
     */
    @Override
    public boolean isHasRole(String projectName, String userId) {
        boolean hasRole = false;

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(projectName, userId);

            if (osClient.getToken().getRoles() != null && osClient.getToken().getRoles().size() > 0) {
                for (Role role : osClient.getToken().getRoles()) {
                    if ("admin".equalsIgnoreCase(role.getName())) {
                        hasRole = true;
                        break;
                    }
                }
            }
        } catch (ConnectException e)  {
            // log
            log.error(e.getMessage());
        }

        return hasRole;
    }

    /**
     * retrieve nodes
     */
    @Override
    public List<NodeVo> retrieveNodes(String projectName, String userId) {
        List<NodeVo> nodes = null;

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(projectName, userId);

            // create instance
            WebClient webClient = getWebClient(osClient, projectName, userId);

            // get node lists
            String response = webClient.get()
                    .uri("/v1/nodes/detail")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            if (response != null && response.length() > 0) {
                Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                BareMetalVo bareMetal = gson.fromJson(response, BareMetalVo.class);

                // get nodes
                nodes = bareMetal.getNodes();
            }
        } catch (ConnectException | SSLException e)  {
            // log
            log.error(e.getMessage(), e);
        }

        return nodes;
    }

    /**
     * retrieve node detail
     */
    @Override
    public NodeVo retrieveNodeDetail(String projectName, String userId, String uuid) {
        NodeVo node = null;

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(projectName, userId);

            // create instance
            WebClient webClient = getWebClient(osClient, projectName, userId);

            // get node lists
            String response = webClient.get()
                    .uri("/v1/nodes/" + uuid)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            if (response != null && response.length() > 0) {
                Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                node = gson.fromJson(response, NodeVo.class);

                // properties
                if (node.getProperties() != null) {
                    List<MapperVo> propertyList = new ArrayList<MapperVo>();

                    // set properties map to list
                    for (Map.Entry<String, Object> entry : node.getProperties().entrySet()) {
                        MapperVo mapper = new MapperVo();

                        mapper.setKey(entry.getKey());
                        mapper.setValue(entry.getValue());

                        // add property
                        propertyList.add(mapper);
                    }

                    node.setPropertyList(propertyList);
                }

                // extras
                if (node.getExtra() != null) {
                    List<MapperVo> extraList = new ArrayList<MapperVo>();

                    // set extras map to list
                    for (Map.Entry<String, Object> entry : node.getExtra().entrySet()) {
                        MapperVo mapper = new MapperVo();

                        mapper.setKey(entry.getKey());
                        mapper.setValue(entry.getValue());

                        // add extra
                        extraList.add(mapper);
                    }

                    node.setExtraList(extraList);
                }

                // set driver info(drac)
                if (node.getDriver().equalsIgnoreCase("idrac")) {
                    DriverVo driverVo = new DriverVo();

                    driverVo.setKernel((String) node.getDriverInfo().get("deploy_kernel"));
                    driverVo.setRamdisk((String) node.getDriverInfo().get("deploy_ramdisk"));
                    driverVo.setAddress((String) node.getDriverInfo().get("drac_address"));
                    driverVo.setPort((String) node.getDriverInfo().get("drac_port"));
                    driverVo.setName((String) node.getDriverInfo().get("drac_username"));

                    node.setDriverVo(driverVo);
                } else {
                    //@TODO-기타 드라이버
                }
            }
        } catch (ConnectException | SSLException e)  {
            // log
            log.error(e.getMessage(), e);
        }

        return node;
    }

    /**
     * retrieve drivers
     */
    @Override
    public List<DriverVo> retrieveDrivers(String projectName, String userId) {
        List<DriverVo> drivers = null;

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(projectName, userId);

            // create instance
            WebClient webClient = getWebClient(osClient, projectName, userId);

            // get node lists
            String response = webClient.get()
                    .uri("/v1/drivers")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            if (response != null && response.length() > 0) {
                Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                BareMetalVo bareMetal = gson.fromJson(response, BareMetalVo.class);

                // get drivers
                drivers = bareMetal.getDrivers();
            }
        } catch (ConnectException | SSLException e)  {
            // log
            log.error(e.getMessage(), e);
        }

        return drivers;
    }

    /**
     * get web client
     */
    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
        String bareMetalUrl = getBareMetalUrl(projectName, userId);

        if (bareMetalUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(bareMetalUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .defaultHeader("X-OpenStack-Ironic-API-Version", OpenStackConstant.BAREMETAL_API_VERSION)
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(bareMetalUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .defaultHeader("X-OpenStack-Ironic-API-Version", OpenStackConstant.BAREMETAL_API_VERSION)
                    .build();
        }

        return webClient;
    }

    /**
     * get baremetal(ironic) url
     * 일반 사용자 계정으로 엔드포인트 url 조회가 안돼 keystone url을 편집하여 사용
     */
    private String getBareMetalUrl(String projectName, String userId) {
        String url = null;

        // get keystone url
//        url = openStackConnectionService.getKeystoneUrl(projectName, userId);

        // get glance url
        url = url.substring(0, url.lastIndexOf(":")) + ":6385";

        return url;
    }

    /**
     * connect to openstack
     */
    private OSClient.OSClientV3 connect(String projectName, String userId) throws ConnectException {
        return openStackConnectionService.connect(projectName, userId);
    }
}
