package com.okestro.symphony.dashboard.api.dashboard.model;

import lombok.Data;

import java.util.List;

@Data
public class MenuInfo {
    private String id;

    private List<Items> items;

    @Data
    public class Items {
        private String key;

        private String value;
    }
}
