/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

// 지난 주 대비 인스턴스별 자원 사용률
@Data
public class OstInstanceGraphAggregatedVo {

    private String id;

    private String aggregator;
    @JsonProperty("numSeries")
    private Integer numSeries;
    private String order;
    @JsonProperty("displayType")
    private String displayType;
    private String query;
    private String timespan;
    private String metric;
    private Scope scope;
    private String group;
    private ArrayList<ArrayList<Double>> values;
    private ArrayList<ArrayList<Long>> times;
    private ArrayList<String> series;


    public class Scope {
        private String provider;
    }


}
