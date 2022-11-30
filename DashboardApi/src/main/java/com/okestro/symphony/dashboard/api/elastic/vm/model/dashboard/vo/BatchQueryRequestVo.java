package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class BatchQueryRequestVo {

    private String query;
    private String displayType;
    private String aggregator;
    private Integer numSeries;
    private String order;
    private Time time;
    private Opt opt;

    private String metricName;
    private String dateType;
    private String dateUnit;
    private ArrayList<String> topList;

    public class Time {
        private String liveSpane;
    }

    public class Opt {
        private String provider;
    }

}
