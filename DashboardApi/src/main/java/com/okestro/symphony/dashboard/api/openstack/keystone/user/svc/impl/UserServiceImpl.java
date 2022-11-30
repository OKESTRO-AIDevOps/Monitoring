/*
 * Developed by ygchoi on 2019-06-12.
 * Last modified 2019-06-12 00:13:01.
 * Copyright (c) 2019 OKESTRO. All rights reserved.
 */

package com.okestro.symphony.dashboard.api.openstack.keystone.user.svc.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;

import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.OstackUserVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.UserProjectVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.user.svc.UserService;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Role;
import org.openstack4j.model.identity.v3.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
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
public class UserServiceImpl implements UserService {
    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Autowired
    WebsocketService websocketService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuditLogService auditLogService;


    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;


    /**
     * admin권한으로 모든 유저 리스트 조회.
     * @return String(JsonStr)
     */
    @Override
    public String listAllUser(){
        String resultStr = null;

        List<OstackUserVo> list = new ArrayList<>();

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);

            // get user
            List<? extends User> users = osClient.identity().users().list();

            for (User item : users) {
                OstackUserVo vo = new OstackUserVo();

                vo.setName(item.getName());
                vo.setId(item.getId());
                vo.setEmail(item.getEmail());
                vo.setDomainId(item.getDomainId());

                UserProjectVo projectVo = new UserProjectVo();
                projectVo.setId(item.getDefaultProjectId());
                vo.setUserProjectVo(projectVo);
//                vo.setProvider(providerVo);

                vo.setPassword(item.getPassword());
                vo.setEnabled(item.isEnabled());
                vo.setDesc(item.getDescription());

                list.add(vo);
            }

            Gson gson = new Gson();
            try {
                String gsonStr = gson.toJson(list);
                JSONArray tmpJArr = new JSONArray(gsonStr);
                resultStr = tmpJArr.toString(1);
            }catch (JSONException e){
                log.error(e.getMessage(),e);
            }

        } catch (ConnectException e) {
            log.error("## Exception 발생. ["+e.getMessage()+"]", e);
            e.printStackTrace();
        }

        return resultStr;
    }

    @Override
    public List<OstackUserVo> list(String projectName, String userId){
        List<OstackUserVo> list = new ArrayList<>();

        try {
            // connect to openstack
            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);

            // get user
            List<? extends User> users = osClient.identity().users().list();

            for (User item : users) {
                OstackUserVo vo = new OstackUserVo();

                vo.setName(item.getName());
                vo.setId(item.getId());
                vo.setEmail(item.getEmail());
                vo.setDomainId(item.getDomainId());

                UserProjectVo projectVo = new UserProjectVo();
                projectVo.setId(item.getDefaultProjectId());
                vo.setUserProjectVo(projectVo);
//                vo.setProvider(providerVo);

                vo.setPassword(item.getPassword());
                vo.setEnabled(item.isEnabled());
                vo.setDesc(item.getDescription());


                list.add(vo);
            }
        } catch (ConnectException e) {
            // log
            log.error(e.getMessage(), e);
        }

        return list;
    }


//    @Override
//    public List<UserProjectVo> projectList(String projectName, String userId) {
//        List<UserProjectVo>  resultPrj = new ArrayList<>();
//
//        try {
//            // get openstack client
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
//            WebClient webClient = getWebClient(osClient, projectName, userId);
//
//            // get resource
//            String resultData = webClient.get()
//                    .uri("/v3/projects")
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .log()
//                    .block();
//
//            JsonObject jsonObject = new JsonParser().parse(resultData).getAsJsonObject();
//
//            for(int i=0; i < jsonObject.get("projects").getAsJsonArray().size(); i++) {
//                UserProjectVo projectVo = new UserProjectVo();
//                projectVo.setId(jsonObject.get("projects").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString());
//                projectVo.setName(jsonObject.get("projects").getAsJsonArray().get(i).getAsJsonObject().get("name").getAsString());
//                resultPrj.add(projectVo);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return resultPrj;
//    }

//    @Override
//    public List<? extends Role> roleList(String projectName, String userId) {
////        List<RoleVo> roles = new ArrayList<>();
//        List<? extends Role> roles = null;
//
//        try {
//            // get openstack client
//            OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectName, userId);
//
//            roles = osClient.identity().roles().list();
//
////            osClient.identity().roles().list().forEach(role -> {
////                roles.add(role);
////            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return roles;
//    }



    /**
     * get web client
     */
    private WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId) throws SSLException {
        WebClient webClient;

        // get Heat url
        String HeatIpUrl = getHeatUrl(projectName, userId);

        if (HeatIpUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(HeatIpUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(HeatIpUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }

    /**
     * get baremetal(ironic) url
     * 일반 사용자 계정으로 엔드포인트 url 조회가 안돼 keystone url을 편집하여 사용
     */
    private String getHeatUrl(String projectName, String userId) {
        String url = null;

        // get keystone url
//        url = openStackConnectionService.getKeystoneUrl(projectName, userId);

        // get glance url
//        url = url.substring(0, url.lastIndexOf(":")) + ":6385";
        url = url.substring(0, url.lastIndexOf(":")) + ":5000";

        return url;
    }

}