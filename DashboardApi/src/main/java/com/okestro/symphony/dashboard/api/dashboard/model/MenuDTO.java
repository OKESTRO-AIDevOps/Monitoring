package com.okestro.symphony.dashboard.api.dashboard.model;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private String type;

    private String id;

    private List<MenuInfo> list;
}
