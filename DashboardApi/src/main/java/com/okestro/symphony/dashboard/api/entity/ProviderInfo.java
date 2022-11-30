package com.okestro.symphony.dashboard.api.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderInfo {
    private String id;

    private String name;

    private String type;

    private String url;

    private String domain;

    private String project;

    private String user;

    private String pwd;

    private String agentId;
}
