package com.okestro.symphony.dashboard.calltest.svc;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.ProjectVo;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Role;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OpenstackService {


    public List<ProjectVo> list(String projectName, String userId, OpenStackConnectionService openStackConnectionService){
        List<ProjectVo> list = new ArrayList<>();
        String roleId = null;
        boolean isExistNameFlag = true;

        //그룹명 중복 조회(isExistName)에서 현재 list 메서드를 타게 되어있는데 checkProjectUserRole을 타면 중복 조회 되는 속도가 너무 느려서 flag 추가 jh
        if(userId.contains("isExistName")){
            userId = userId.replace("isExistName", "");
            isExistNameFlag = false;
        }

        boolean finalIsExistNameFlag = isExistNameFlag;

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
            WebClient webClient = null;
//            WebClient webClient = getWebClient(osClient,projectName, userId, openStackConnectionService.getKeystoneUrl(projectName, userId));
            // get domain resource
            String domains = webClient.get()
                    .uri("/domains")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();
            JsonObject domainJsonObject = new JsonParser().parse(domains).getAsJsonObject();
            Gson domainGson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            LinkedTreeMap domainTreeMap = domainGson.fromJson(domainJsonObject, LinkedTreeMap.class);

            // get project resource
            String resultData = webClient.get()
                    .uri("/projects")
                    .retrieve()
                    .bodyToMono(String.class)
                    .log()
                    .block();

            JsonObject jsonObject = new JsonParser().parse(resultData).getAsJsonObject();
            Gson gson = new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

            List<? extends Role> roles = osClient.identity().roles().list();

            for (Role role : roles) {
                if(role.getName().equals("admin")){
                    roleId = role.getId();
                }
            }
            String finalRoleId = roleId;

            LinkedTreeMap treeMap = gson.fromJson(jsonObject, LinkedTreeMap.class);

            ((ArrayList<LinkedTreeMap>) treeMap.get("projects")).forEach(project -> {
                ProjectVo vo = new ProjectVo();
                vo.setId((String) project.get("id"));
                vo.setName((String) project.get("name"));
                vo.setDesc((String) project.get("description"));
                vo.setEnabled((Boolean) project.get("enabled"));
                vo.setDomainId((String) project.get("domain_id"));

                ((ArrayList<LinkedTreeMap>) domainTreeMap.get("domains")).forEach(domain -> {
                    if (((String) domain.get("id")).equals(vo.getDomainId())) {
                        vo.setDomainNm((String) domain.get("name"));
                    }
                });

                //그룹 생성, 수정 시 '관리자 계정에 접근 권한 부여' 체크 안했을때
                //checkProjectUserRole이 null 이 나오는데 NullPointException으로 떨어져서 Exception일때 adminRoleBind false 넣어줌 jh
                if(finalIsExistNameFlag){
                    try{
                        if (osClient.identity().roles().checkProjectUserRole((String) project.get("id"), osClient.identity().users().getByName("admin").get(0).getId(), finalRoleId) != null) {
                            if (osClient.identity().roles().checkProjectUserRole((String) project.get("id"), osClient.identity().users().getByName("admin").get(0).getId(), finalRoleId).isSuccess()) {
                                vo.setAdminRoleBind(true);
                            }else{
                                vo.setAdminRoleBind(false);
                            }
                        }
                    }catch (NullPointerException e){
                        vo.setAdminRoleBind(false);
                    }
                }

                list.add(vo);
            });


        } catch (ConnectException e) {
            // log
            log.error(e.getMessage(), e);
        }

        return list;
    }

    private WebClient getWebClient(OSClient.OSClientV3 osClient, String bareMetalUrl) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
//        String bareMetalUrl = openStackConnectionService.getKeystoneUrl(projectName, userId);
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
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(bareMetalUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }


    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId, String bareMetalUrl) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
//        String bareMetalUrl = openStackConnectionService.getKeystoneUrl(projectName, userId);
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
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(bareMetalUrl)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }

    public static OSClient.OSClientV3 projectScopeConnectTestMethod(OpenStackConnectionService openStackConnectionService) throws ConnectException {
//        String endpoint = "http://100.0.0.132:5000/v3";
//        String domain = "Default";
//        String user = "admin_test";
//        String password = "okestro2018";

//        String endpoint = "https://89.98.80.115:5000/v3";
        String endpoint = "http://100.0.0.189:5000/v3";
//        String endpoint = "http://100.0.0.32:5000/v3";
        String domain = "Default";
        String user = "admin";
        String password = "okestro2018";


        OSClient.OSClientV3 os = openStackConnectionService.connect(endpoint, domain,"admin", user, password);

        return os;

    }


    public static OSClient.OSClientV3 UnscopeConnectTestMethod(OpenStackConnectionService openStackConnectionService) throws ConnectException {
//        String endpoint = "http://100.0.0.132:5000/v3";
//        String domain = "Default";
//        String user = "admin_test";
//        String password = "okestro2018";

//        String endpoint = "https://89.98.80.115:5000/v3";
//        String endpoint = "http://100.0.0.245:5000/v3";
        String endpoint = "http://100.0.0.189:5000/v3";
        String domain = "Default";
        String user = "admin";
        String password = "okestro2018";


        OSClient.OSClientV3 os = openStackConnectionService.connectUnscoped(endpoint, domain, user, password);

        return os;

    }
}
