package com.okestro.symphony.dashboard.cmm.websocket.svc;

import com.okestro.symphony.dashboard.cmm.websocket.config.WsDestType;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsNoticeLevel;
import com.okestro.symphony.dashboard.cmm.websocket.config.WsRequestAndLevel;
import com.okestro.symphony.dashboard.cmm.websocket.model.WsVo;

public interface WebsocketService {
    /**
     * 웹소켓 메시지 날리는 부분
     */
    public void sendNotice(WsRequestAndLevel reqType, String message, String receiver);
    public void sendNotice(WsNoticeLevel noticeLevel, String message, String receiver);
    public void sendReload(WsDestType dest, String status, String receiver);
    public void sendMessage(WsVo msg);
    public void sendStatusMessage(WsVo msg, String destination);

    /**
     * Redis 를 통해서 웹소켓 메시지 날리는 부분
     */
//    public void redisPreReqNotice(WsRequestAndLevel reqType, String message, String token);
//    public void redisSendNotice(WsRequestAndLevel noticeLevel, String message, boolean isReloadAt,  WsDestType dest, String idToken);
//    public void redisSendMsg(WsVo msg);
}
