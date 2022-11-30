package com.okestro.symphony.dashboard.cmm.websocket.config;

public enum WsNoticeLevel {
    WARNING("warning"),
    ERROR("error"),
    SUCCESS("success"),
    INFO("info");

    private String name;

    public String getName() {
        return name;
    }

    private WsNoticeLevel(String name){
        this.name = name;
    }
}