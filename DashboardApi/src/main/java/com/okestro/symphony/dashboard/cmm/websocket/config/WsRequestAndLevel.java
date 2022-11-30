package com.okestro.symphony.dashboard.cmm.websocket.config;

public enum WsRequestAndLevel {
    CREATING("creating"),
    UPDATING("updating"),
    DELETING("deleting"),
    CONNECTING("connecting"),
    DISCONNECTING("disconnecting"),
    SUSPENDING("suspending"),
    STATUS_CHANGING("status_changing"),
    STARTING("starting"),

    WARNING("warning"),
    ERROR("error"),
    SUCCESS("success"),
    INFO("info"),

    CREATED("created"),
    UPDATED("updated"),
    DELETED("deleted"),
    CONNECTED("connected"),
    DISCONNECTED("disconnected"),
    SUSPENDED("suspended"),
    STATUS_CHANGED("status_changed"),
    STARTED("started"),
    ;


    private String name;

    public String getName() {
        return name;
    }

    private WsRequestAndLevel(String name){
        this.name = name;
    }
}