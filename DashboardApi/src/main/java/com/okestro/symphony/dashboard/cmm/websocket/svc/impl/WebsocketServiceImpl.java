package com.okestro.symphony.dashboard.cmm.websocket.svc.impl;

import com.google.gson.Gson;
import com.okestro.symphony.dashboard.cmm.websocket.svc.WebsocketService;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsDestType;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsNoticeLevel;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsRequestAndLevel;
import com.okestro.symphony.dashboard.cmm.websocket.model.WsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;


@Component
public class WebsocketServiceImpl implements WebsocketService {

    @Autowired
    private SimpMessagingTemplate template;

    private final static String DEST = "/topic/openstackMessage";

    /**
     * request 메시지를 전송
     * @param reqType	CREATING, UPDATING, DELETING 등
     * @param message	전송할 알림 메시지
     * @param receiver	메시지 수신자
     */
    public void sendNotice(WsRequestAndLevel reqType, String message, String receiver){
        WsVo vo = new WsVo();
//        vo.setType(WsType.NOTICE);
        vo.setReqType(reqType.getName());
        vo.setData(message);
        vo.setReceiver(receiver);

        sendMessage(vo);
    }

    /**
     * alram 메시지를 전송
     * @param noticeLevel	INFO, WARNING, ERROR 등
     * @param message	전송할 알림 메시지
     * @param receiver	메시지 수신자
     */
    public void sendNotice(WsNoticeLevel noticeLevel, String message, String receiver){
        WsVo vo = new WsVo();
//        vo.setType(WsType.NOTICE);
        vo.setNoticeType(noticeLevel.getName());
        vo.setData(message);
        vo.setReceiver(receiver);

        sendMessage(vo);
    }

    /**
     * 변경된 정보를 다시 읽도록 reload 메시지를 전송
     * @param dest	reload 할 대상(PROJECT, VM, NETWORK 등)
     * @param status	reload 시 필요한 정보를 전송(없을시 null, 상태가 변경일 때만 사용)
     * @param receiver	메시지 수신자
     */
    public void sendReload(WsDestType dest, String status, String receiver){
        WsVo vo = new WsVo();
//        vo.setType(WsType.RELOAD);
//        vo.setDest(dest.name());
        vo.setData(status);
        vo.setReceiver(receiver);

        sendStatusMessage(vo ,dest.name());
    }

    /**
     * 웹소켓 클라이언트에게 메시지를 전송
     * destination으로 subscription 하는 대상 전체에게 메시지(message)를 전송한다.
     * @param msg	sendMessage
     */
    public void sendMessage(WsVo msg) {
        this.template.convertAndSend(this.DEST, new Gson().toJson(msg, WsVo.class));
//		this.template.convertAndSend("/topic/test", message);
    }

    /**
     * 웹소켓 클라이언트에게 상태 전송
     * destination으로 subscription 하는 한 대상(VM,ROUTER 등)에게 메시지(message)를 전송한다.
     * @param msg	sendMessage
     * @param destination 메시지를 받을 대상
     */
    public void sendStatusMessage(WsVo msg, String destination){
        String DEST = "/topic/"+destination.toLowerCase();
        this.template.convertAndSend(DEST, new Gson().toJson(msg, WsVo.class));
    }

//    public void sendMessage(String msg) {
//        this.template.convertAndSend(this.DEST, msg);
//		this.template.convertAndSend("/topic/test", message);
//    }
}