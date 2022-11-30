package com.okestro.symphony.dashboard.util;

import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class OpenstackConUtil {

    @Autowired
    OpenStackConnectionService openStackConnectionService;

    @Value("${config.openstack.endpoint}")
    String endpoint = null;

    @Value("${config.openstack.domain}")
    String domain = null;

    @Value("${config.openstack.user}")
    String user = null;

    @Value("${config.openstack.passwd}")
    String password = null;


    /**
     * openstack API로 Endpoint 리스트 받기.
     */
    private JSONArray openstackCallSvcEndpointList() {

        OSClient.OSClientV3 os = null;
        List<? extends Service> services = null;
        List<? extends Endpoint> endPoints = null;

        List<JSONObject> serviceObjArr = new ArrayList<>();
        JSONObject serviceJsonObj = new JSONObject();
        JSONArray tmpJArr = new JSONArray();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = openStackConnectionService.connectUnscoped(endpoint,domain,user,password);

            // service 리스트
            services = os.identity().serviceEndpoints().list();
            // endPoint 리스트
            endPoints = os.identity().serviceEndpoints().listEndpoints();


            // service 리스트 for문
            for(org.openstack4j.model.identity.v3.Service tmpService : services){
                tmpService = os.identity().serviceEndpoints().get(tmpService.getId());

                //service 객체의 Id 와 Name 을 추출
                serviceJsonObj.put(tmpService.getId(),tmpService.getName());

                log.debug("## Id:"+tmpService.getId());
                log.debug("## Name:"+tmpService.getName());
                log.debug("## Endpoint:"+tmpService.getEndpoints());
            }

            // endpoint 리스트 for문
            for(Endpoint tmpEndpoint : endPoints){
                Iterator iter = serviceJsonObj.keys();
                JSONObject svcJSONObj = new JSONObject();
                while (iter.hasNext()){
                    if(iter.next().toString().equals(tmpEndpoint.getServiceId())){
                        log.debug("## Y_TEST Service id exist! ["+tmpEndpoint.getServiceId()+"]");
                        log.debug("## Y_TEST Service Name! ["+serviceJsonObj.get(tmpEndpoint.getServiceId())+"]");
                        log.debug("## Y_TEST Service url is ["+tmpEndpoint.getUrl()+"]");
                        svcJSONObj.put("ID",tmpEndpoint.getServiceId());
                        svcJSONObj.put("NAME",serviceJsonObj.get(tmpEndpoint.getServiceId()));
                        svcJSONObj.put("END_POINT",tmpEndpoint.getUrl());

                        serviceObjArr.add(svcJSONObj);
                    }
                }
            }

            String jsonStr = serviceObjArr.toString();
            tmpJArr = new JSONArray(jsonStr);


            log.debug("========Response Convert Json================");
            log.debug("gson Result["+tmpJArr.toString(1)+"]");


        }catch (NullPointerException | ConnectException | JSONException e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }finally {
            return tmpJArr;
        }

    }
}
