package com.okestro.symphony.dashboard.cmm.audit;

import com.okestro.symphony.dashboard.cmm.model.AuditVo;
import com.okestro.symphony.dashboard.util.DateForm;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Service
public class AuditLogService {

    @Autowired
    @Qualifier("webClient")
    private WebClient webClient;

    @Value("${platform.audit.admin.api.url}")
    private String baseAdminUrl;

    private String auditLogUri = "/audit/saveAuditLog";

    @Async
    public void sendLog(String se, String prjctNm, String tenNm, String cn, String userId){
        AuditVo vo = new AuditVo(se, prjctNm, tenNm, cn, userId);
        vo.setCreatDt(DateForm.getCurrentDateTime());



        WebClient client = webClient.mutate().build();
        if(baseAdminUrl.startsWith("https:")){
            HttpClient httpClient = null;
            try{
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
                httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));
                client = webClient.mutate().clientConnector(new ReactorClientHttpConnector(httpClient)).build();
            }catch(SSLException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }

        String result = client.mutate()
                .baseUrl(baseAdminUrl)
                .build()
                .post()
                .uri(auditLogUri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(vo)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        log.debug("[result : " + result.toString() + "],  send data : " + vo.toString());
    }
}
