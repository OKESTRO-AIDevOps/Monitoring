package com.okestro.symphony.dashboard.cmm.websocket.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class WsVo {
    private String dest;
    private String reqType;
    private String noticeType;
    private String data;
    private Object object;
    private String receiver;
    private String idToken;
    private boolean isReloadAt;

}
