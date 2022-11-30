/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.svc.impl;


import com.okestro.symphony.dashboard.cmm.audit.AuditLogService;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsDestType;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsRequestAndLevel;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClientException;

import java.net.URISyntaxException;

@Slf4j
public abstract class CommonAbstrctServiceImpl {
    @Autowired
    AuditLogService auditLogService;

    @Autowired
    WebsocketService websocketService;

    protected void commonSuccess (String logType, String pjtNm, String pjt, String msg, String userId,
                                  WsDestType destType, WsRequestAndLevel wsRequestAndLevel){
        // 감사로그 저장
        auditLogService.sendLog(logType, pjtNm, pjt, msg, userId);
        // 화면에 성공 notice 띄우기
        websocketService.sendNotice(wsRequestAndLevel, msg, userId);
        // 목록 동기화 부분
        websocketService.sendReload(destType, wsRequestAndLevel.getName(), userId);

    }

    protected  void commonFail(String logType, String pjtNm, String pjt, String msg, String userId,
                               WsDestType destType, WsRequestAndLevel wsRequestAndLevel, String errorMsg){

        // 에러 로그 저장
        log.error(errorMsg);
        // 감사로그 저장
        auditLogService.sendLog(logType, pjtNm, pjt, msg, userId);
        // 화면에 실패 notice 띄우기
        websocketService.sendNotice(wsRequestAndLevel, msg, userId);
        // 목록 동기화 부분
        websocketService.sendReload(destType, wsRequestAndLevel.getName(), userId);

//        if(e.getClass().equals(ClientResponseException.class)){
//            clientResponseException();
//        } else if(e.getClass().equals(WebClientException.class)){
//            webClientException();
//        } else if(e.getClass().equals(NullPointerException.class)){
//            nullPointerException();
//        } else if(e.getClass().equals(RegionEndpointNotFoundException.class)){
//            regionEndpointNotFoundException();
//        } else if(e.getClass().equals(OS4JException.class)){
//            os4JException();
//        } else if(e.getClass().equals(RuntimeException.class)){
//            runtimeException();
//        } else if(e.getClass().equals(ConnectionException.class)){
//            connectionException();
//        } else if(e.getClass().equals(IllegalArgumentException.class)){
//            illegalArgumentException();
//        } else if(e.getClass().equals(AuthenticationException.class)){
//            authenticationException();
//        } else if(e.getClass().equals(URISyntaxException.class)){
//            uriSyntaxException();
//        }

    }

    /**
     * ClientResponseException
     * @soucrce NetworkingServiceImpl, ...
     * @param
     * @return clientResponseException
     *
     * Captures Server based Errors (Return Codes between 400 - 499)
     * "Unable to retrieve current session. Please verify thread has a current session available."
     */
    protected ClientResponseException clientResponseException(){
        return clientResponseException();
    }

    /**
     * WebClientException
     * @soucrce
     * @param
     * @return webClientException
     *
     * Abstract base class for exception published by WebClient in case of errors.
     */
    protected WebClientException webClientException(){
        return webClientException();
    }

    /**
     * NullPointerException
     * @soucrce
     * @param
     * @return nullPointerException
     *
     * Thrown when an application attempts to use null in a case where an object is required. These include:
     * Calling the instance method of a null object.
     * Accessing or modifying the field of a null object.
     * Taking the length of null as if it were an array.
     * Accessing or modifying the slots of null as if it were an array.
     * Throwing null as if it were a Throwable value.
     * Applications should throw instances of this class to indicate other illegal uses of the null object.
     */
    protected NullPointerException nullPointerException(){
        return nullPointerException();
    }

    /**
     * RegionEndpointNotFoundException
     * @Source OSClient
     * @param
     * @return regionEndpointNotFoundException
     *
     * Exception that is thrown when a Service Endpoint cannot be found for the user specified Region
     */
    protected RegionEndpointNotFoundException regionEndpointNotFoundException(){
        return regionEndpointNotFoundException();
    }

    /**
     * OS4JException
     * @soucrce BaseOpenStackService
     * @param
     * @return os4JException
     *
     * "Unable to retrieve current session. Please verify thread has a current session available."
     */
    protected OS4JException os4JException(){
        return os4JException();
    }

    /**
     * RuntimeException
     * @soucrce
     * @param
     * @return runtimeException
     *
     * RuntimeException is the superclass of those exceptions that can be thrown during the normal operation of the Java Virtual Machine.
     * RuntimeException and its subclasses are unchecked exceptions.
     * Unchecked exceptions do not need to be declared in a method or constructor's throws clause
     * if they can be thrown by the execution of the method or constructor and
     * propagate outside the method or constructor boundary.
     */
    protected RuntimeException runtimeException(){
        return runtimeException();
    }

    /**
     * ConnectionException
     * @soucrce OpenStackConnectionService
     * @param
     * @return connectionException
     *
     * An exception which is thrown when a connection/IO exception has occurred with the remote host
     * Signals that an error occurred while attempting to connect a socket to a remote address and port.
     * Typically, the connection was refused remotely (e.g., no process is listening on the remote address/port).
     */
    protected ConnectionException connectionException(){
        return connectionException();
    }

    /**
     * OS4JException
     * @source BaseOpenStackService
     * @param
     * @return os4JException
     *
     * Thrown to indicate that a method has been passed an illegal or inappropriate argument.
     */
    protected IllegalArgumentException illegalArgumentException(){
        return illegalArgumentException();
    }

    /**
     * AuthenticationException
     * @source OSClientBuilder
     * @param
     * @return authenticationException
     *
     * An exception that is thrown when Credentials failed or the default tenant is invalid
     */
    protected AuthenticationException authenticationException(){
        return authenticationException();
    }

    /**
     * URISyntaxException
     * @source OSClientSession
     * @param
     * @return uriSyntaxException
     *
     * Checked exception thrown to indicate that a string could not be parsed as a URI reference.
     */
    protected URISyntaxException uriSyntaxException(){
        return uriSyntaxException();
    }

}
