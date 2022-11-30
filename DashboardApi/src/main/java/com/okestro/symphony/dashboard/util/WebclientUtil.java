package com.okestro.symphony.dashboard.util;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.Service;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class WebclientUtil {

    /**
     * 오픈스택 API에서 조회한 데이터를 endPointUrl을 통해 정보를 조회할 Webclient 객체 초기화.
     * @param endPointUrl
     * @return
     * @throws SSLException
     */
    public WebClient getWebClient(String endPointUrl) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
//        String bareMetalUrl = openStackConnectionService.getKeystoneUrl(projectName, userId);
        if (endPointUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

//            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50)).build(); // set max memory size 1MB;

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .exchangeStrategies(exchangeStrategies) // set exchange strategies
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }

    /**
     * 오픈스택 V3 API client 객체, 프로젝트명, 유저명, endPointUrl을 통해 정보를 조회할 Webclient 객체 초기화.
     * ※ 해당 endpoin는 horizen 또는 공식 openstack DOC 사이트에서 참고.
     * @param osClient
     * @param projectName
     * @param userId
     * @param endPointUrl
     * @return
     * @throws SSLException
     */
    public WebClient getWebClient(OSClient.OSClientV3 osClient, String projectName, String userId, String endPointUrl) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
//        String bareMetalUrl = openStackConnectionService.getKeystoneUrl(projectName, userId);
        if (endPointUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }

    /**
     * 오픈스택 V3 API client 객체, 프로젝트명, 유저명, endPointUrl을 통해 정보를 조회할 Webclient 객체 초기화.
     * ※ 해당 endpoin는 horizen 또는 공식 openstack DOC 사이트에서 참고.
     * @param osClient
     * @param endPointUrl
     * @return
     * @throws SSLException
     */
    public WebClient getWebClient(OSClient.OSClientV3 osClient, String endPointUrl) throws SSLException {
        WebClient webClient = null;

        // get baremetal url
//        String bareMetalUrl = openStackConnectionService.getKeystoneUrl(projectName, userId);
        if (endPointUrl.startsWith("https://")) {
            // set insecure
            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

//            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50)).build(); // set max memory size 1MB;

            webClient = WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
//                    .exchangeStrategies(exchangeStrategies) // set exchange strategies
                    .build();
        } else {
            webClient = WebClient.builder()
                    .baseUrl(endPointUrl)
                    .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", osClient.getToken().getId())
                    .build();
        }

        return webClient;
    }


}
