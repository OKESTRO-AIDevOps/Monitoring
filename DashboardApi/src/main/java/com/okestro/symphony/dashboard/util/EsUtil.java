package com.okestro.symphony.dashboard.util;

import com.okestro.symphony.dashboard.cmm.constant.ElasticSearchConstant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class EsUtil {

    public String getIdxName (String metricType){
        String result = null;

        switch (metricType){
            // ------meta Data------
            case ElasticSearchConstant.META_HYPERVISOR : result = "ost-metric-hypervisor-"; break;

            case ElasticSearchConstant.META_HOST : result = "ost-metric-hosts-";  break;

            case ElasticSearchConstant.META_PROJECT : result = "ost-metric-projects-";  break;

            case ElasticSearchConstant.META_PROJECT_DETAIL : result = "ost-metric-projects-detail-";  break;

            // ------aggregation Data------
            case ElasticSearchConstant.PROJECT_METRIC_CPU : result = "ost-metric-projects-detail-";  break;

            case ElasticSearchConstant.PROJECT_METRIC_RAM : result = "ost-metric-projects-detail-";  break;

            case ElasticSearchConstant.PROJECT_METRIC_DISK : result = "ost-metric-projects-detail-";  break;

            case ElasticSearchConstant.PROJECT_METRIC_NETWORK : result = "ost-metric-projects-detail-";  break;


            default: result = "ost-metric-hypervisors";  break;
        }
        return result;
    }

    public SearchResponse selectMetricToEs(RestHighLevelClient esClient , String indexNm, String sortField, String sortType, String gte, String lte){
        SearchResponse response = null;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",indexNm);
        List<FieldSortBuilder> sorts = new ArrayList<>();
        sorts.add(new FieldSortBuilder("@timestamp").order(new FieldSortBuilder("@timestamp").order().ASC));
        if(sortType.equalsIgnoreCase("asc")){
            sorts.add(new FieldSortBuilder(sortField).order(new FieldSortBuilder(sortField).order().ASC));
        }else{
            log.debug("## setting desc");
            sorts.add(new FieldSortBuilder(sortField).order(new FieldSortBuilder(sortField).order()));
        }

        log.debug("## Y_TEST sortField["+sortField+"]");
        log.debug("## Y_TEST sortType["+sortType+"]");

        QueryBuilder query = QueryBuilders.boolQuery()
                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));
        try{                                                            // from to 시간안에서 tophits로 가장 최근 값을 기준으로 뽑아냄.
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("test_aggs_terms").field("project_detail_info.project_name.keyword").size(1000)
                                                    .subAggregation(
                                                            AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp")
                                                    ))
                                    .size(0).sort(sorts.get(1))
                                    .trackTotalHits(true)
//									.sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);
            log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            JSONObject resultJson = new JSONObject(response.toString());
            log.debug("## elasticsearch response: ["+resultJson.toString(1)+"] toString");
            String resultStr = resultJson.toString(1);

        }catch (Exception e){
            log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return response;
    }

    public SearchResponse selectMetricToEs(RestHighLevelClient esClient , String indexNm, String sortField, String sortType, long gte, long lte){
        SearchResponse response = null;
//        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",indexNm);
        List<FieldSortBuilder> sorts = new ArrayList<>();
        sorts.add(new FieldSortBuilder("@timestamp").order(new FieldSortBuilder("@timestamp").order().ASC));
        if(sortType.equalsIgnoreCase("asc")){
            sorts.add(new FieldSortBuilder(sortField).order(new FieldSortBuilder(sortField).order().ASC));
        }else{
            log.debug("## setting desc");
            sorts.add(new FieldSortBuilder(sortField).order(new FieldSortBuilder(sortField).order()));
        }

        log.debug("## Y_TEST sortField["+sortField+"]");
        log.debug("## Y_TEST sortType["+sortType+"]");

        QueryBuilder query = QueryBuilders.boolQuery()
//                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));
        try{                                                            // from to 시간안에서 tophits로 가장 최근 값을 기준으로 뽑아냄.
            response = esClient.search(new SearchRequest(indexNm)
                    .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("test_aggs_terms").field("project_detail_info.project_name.keyword").size(1000)
                                                    .subAggregation(
                                                            AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp")
                                                    ))
                                    .size(0).sort(sorts.get(1))
                                    .trackTotalHits(true)
//									.sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);
            log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            JSONObject resultJson = new JSONObject(response.toString());
            log.debug("## elasticsearch response: ["+resultJson.toString(1)+"] toString");
            String resultStr = resultJson.toString(1);

        }catch (Exception e){
            log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return response;
    }

    public SearchResponse selectMetricToEs(RestHighLevelClient esClient , String indexNm, String gte, String lte){
        SearchResponse response = null;
//        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",indexNm);
        List<FieldSortBuilder> sorts = new ArrayList<>();
        sorts.add(new FieldSortBuilder("@timestamp").order(new FieldSortBuilder("@timestamp").order().ASC));



        QueryBuilder query = QueryBuilders.boolQuery()
//                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));
        try{                                                            // from to 시간안에서 tophits로 가장 최근 값을 기준으로 뽑아냄.
            response = esClient.search(new SearchRequest(indexNm)
                    .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("test_aggs_terms").field("project_detail_info.project_name.keyword").size(1000)
                                                    .subAggregation(
                                                            AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp")
                                                    ))
                                    .size(0)
                                    .trackTotalHits(true)
//									.sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);
            log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            JSONObject resultJson = new JSONObject(response.toString());
            log.debug("## elasticsearch response: ["+resultJson.toString(1)+"] toString");
            String resultStr = resultJson.toString(1);

        }catch (Exception e){
            log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return response;
    }

    public SearchResponse selectMetricToEs(RestHighLevelClient esClient , String indexNm, long gte, long lte){
        SearchResponse response = null;
//        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",indexNm);
        List<FieldSortBuilder> sorts = new ArrayList<>();
        sorts.add(new FieldSortBuilder("@timestamp").order(new FieldSortBuilder("@timestamp").order().ASC));



        QueryBuilder query = QueryBuilders.boolQuery()
//                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));
        try{                                                            // from to 시간안에서 tophits로 가장 최근 값을 기준으로 뽑아냄.
            response = esClient.search(new SearchRequest(indexNm)
                    .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("test_aggs_terms").field("project_detail_info.project_name.keyword").size(1000)
                                                    .subAggregation(
                                                            AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp")
                                                    ))
                                    .size(0)
                                    .trackTotalHits(true)
//									.sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);
            log.debug("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            JSONObject resultJson = new JSONObject(response.toString());
            log.debug("## elasticsearch response: ["+resultJson.toString(1)+"] toString");
            String resultStr = resultJson.toString(1);

        }catch (Exception e){
            log.debug("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        return response;
    }
}
