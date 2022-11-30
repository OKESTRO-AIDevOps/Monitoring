package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GraphRequestVo {

    private String metricName;
    private String dateType;
    private String dateUnit;
    private ArrayList<String> instanceTopList;

}
