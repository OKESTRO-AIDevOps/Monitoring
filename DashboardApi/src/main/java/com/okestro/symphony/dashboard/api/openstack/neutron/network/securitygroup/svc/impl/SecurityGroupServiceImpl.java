package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.svc.impl;

import com.google.gson.*;
import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.cmm.msg.OpenStackResultVo;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model.*;
import com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.svc.SecurityGroupService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.identity.v3.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Data
public class SecurityGroupServiceImpl implements SecurityGroupService {

    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Autowired
    AuditLogService auditLogService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    WebsocketService websocketService;

    /**
     * 보안그룹조회(WebClient방식)
     */
    @Override
    public OpenStackResultVo getSecurityGroups(String projectName, String userId) {
        OpenStackResultVo openStackResultVo = new OpenStackResultVo();

        List<SecurityGroupVo> list = null;
        List<SecurityGroupVo> filteredList = new ArrayList<>();

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(projectName, userId);
            // projectId
            String projectId = osClient.getToken().getProject().getId();

            // get webclient
            WebClient webClient = getWebClient(osClient, projectName, userId);

            // retrieve security groups
            String resultData = webClient.get()
                    .uri("/v2.0/security-groups")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            // parsing
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

//            JsonObject jsonObject = JsonParser.parseString(resultData).getAsJsonObject();

            SecurityGroupGroupVo securityGroupGroupVo = gson.fromJson(resultData, SecurityGroupGroupVo.class);

            // get security group lists
            list = securityGroupGroupVo.getSecurityGroups();

            //같은 tenantId(프로젝트) 인 데이터만 list에 보여줌 jh
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getTenantId().equals(projectId)) {
                    filteredList.add(list.get(i));
                }
            }
            openStackResultVo.setViewList(filteredList);
            openStackResultVo.setResultCode(OpenStackConstant.SUCCEEDED);


        } catch (ConnectException | SSLException e) {
            openStackResultVo.setResultCode(OpenStackConstant.FAILED);
            log.error(e.getMessage(), e);
        }


        return openStackResultVo;
    }

    /**
     * 보안 그룹 상세
     */
    @Override
    public OpenStackResultVo retrieveSecurityGroup(SecurityGroupVo securityGroupVo) {
        OpenStackResultVo openStackResultVo = new OpenStackResultVo();

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = connect(securityGroupVo.getProjectName(), securityGroupVo.getUserId());

            // get webclient
            WebClient webClient = getWebClient(osClient, securityGroupVo.getProjectName(), securityGroupVo.getUserId());


            try {

                String secGroupId = securityGroupVo.getId();

                // get resource
                String resultData = webClient.get()
                        .uri("/v2.0/security-groups/{secGroupId}", secGroupId)
                        .retrieve()
                        .bodyToMono(String.class)
                        .log()
                        .block();
                // json parsing
                Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                DetailSecGroupVo securityGroup = new DetailSecGroupVo();
                JsonObject jsonObject = new JsonParser().parse(resultData).getAsJsonObject();
                securityGroup = gson.fromJson(jsonObject.get("security_group"), DetailSecGroupVo.class);
                openStackResultVo.setResultCode(OpenStackConstant.SUCCEEDED);
                openStackResultVo.setViewList(securityGroup);


            } catch (Exception e) {
                log.error(e.getMessage(), e);
                openStackResultVo.setResultCode(OpenStackConstant.ERROR);
            }
        } catch (ConnectException | SSLException e) {
            log.error(e.getMessage(), e);
        }
        return openStackResultVo;


    }

    /**
     * 보안 그룹 이름 중복 체크
     */
    public OpenStackResultVo checkDupSecGroupName(String projectName, String name, String userId) {
        OpenStackResultVo openStackResultVo = new OpenStackResultVo();
        boolean result = false;

        try {
            // get openstack client
            OSClient.OSClientV3 osClient = connect(projectName, userId);

            List<? extends SecGroupExtension> sg = osClient.compute().securityGroups().list();

            for (int i = 0; i < sg.size(); i++) {
                if (name.equals(sg.get(i).getName())) {
                    result = name.equals(sg.get(i).getName());
                    openStackResultVo.setResultCode(OpenStackConstant.EXIST);
                    break;
                } else {

                    openStackResultVo.setResultCode(OpenStackConstant.FAILED);
                }

            }
        } catch (ConnectException e) {
            log.error(e.getMessage(), e);
        } finally {
            openStackConnectionService.close();
        }

        return openStackResultVo;

    }

    /**
     * secGroup detail에서 사용할 부분
     *
     * @param osClient
     * @return
     */
    private String getEndPointUrl(OSClient.OSClientV3 osClient, String service1, String service2) {
        String metricUrl = null;

        // get endpoints
        List<? extends Endpoint> endpoints = osClient.identity().serviceEndpoints().listEndpoints();

        if (endpoints != null & endpoints.size() > 0) {
            for (Endpoint endpoint : endpoints) {
                if (endpoint.getIface().toString().equalsIgnoreCase("admin")) {
                    // get service
                    org.openstack4j.model.identity.v3.Service service = osClient.identity().serviceEndpoints().get(endpoint.getServiceId());

                    if (service.getType().equalsIgnoreCase(service1) && service.getName().equalsIgnoreCase(service2)) {
                        metricUrl = endpoint.getUrl().toString();
                        break;
                    }
                }
            }
        }

        return metricUrl;
    }

    /**
     * connect to openstack
     */
    private OSClient.OSClientV3 connect(String projectName, String userId) throws ConnectException {
        return openStackConnectionService.connect(projectName, userId);
    }

    /**
     * get network (neutron) url
     * 일반 사용자 계정으로 엔드포인트 url 조회가 안돼 keystone url을 편집하여 사용
     */
    private String getNetworkUrl(String projectName, String userId) {
        String url = null;

        // get keystone url
//        url = openStackConnectionService.getKeystoneUrl(projectName, userId);

        // get glance url
        url = url.substring(0, url.lastIndexOf(":")) + ":9696";

        return url;
    }

    /**
     * get web client
     */
    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId) throws SSLException {
        WebClient webClient = null;

        // get network url
        String networkUrl = getNetworkUrl(projectName, userId);

        if (networkUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(networkUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(networkUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }
}
