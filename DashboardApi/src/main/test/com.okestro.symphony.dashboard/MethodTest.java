package com.okestro.symphony.dashboard;

import com.google.gson.Gson;
import com.okestro.symphony.dashboard.config.elastic.RestClientConfiguration;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.pipeline.BucketScriptPipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodTest {

    RestClientConfiguration restClientConfiguration = new RestClientConfiguration();
    private final RestHighLevelClient esClient = elasticsearchClient();
    QueryBuilder tmpQuryBuilder;



    public RestHighLevelClient elasticsearchClient() {

        //@ TODO-useSsl, withBasicAuth
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
//                .withBasicAuth("elastic","okestro2018")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    /**
     * ES에 aggs 쿼리 조회 테스트.
     */
    @Test
    public void aggSearch() throws JSONException {
        Map<String, String> bucketsPathsMap = new HashMap<String, String>();
        bucketsPathsMap.put("total_quota_avg","total_quota_avg");
        bucketsPathsMap.put("total_usage_avg","total_usage_avg");

        Script script = new Script("params.total_quota_avg - params.total_usage_avg");

        SearchResponse response = null;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index","ost-metric-projects-detail-*");
        PipelineAggregatorBuilders.bucketScript("tmp_pct",bucketsPathsMap,script);
        List<FieldSortBuilder> sorts = new ArrayList<>();
        sorts.add(new FieldSortBuilder("tmp_pct").order(new FieldSortBuilder("tmp_pct").order().ASC));

        QueryBuilder query = QueryBuilders.boolQuery()
                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("now-7d/d")
                        .lte("now/d"));

        try{
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                            .query(query)
                            .aggregation(
                                    AggregationBuilders.terms("tmp_term_test_result").field("project_detail_info.project_name.keyword").size(1000)
                                            .subAggregation(
                                                    AggregationBuilders.avg("total_quota_avg")
                                                            .field("project_detail_info.project_quota_set.cores_quota")
                                              ).subAggregation(
                                            AggregationBuilders.avg("total_usage_avg")
                                                    .field("project_detail_info.project_usage.cores_usage")
                                    ).subAggregation(
                                            PipelineAggregatorBuilders.bucketScript("tmp_pct",bucketsPathsMap,script)
                                    ).subAggregation(
                                            PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
                                    ))
                            .size(0)
                            .trackTotalHits(true)
                            .sort(SortBuilders.scoreSort())
//                            .sort(SortBuilders.fieldSort("tmp_pct"))
                    ), RequestOptions.DEFAULT);
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }

        System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
//        Gson gson = new Gson();
//        String gsonStr = gson.toJson(response.toString());
//        JSONObject tmpJobj = null;
        JSONObject tmpJobj = new JSONObject(response.toString());
        System.out.println("## elasticsearch response: {"+tmpJobj.toString(1)+"} toString");

    }




    /**
     * ES에 그래프관련 aggs  쿼리 조회 테스트.

     */
    @Test
    public void graphAggSearch() {

        SearchResponse response = null;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index","ost-metric-projects-detail-*");
        QueryBuilder query = QueryBuilders.boolQuery()
                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("now-7d/d")
                        .lte("now/d"));

        try{
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                            .query(query)
                            .aggregation(
                                    AggregationBuilders.terms("tmp_term_test_result").field("project_detail_info.project_name.keyword").size(1000)
                                            .subAggregation(AggregationBuilders.dateHistogram("by_year")
                                                    .field("dateOfBirth")
                                                    .dateHistogramInterval(DateHistogramInterval.days(3652))
                                                    .extendedBounds(new ExtendedBounds(1940L, 2009L))
                                                    .format("YYYY")
                                                    .subAggregation(AggregationBuilders.avg("avg_children").field("children"))
                                            )
                            )
                            .aggregation(
                                    AggregationBuilders.dateHistogram("by_year")
                                            .field("dateOfBirth")
                                            .dateHistogramInterval(DateHistogramInterval.YEAR)
                                            .extendedBounds(new ExtendedBounds(1940L, 2009L))
                                            .format("YYYY")
                            )
//                            .from(from)
                            .size(0)
                            .trackTotalHits(true)
                            .sort(SortBuilders.scoreSort())
                            .sort(SortBuilders.fieldSort("dateOfBirth"))
                    ), RequestOptions.DEFAULT);
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }

        System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
        System.out.println("## elasticsearch response: {"+response.toString()+"} toString");

    }

}
