package com.okestro.symphony.dashboard;

import Vo.ProjectMetricDetailVo;
import Vo.ProjectMetricVo;
import com.google.gson.*;
import com.okestro.symphony.dashboard.api.elastic.vm.model.ElasticIndex;
import com.okestro.symphony.dashboard.api.elastic.vm.model.dashboard.vo.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostAggregateEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostAggregateHostEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity.HostEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.*;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity.ProjectQuotaEntity;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.HostAggregateMetricVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.HypervisorMetricVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ServerMetricVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VolumeVo;
import com.okestro.symphony.dashboard.cmm.constant.OpenStackConstant;
import com.okestro.symphony.dashboard.cmm.engine.OpenStackConnectionService;
import com.okestro.symphony.dashboard.api.openstack.baremetal.model.NodeVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.HostVo;
import com.okestro.symphony.dashboard.api.openstack.keystone.instancetype.model.FlavorVo;
//import com.okestro.symphony.dashboard.api.openstack.keystone.user.model.UserVo;
import com.okestro.symphony.dashboard.api.openstack.dashboard.model.SummaryVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.SecGroupVo;
import com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.VmVo;
import com.okestro.symphony.dashboard.util.DateUtil;
import com.okestro.symphony.dashboard.calltest.svc.OpenstackService;

import com.okestro.symphony.dashboard.util.WebclientUtil;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedAvg;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.aggregations.pipeline.SimpleValue;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import org.openstack4j.api.OSClient;
import org.openstack4j.api.compute.ComputeService;
import org.openstack4j.api.compute.ServerService;
import org.openstack4j.api.networking.NetworkingService;
import org.openstack4j.api.storage.BlockStorageService;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.ext.AvailabilityZone;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.model.identity.v3.*;

import org.openstack4j.model.network.NetQuota;
import org.openstack4j.model.storage.block.BlockQuotaSet;
import org.openstack4j.model.storage.block.BlockQuotaSetUsage;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Slf4j
public class DashboardApiTest {

//    public static void main(String[] args) {
//        SpringApplication.run(DashboardApi.class, args);
//    }


    OpenStackConnectionService openStackConnectionService = new OpenStackConnectionService();

//    RestClientConfiguration restClientConfiguration = new RestClientConfiguration();
//    private final RestHighLevelClient esClient = restClientConfiguration.elasticsearchClient();


    // 하이퍼바이저 Top 5 cpu,memory 사용률 조회.
    @Test
    public void retrueveGraphHypervisorMetricTest(){
        RestHighLevelClient esClient = elasticsearchClient();
        ElasticIndex index = new ElasticIndex();
        OstHypervisorGraphAggregatedVo graphAggregatedVo = new OstHypervisorGraphAggregatedVo();
        OstHypervisorAggregatedVo aggregatedVo = new OstHypervisorAggregatedVo();
        List<OstHypervisorAggregatedVo> voList = new ArrayList<OstHypervisorAggregatedVo>();

        ArrayList<ArrayList<Long>> timesList = new ArrayList<>();
        ArrayList<ArrayList<Double>> valuesList = new ArrayList<>();

        ArrayList<String> hostTopList = new ArrayList<>();
        hostTopList.add("contrabass01");
        hostTopList.add("contrabass02");
        hostTopList.add("contrabass03");
        hostTopList.add("contrabass04");
        hostTopList.add("contrabass05");

        System.out.println("## Y_TEST timesList.Size["+timesList.size()+"]");
        System.out.println("## Y_TEST valuesList.Size["+valuesList.size()+"]");

        ArrayList<Long> timeArr  = new ArrayList<>();
        ArrayList<Double> valueArr = new ArrayList<>();
        ArrayList<String> seriesArr = new ArrayList<>();

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Date date = null;
        String gte = null;
        String lte = null;
        String metricName = "cpu";
        String dateType = "day";
        String dateUnit = "1";
        DateHistogramInterval interval = null;


        if(dateType.equalsIgnoreCase("day") || dateType.equalsIgnoreCase("d")){
            gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간
            lte = "now/d";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
        }else if(dateType.equalsIgnoreCase("hour") || dateType.equalsIgnoreCase("h")){
            gte = "now-"+dateUnit+"h/h";																// date 타입기준 입력받은 dateUnit만큼 전 시간
            lte = "now/h";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
        }else{
            gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간, 입력안받았으면 기본 1
            lte = "now/d";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));	 					// 20초로 고정. 추후 설정값으로 받아야함.
        }

        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        String usageMetricField = null;
        String quotaMetricField = null;


        switch (metricName.toUpperCase()){
            case "CPU" :
                metricType = "cores";
                quotaMetricField = "system.cpu.cores";
                usageMetricField = "system.cpu.total.norm.pct";
                index.setIndex("sym-metric-cpu-");
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "RAM" :
                metricType = "memory";
                quotaMetricField = "system.memory.actual.free";
                usageMetricField = "system.memory.actual.used.pct";
                index.setIndex("sym-metric-memory-");
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;

            default:
                index.setIndex("sym-metric-cpu-");
                System.out.println("조회할 Instance metric Type(CPU,RAM)을 확인해주세요.. ");
        }

        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");

        // bucketPath
        Map<String, String> bucketsPathsMap = new HashMap<String, String>();
        bucketsPathsMap.put("total_"+metricName.toLowerCase()+"_usage_avg","total_"+metricName.toLowerCase()+"_usage_avg");
        Script usageScript = new Script("params.total_"+metricName.toLowerCase()+"_usage_avg * 100");

        // sort관련 정보 셋팅
        List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().ASC));	// 오름차순
        sorts.add(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order(new FieldSortBuilder("total_"+metricName.toLowerCase()+"_usage_avg_percent").order().DESC)); // 내림차순

        SearchResponse response = null;


        QueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("host.hostname.keyword",hostTopList))				// defualt top 5개 호스트명 셋팅.
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte))
                .mustNot(QueryBuilders.existsQuery("cloud"));

        try{
            response =
                    esClient.search(new SearchRequest(index.getIndex()+"*")
                            .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.dateHistogram("total_agg").field("@timestamp").fixedInterval(interval)
                                                    .subAggregation(
                                                            AggregationBuilders.terms("term_agg_result").field("host.hostname.keyword").size(10)				// 설정 사이즈 만큼 추출
                                                                    .subAggregation(
                                                                            AggregationBuilders.avg("total_"+metricName.toLowerCase()+"_usage_avg").field(usageMetricField)
                                                                    ).subAggregation(
                                                                    PipelineAggregatorBuilders.bucketScript("total_"+metricName.toLowerCase()+"_usage_avg_percent",bucketsPathsMap,usageScript)
                                                            ).subAggregation(
                                                                    PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
                                                            )
                                                    )
                                    )
                                    .size(0)
                                    .trackTotalHits(true)
                                    .sort(SortBuilders.scoreSort())
                            ), RequestOptions.DEFAULT);

            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            e.printStackTrace();
            log.error("## ES search ERROR 발생! ["+e.getMessage()+"]");
        }

        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Histogram histogram = response.getAggregations().get("total_agg");
        Collection<Histogram.Bucket> histogramBuckets = (Collection<Histogram.Bucket>) histogram.getBuckets();
        Terms terms = null;
        Collection<Terms.Bucket> bucketsList = null;


        SimpleValue usageBucket = null;
        String termInstanceNm = null;
        System.out.println("## histogram bucket Hits Size["+histogramBuckets.size()+"]");

        try{
            for (Histogram.Bucket bucket : histogramBuckets) {											// 기간 통계 voList에 담음.

                usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

                terms = bucket.getAggregations().get("term_agg_result");
                bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

                date = transFormat.parse(bucket.getKeyAsString());    // dateType의 timestamp를 longType으로 셋팅.

                for(Terms.Bucket termsBucket : bucketsList){
                    aggregatedVo = new OstHypervisorAggregatedVo();

                    termInstanceNm = termsBucket.getKeyAsString();
                    usageBucket = (SimpleValue) termsBucket.getAggregations().getAsMap().get("total_"+metricName.toLowerCase()+"_usage_avg_percent");

                    aggregatedVo.setTimestamp(date.getTime());
                    aggregatedVo.setHypervisorName(termInstanceNm);

                    if(metricType.equalsIgnoreCase("cores")){
                        aggregatedVo.setVcpuUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
                    }else if(metricType.equalsIgnoreCase("memory")){
                        aggregatedVo.setMemoryUsagePercent(Double.parseDouble(usageBucket.getValueAsString()));
                    }

                    voList.add(aggregatedVo);
                }


            }

            // 추출한 voList를 Top5 물리 호스트 별로 times, values, series 에 담기. 이때 정렬은 따로안하고 애초에 전달받은 물리 호스트가 Top 5 순서대로 전달받아야함.
            for(String tmpInstanceNm : hostTopList){
                timeArr = new ArrayList<>();
                valueArr = new ArrayList<>();
                for(OstHypervisorAggregatedVo tmpVo : voList ){
                    if(tmpInstanceNm.equalsIgnoreCase(tmpVo.getHypervisorName())){
                        timeArr.add(tmpVo.getTimestamp());
                        if(metricType.equalsIgnoreCase("cores")){
                            System.out.println("## HostName["+tmpVo.getHypervisorName()+"] cores!["+tmpVo.getVcpuUsagePercent()+"]");
                            valueArr.add(tmpVo.getVcpuUsagePercent());
                        }else if(metricType.equalsIgnoreCase("memory")){
                            System.out.println("## HostName["+tmpVo.getHypervisorName()+"] memory!["+tmpVo.getVcpuUsagePercent()+"]");
                            valueArr.add(tmpVo.getMemoryUsagePercent());
                        }
                    }
                }
                if(valueArr.size() != 0 && timeArr.size() != 0){				// 위 for문에서 물리 호스트 네임이 존재할때만 추가.
                    timesList.add(timeArr);
                    valuesList.add(valueArr);
                    seriesArr.add(tmpInstanceNm);
                }

            }

            graphAggregatedVo.setTimes(timesList);
            graphAggregatedVo.setValues(valuesList);
            graphAggregatedVo.setSeries(seriesArr);

            System.out.println("## Y_TEST Stop_Line!");

        }catch (Exception e){
            e.printStackTrace();
            log.error("## Elasticsearch에서 Instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
        }
    }

    @Test
    public void retriveGraphInstanceMetricTest(){

        RestHighLevelClient esClient = elasticsearchClient();
        ElasticIndex index = new ElasticIndex();
        OstInstanceAggregatedVo aggregatedVo = new OstInstanceAggregatedVo();
        List<OstInstanceAggregatedVo> voList = new ArrayList<OstInstanceAggregatedVo>();

        ArrayList<String> instanceTopList = new ArrayList<>();

        ArrayList<ArrayList<Long>> timesList = new ArrayList<>();
        ArrayList<ArrayList<Double>> valuesList = new ArrayList<>();
        ArrayList<Long> timeArr  = new ArrayList<>();
        ArrayList<Double> valueArr = new ArrayList<>();
        ArrayList<String> seriesArr = new ArrayList<>();


        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        Date date = null;
        String gte = null;
        String lte = null;
        DateHistogramInterval interval = null;
        String metricName = "cpu";
        String dateType = "day";
        String dateUnit = "1";
        instanceTopList.add("bigdata-elasticsearch-master-1.novalocal");
        instanceTopList.add("platform-consul-3.novalocal");
        instanceTopList.add("platform-haproxy.novalocal");
        instanceTopList.add("platform-ptlmail.novalocal");
        instanceTopList.add("platform-beat-test.novalocal");


        if(dateType.equalsIgnoreCase("day") || dateType.equalsIgnoreCase("d")){
            gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간
            lte = "now/d";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
        }else if(dateType.equalsIgnoreCase("hour") || dateType.equalsIgnoreCase("h")){
            gte = "now-"+dateUnit+"h/h";																// date 타입기준 입력받은 dateUnit만큼 전 시간
            lte = "now/h";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));						// 20초로 고정. 추후 설정값으로 받아야함.
        }else{
            gte = "now-"+dateUnit+"d/d";																// date 타입기준 입력받은 dateUnit만큼 전 시간, 입력안받았으면 기본 1
            lte = "now/d";																				// date 타입기준 현재시간
            interval = DateHistogramInterval.seconds(Integer.parseInt("20"));	 					// 20초로 고정. 추후 설정값으로 받아야함.
        }

        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        String usageMetricField = null;
        String quotaMetricField = null;


        switch (metricName.toUpperCase()){
            case "CPU" :
                metricType = "cores";
                quotaMetricField = "system.cpu.cores";
                usageMetricField = "system.cpu.total.norm.pct";
                index.setIndex("sym-metric-cpu-");
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "RAM" :
                metricType = "memory";
                quotaMetricField = "system.memory.actual.free";
                usageMetricField = "system.memory.actual.used.pct";
                index.setIndex("sym-metric-memory-");
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;

            default:
                index.setIndex("sym-metric-cpu-");
                System.out.println("조회할 Instance metric Type(CPU,RAM)을 확인해주세요.. ");
        }

        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");

        // bucketPath
        Map<String, String> bucketsPathsMap = new HashMap<String, String>();
        bucketsPathsMap.put("total_"+metricName.toLowerCase()+"_usage_avg","total_"+metricName.toLowerCase()+"_usage_avg");
        Script usageScript = new Script("params.total_"+metricName.toLowerCase()+"_usage_avg * 100");

        // sort관련 정보 셋팅
        List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().ASC));	// 오름차순
        sorts.add(new FieldSortBuilder("total_cpu_usage_avg_percent").order(new FieldSortBuilder("total_cpu_usage_avg_percent").order().DESC)); // 내림차순

        SearchResponse response = null;


        QueryBuilder query = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("cloud.instance.name.keyword",instanceTopList))				// top 8개 인스턴스명 셋팅.
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));

        try{
            response =
                    esClient.search(new SearchRequest(index.getIndex()+"*")
                            .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.dateHistogram("total_agg").field("@timestamp").fixedInterval(interval)
                                                    .subAggregation(
                                                            AggregationBuilders.terms("term_agg_result").field("cloud.instance.name.keyword").size(10)
                                                                    .subAggregation(
                                                                            AggregationBuilders.avg("total_"+metricName.toLowerCase()+"_usage_avg").field(usageMetricField)
                                                                    ).subAggregation(
                                                                    PipelineAggregatorBuilders.bucketScript("total_cpu_usage_avg_percent",bucketsPathsMap,usageScript)
                                                            ).subAggregation(
                                                                    PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
                                                            )
                                                    )
                                    )
                                    .size(0)
                                    .trackTotalHits(true)
                                    .sort(SortBuilders.scoreSort())
                            ), RequestOptions.DEFAULT);

            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
        }

        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Histogram histogram = response.getAggregations().get("total_agg");
        Collection<Histogram.Bucket> histogramBuckets = (Collection<Histogram.Bucket>) histogram.getBuckets();
        Terms terms = null;
        Collection<Terms.Bucket> bucketsList = null;


        SimpleValue usageBucket = null;
        String termInstanceNm = null;
        System.out.println("## histogram bucket Hits Size["+histogramBuckets.size()+"]");

        try{
            for (Histogram.Bucket bucket : histogramBuckets) {											// 기간 통계 voList에 담음.

                usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("total_cpu_usage_avg_percent");

                terms = bucket.getAggregations().get("term_agg_result");
                bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

                date = transFormat.parse(bucket.getKeyAsString());    // dateType의 timestamp를 longType으로 셋팅.

                for(Terms.Bucket termsBucket : bucketsList){
                    aggregatedVo = new OstInstanceAggregatedVo();

                    termInstanceNm = termsBucket.getKeyAsString();
                    usageBucket = (SimpleValue) termsBucket.getAggregations().getAsMap().get("total_cpu_usage_avg_percent");

                    aggregatedVo.setTimestamp(date.getTime());
                    aggregatedVo.setInstanceName(termInstanceNm);

                    System.out.println("## tmpKeyStr["+termInstanceNm+"]");
                    System.out.println("## Y_TEST metricName is ["+metricName+"]");
                    System.out.println("## Y_TEST metricName.toUpperCase is ["+metricName.toUpperCase()+"]");
                    System.out.println("## Y_TEST Key_as_String["+bucket.getKeyAsString()+"]");
                    System.out.println("## Y_TEST usageBucket["+usageBucket.getValueAsString()+"]");
                    System.out.println("##Y_TEST ["+aggregatedVo.getTimestamp()+"]");

                    voList.add(aggregatedVo);
                }

                if(metricType.equalsIgnoreCase("cores")){
                    aggregatedVo.setCpuUsageAvg(Double.parseDouble(usageBucket.getValueAsString()));
                }else if(metricType.equalsIgnoreCase("memory")){
                    aggregatedVo.setRamUsageAvg(Double.parseDouble(usageBucket.getValueAsString()));
                }
            }

            // 추출한 voList를 Top5 인스턴스 별로 times, values, series 에 담기.
            for(String tmpInstanceNm : instanceTopList){
                timeArr = new ArrayList<>();
                valueArr = new ArrayList<>();
                for(OstInstanceAggregatedVo tmpVo : voList ){
                    if(tmpInstanceNm.equalsIgnoreCase(tmpVo.getInstanceName())){
                        System.out.println("##Y_TEST Nm["+tmpVo.getInstanceName()+"]");
                        System.out.println("##Y_TEST ["+tmpVo.getTimestamp()+"]");
                        timeArr.add(tmpVo.getTimestamp());
                        valueArr.add(tmpVo.getCpuUsageAvg());
                    }
                }
                timesList.add(timeArr);
                valuesList.add(valueArr);
            }
            seriesArr = instanceTopList;


            System.out.println("## Y_TEST stop Line!");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("## Elasticsearch에서 Instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
        }

    }


    @Test
    public void retriveInstanceMetricTest2(){

        RestHighLevelClient esClient = elasticsearchClient();
        OstInstanceAggregatedVo aggregatedVo = new OstInstanceAggregatedVo();
        List<OstInstanceAggregatedVo> voList = new ArrayList<OstInstanceAggregatedVo>();
        ElasticIndex index = new ElasticIndex();
        DateUtil dateUtil = new DateUtil();
        String metricName = "cpu";
        String dateType = "day";
        String dateUnit = "1";
        ArrayList<String> instanceTopList = new ArrayList<>();
        instanceTopList.add("bigdata-elasticsearch-master-1.novalocal");
        instanceTopList.add("platform-consul-3.novalocal");
        instanceTopList.add("platform-haproxy.novalocal");
        instanceTopList.add("platform-ptlmail.novalocal");
        instanceTopList.add("platform-beat-test.novalocal");

        long gte = dateUtil.getBreforeTimestamp(dateType,Integer.valueOf(dateUnit));
        long lte = System.currentTimeMillis();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");;
        Date timeStamp = null;

        int overCount = 8;
        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        ParsedTopHits topHits = null;
        SearchHit topHitValue = null;
        long curUnixTime = System.currentTimeMillis()/1000;

        switch (metricName.toUpperCase()){
            case "CPU" :
//                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                index.setIndex("sym-metric-cpu-2021.06.20");

                metricType = "cores";
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "RAM" :
                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                metricType = "memory_mb";
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;


            default:
                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                System.out.println("조회할 Instance metric Type(CPU,RAM)을 확인해주세요. 설정값이 올바르지않아 CPU & RAM 모두 조회합니다.");
                break;
        }

        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");

        Map<String, String> bucketsPathsMap = new HashMap<String, String>();
        bucketsPathsMap.put("total_cpu_usage_avg","total_cpu_usage_avg");
        Script quotaScript = new Script("100.0 - (params.total_cpu_usage_avg * 100)");
        Script usageScript = new Script("params.total_cpu_usage_avg * 100");

        List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_pct").order(new FieldSortBuilder("tmp_pct").order().ASC));	// 오름차순
        sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().DESC)); // 내림차순

        SearchResponse response = null;
//        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex()+"*");


        QueryBuilder query = QueryBuilders.boolQuery()
//                .must(matchQuery)
                .must(QueryBuilders.termsQuery("cloud.instance.name.keyword",instanceTopList))
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("now-1d/d")
                        .lte("now/d"));

        try{
            response =
                    esClient.search(new SearchRequest("sym-metric-cpu-2021.*")
                            .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("term_agg_result").field("cloud.instance.name.keyword").size(10)
                                                    .subAggregation(
                                                            AggregationBuilders.avg("total_cpu_quota_avg")
                                                                    .field("system.cpu."+metricType)
                                                    ).subAggregation(
                                                    AggregationBuilders.avg("total_cpu_usage_avg")
                                                            .field("system.cpu.total.norm.pct")
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketScript("tmp_quotaAvg",bucketsPathsMap,quotaScript)
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketScript("tmp_usageAvg",bucketsPathsMap,usageScript)
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
                                            ))
                                    .size(0)
                                    .trackTotalHits(true)
                                    .sort(SortBuilders.scoreSort())
                            ), RequestOptions.DEFAULT);
            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }

        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Terms terms = response.getAggregations().get("term_agg_result");
        Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();


        System.out.println("## bucket Hits Size["+buckets.size()+"]");


        Collection<Terms.Bucket> bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

        ParsedAvg quotaAvg = null;
        ParsedAvg usageAvg = null;
        SimpleValue quotaBucket = null;
        SimpleValue usageBucket = null;


        for (Terms.Bucket bucket : bucketsList) {											// 기간 통계 voList에 담음.
            aggregatedVo = new OstInstanceAggregatedVo();
            quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_cpu_quota_avg");
            usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_cpu_usage_avg");
            quotaBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_quotaAvg");
            usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_usageAvg");


            aggregatedVo.setInstanceName(bucket.getKeyAsString());
            System.out.println("## Y_TEST metricName is ["+metricName+"]");
            System.out.println("## Y_TEST metricName.toUpperCase is ["+metricName.toUpperCase()+"]");
            System.out.println("## Y_TEST Key_as_String["+bucket.getKeyAsString()+"]");
            System.out.println("## Y_TEST quotaAvg["+quotaAvg.getValue()+"]");
            System.out.println("## Y_TEST usageAvg["+usageAvg.getValue()+"]");
            System.out.println("## Y_TEST quotaBucket["+quotaBucket.getValueAsString()+"]");
            System.out.println("## Y_TEST usageBucket["+usageBucket.getValueAsString()+"]");




            if(metricType.equalsIgnoreCase("cores")){
                aggregatedVo.setCpuQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setCpuUsageAvg(usageAvg.getValue());
            }else if(metricType.equalsIgnoreCase("ram")){
                aggregatedVo.setRamQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setRamUsageAvg(usageAvg.getValue());
            }else if(metricType.equalsIgnoreCase("instances")){
                aggregatedVo.setInstanceQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setInstanceUsageAvg(usageAvg.getValue());
            }

            aggregatedVo.setTimestamp(curUnixTime);

            voList.add(aggregatedVo);
        }

    }
    
    @Test
    public void retriveInstanceMetricTest(){

        RestHighLevelClient esClient = elasticsearchClient();
        OstInstanceAggregatedVo aggregatedVo = new OstInstanceAggregatedVo();
        List<OstInstanceAggregatedVo> voList = new ArrayList<OstInstanceAggregatedVo>();
        ElasticIndex index = new ElasticIndex();
        DateUtil dateUtil = new DateUtil();
        String metricName = "cpu";
        String dateType = "day";
        String dateUnit = "1";

        long gte = dateUtil.getBreforeTimestamp(dateType,Integer.valueOf(dateUnit));
        long lte = System.currentTimeMillis();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");;
        Date timeStamp = null;

        int overCount = 8;
        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        ParsedTopHits topHits = null;
        SearchHit topHitValue = null;
        long curUnixTime = System.currentTimeMillis()/1000;

        switch (metricName.toUpperCase()){
            case "CPU" :
//                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                index.setIndex("sym-metric-cpu-2021.06.20");

                metricType = "cores";
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "RAM" :
                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                metricType = "memory_mb";
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;


            default:
                index.setIndex("sym-metric-cpu-"+index.getSystemIndexDate());
                System.out.println("조회할 Instance metric Type(CPU,RAM)을 확인해주세요. 설정값이 올바르지않아 CPU & RAM 모두 조회합니다.");
                break;
        }

        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");

        Map<String, String> bucketsPathsMap = new HashMap<String, String>();
        bucketsPathsMap.put("total_cpu_usage_avg","total_cpu_usage_avg");
        Script quotaScript = new Script("100.0 - (params.total_cpu_usage_avg * 100)");
        Script usageScript = new Script("params.total_cpu_usage_avg * 100");

        List<FieldSortBuilder> sorts = new ArrayList<>();
//		sorts.add(new FieldSortBuilder("tmp_pct").order(new FieldSortBuilder("tmp_pct").order().ASC));	// 오름차순
        sorts.add(new FieldSortBuilder("tmp_usageAvg").order(new FieldSortBuilder("tmp_usageAvg").order().DESC)); // 내림차순

        SearchResponse response = null;
//        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex()+"*");


        QueryBuilder query = QueryBuilders.boolQuery()
//                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("now-1d/d")
                        .lte("now/d"));

        try{
            response =
                    esClient.search(new SearchRequest("sym-metric-cpu-2021.*")
                            .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("term_agg_result").field("cloud.instance.name.keyword").size(10)
                                                    .subAggregation(
                                                            AggregationBuilders.avg("total_cpu_quota_avg")
                                                                    .field("system.cpu."+metricType)
                                                    ).subAggregation(
                                                    AggregationBuilders.avg("total_cpu_usage_avg")
                                                            .field("system.cpu.total.norm.pct")
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketScript("tmp_quotaAvg",bucketsPathsMap,quotaScript)
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketScript("tmp_usageAvg",bucketsPathsMap,usageScript)
                                            ).subAggregation(
                                                    PipelineAggregatorBuilders.bucketSort("bucket_sort_order",sorts)
                                            ))
                                    .size(0)
                                    .trackTotalHits(true)
                                    .sort(SortBuilders.scoreSort())
                            ), RequestOptions.DEFAULT);
            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }

        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Terms terms = response.getAggregations().get("term_agg_result");
        Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();


        System.out.println("## bucket Hits Size["+buckets.size()+"]");


        Collection<Terms.Bucket> bucketsList = (Collection<Terms.Bucket>) terms.getBuckets();

        ParsedAvg quotaAvg = null;
        ParsedAvg usageAvg = null;
        SimpleValue quotaBucket = null;
        SimpleValue usageBucket = null;


        for (Terms.Bucket bucket : bucketsList) {											// 기간 통계 voList에 담음.
            aggregatedVo = new OstInstanceAggregatedVo();
            quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_cpu_quota_avg");
            usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_cpu_usage_avg");
            quotaBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_quotaAvg");
            usageBucket = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_usageAvg");


            aggregatedVo.setInstanceName(bucket.getKeyAsString());
            System.out.println("## Y_TEST metricName is ["+metricName+"]");
            System.out.println("## Y_TEST metricName.toUpperCase is ["+metricName.toUpperCase()+"]");
            System.out.println("## Y_TEST Key_as_String["+bucket.getKeyAsString()+"]");
            System.out.println("## Y_TEST quotaAvg["+quotaAvg.getValue()+"]");
            System.out.println("## Y_TEST usageAvg["+usageAvg.getValue()+"]");
            System.out.println("## Y_TEST quotaBucket["+quotaBucket.getValueAsString()+"]");
            System.out.println("## Y_TEST usageBucket["+usageBucket.getValueAsString()+"]");




            if(metricType.equalsIgnoreCase("cores")){
                aggregatedVo.setCpuQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setCpuUsageAvg(usageAvg.getValue());
//                aggregatedVo.setCpuAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
            }else if(metricType.equalsIgnoreCase("ram")){
                aggregatedVo.setRamQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setRamUsageAvg(usageAvg.getValue());
//                aggregatedVo.setRamAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
            }else if(metricType.equalsIgnoreCase("instances")){
                aggregatedVo.setInstanceQuotaAvg(quotaAvg.getValue());
                aggregatedVo.setInstanceUsageAvg(usageAvg.getValue());
//                aggregatedVo.setInstanceAvgMin(Double.parseDouble(minQuotaUsage.getValueAsString()));
            }

            aggregatedVo.setTimestamp(curUnixTime);

            voList.add(aggregatedVo);
//            System.out.println("key:"+bucket.getKey()+", quotaAvg["+quotaAvg.getValue()+"], usageAvg["+usageAvg.getValue()+"], quotaAvg-usageAvg["+Double.parseDouble(minQuotaUsage.getValueAsString())+"]");
        }

    }

    @Test
    public void retrieveComputeMetricToRange (){
        String metricName = "cpu";
        String gte = "1623979842052";
        String lte = String.valueOf(System.currentTimeMillis());
        ElasticIndex index = new ElasticIndex();

        RestHighLevelClient esClient = elasticsearchClient();
        OstComputeMetricVo aggregatedVo = new OstComputeMetricVo();
        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        ParsedTopHits topHits = null;
        SearchHit topHitValue = null;
        long curUnixTime = System.currentTimeMillis()/1000;

        switch (metricName.toUpperCase()){
            case "CPU" :
                index.setIndex("ost-service-compute-"+index.getIndexDate());
                metricType = "vcpus";
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "INSTANCE" :
                index.setIndex("ost-service-compute-"+index.getIndexDate());
                metricType = "memory_mb";
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;


            default:
                index.setIndex("ost-service-compute-");
                System.out.println("조회할 ComputeService metric Type(CPU,NETWORK,INSTANCE)을 확인해주세요. ");
                break;
        }

        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");


        SearchResponse response = null;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex());


        QueryBuilder query = QueryBuilders.boolQuery()
                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte(gte)
                        .lte(lte));

        try{
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                            .query(query)
                            .aggregation(
                                    AggregationBuilders.terms("class_term_result").field("_class.keyword").size(1000)
                                            .subAggregation(
                                                    AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp", SortOrder.DESC)
                                            ))
                            .size(0)
                            .trackTotalHits(true)
                            .sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);
            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }
        System.out.println("## Y_TEST gte["+gte+"]");
        System.out.println("## Y_TEST lte["+lte+"]");

        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Terms terms = response.getAggregations().get("class_term_result");
        Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();

        System.out.println("## bucket Hits Size["+buckets.size()+"]");
        try{
            for (Terms.Bucket bucket : buckets) {
                topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
                topHitValue = topHits.getHits().getAt(0);


                aggregatedVo.setNovaName(bucket.getKeyAsString());

                Gson gson = new Gson();				// topHitValue 들어온 값 json 형태로 추출하기위해.
                JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
                Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
                prtGson.toJson(detailInfoJson);
                System.out.println("## NovaName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


                aggregatedVo.setId(detailInfoJson.get("id").getAsString());

                //*************여기서 중요한거 ES조회시 Exception나는거 없애는방법 찾아서 소스에 적용하기.

                if(metricName.equalsIgnoreCase("cpu")){				// cpu 조회일때.
                    aggregatedVo.setCpuQuota((double)detailInfoJson.get("cpu_quota").getAsInt());
                    aggregatedVo.setCpuUsage((double)detailInfoJson.get("cpu_usage").getAsInt());
                    aggregatedVo.setNetworkQuota((double)detailInfoJson.get("network_quota").getAsInt());
                    aggregatedVo.setNetworkUsage((double)detailInfoJson.get("network_usage").getAsInt());
                    aggregatedVo.setInstanceQuota((double)detailInfoJson.get("instance_quota").getAsInt());
                    aggregatedVo.setInstanceUsage((double)detailInfoJson.get("instance_usage").getAsInt());
                }else if(metricName.equalsIgnoreCase("network")){	// network 조회일때.

                }else if(metricName.equalsIgnoreCase("instance")){		// instance 조회일때.
                    aggregatedVo.setInstanceQuota((double)detailInfoJson.get("instance_quota").getAsInt());
                    aggregatedVo.setInstanceUsage((double)detailInfoJson.get("instance_usage").getAsInt());
                }else{															// cpu와 ram조회일때.
                    System.out.println("## else로 빠짐. 확인요망.");
                    aggregatedVo.setCpuQuota((double)detailInfoJson.get("cpu_quota").getAsInt());
                    aggregatedVo.setCpuUsage((double)detailInfoJson.get("cpu_usage").getAsInt());
                    aggregatedVo.setNetworkQuota((double)detailInfoJson.get("network_quota").getAsInt());
                    aggregatedVo.setNetworkUsage((double)detailInfoJson.get("network_usage").getAsInt());
                    aggregatedVo.setInstanceQuota((double)detailInfoJson.get("instance_quota").getAsInt());
                    aggregatedVo.setInstanceUsage((double)detailInfoJson.get("instance_usage").getAsInt());
                }
                aggregatedVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());
            }
            System.out.println("## Y_TEST CpuQuota["+aggregatedVo.getCpuQuota()+"], CpuUsage["+aggregatedVo.getCpuUsage()+"]");
            System.out.println("## Y_TEST InstanceQuota["+aggregatedVo.getInstanceQuota()+"], InstanceQuota["+aggregatedVo.getInstanceUsage()+"]");

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
        }


    }


    @Test
    public void osHypervisorResult(){

        OSClient.OSClientV3 osClient = null;
        try{
            osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
            WebclientUtil webclientUtil = new WebclientUtil();
            // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
            WebClient webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.OS_QUOTA_SETS);

            HypervisorMetricVo hypervisorMetricInfos = null;
            HostAggregateMetricVo hostAggregateMetricInfos = null;
            // get unix Time
            long epoch = System.currentTimeMillis();

            try {
                // hypervisor 상세 정보.
                hypervisorMetricInfos = webClient.get()
                        .uri("/v2.1/os-hypervisors/detail")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(HypervisorMetricVo.class).block();

                // hypervisor 상세 정보.
                hostAggregateMetricInfos = webClient.get()
                        .uri("/v2.1/os-aggregates")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(HostAggregateMetricVo.class).block();

                System.out.println("## Y_TEST hostAggregate.size["+hostAggregateMetricInfos.getHostAggregates().size()+"]");

                // end time
                System.out.println("========HypervisorList(Webclient)================");

            } catch (Exception e) {
                System.out.println("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
            }


            // 조회한 hypervisor 정보 셋팅
            for(HypervisorMetricVo.Hypervisor hypervisor : hypervisorMetricInfos.getHypervisors()) {
                HostVo vo = new HostVo();

                vo.setId(Integer.toString(hypervisor.getId()));
                vo.setName(hypervisor.getHypervisorHostname());
                vo.setTimestamp(String.valueOf(epoch));
                vo.setType(hypervisor.getHypervisorType());
                vo.setStatus(hypervisor.getStatus());
                vo.setState(hypervisor.getState());
                vo.setHostIp(hypervisor.getHostIp());
                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
                vo.setLeastDiskAvailable(hypervisor.getDiskAvailableLeast());
                vo.setHypervisorVersion(hypervisor.getHypervisorVersion());
                vo.setRunningVms(hypervisor.getRunningVms());
                vo.setVcpus(hypervisor.getVcpus());
                vo.setVcpusUsed(hypervisor.getVcpusUsed());
                vo.setLocalGb(hypervisor.getDiskAvailableLeast());
                vo.setLocalGbUsed(hypervisor.getLocalGbUsed());
                vo.setFreeDiskGb(hypervisor.getFreeDiskGb());
                vo.setMemoryMb(hypervisor.getMemoryMb());
                vo.setMemoryMbUsed(hypervisor.getMemoryMbUsed());
                vo.setFreeRam(hypervisor.getFreeRamMb());
                vo.setProviderName("openstack-1");




                // hostEntity.setCollectDt(time);


//                list.add(vo);

            }

            // 조회한 hostAggregates 정보 셋팅


            // end time
            // System.out.println("========HypervisorList(Webclient)================");



        }catch (Exception e){

        }


    }

    /**
     * ES에 hypervisor tophits aggs 쿼리 조회 테스트.
     */
    @Test
    public void tophitsAggSearch() {

        ElasticIndex index = new ElasticIndex();
        RestHighLevelClient esClient = elasticsearchClient();
        int overCount = 8;
        OstHypervisorAggregatedVo aggregatedVo = new OstHypervisorAggregatedVo();
        List<OstHypervisorAggregatedVo> voList = new ArrayList<OstHypervisorAggregatedVo>();
        JSONObject resultJson = null;
        String resultStr = null;
        String metricType = null;
        ParsedTopHits topHits = null;
        SearchHit topHitValue = null;
        long curUnixTime = System.currentTimeMillis()/1000;
        String metricName = "cpu";

        switch (metricName.toUpperCase()){
            case "CPU" :
                index.setIndex("ost-metric-hypervisors-detail-"+index.getIndexDate());
                metricType = "vcpus";
                System.out.println("## Y_TEST MetricType is CPU["+metricType+"]");
                break;
            case "RAM" :
                index.setIndex("ost-metric-hypervisors-detail-"+index.getIndexDate());
                metricType = "memory_mb";
                System.out.println("## Y_TEST MetricType is MEMORY["+metricType+"]");
                break;


            default:
                System.out.println("조회할 Hypervisor metric Type을 확인해주세요.");
                break;
        }


//        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");
        index.setIndex("ost-metric-hypervisors-2021-06-16");
        System.out.println("## Y_TEST IndexName ["+index.getIndex()+"]");
        SearchResponse response = null;
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("_index",index.getIndex());

        QueryBuilder query = QueryBuilders.boolQuery()
                .must(matchQuery)
                .must(QueryBuilders.rangeQuery("@timestamp")
                        .gte("1623816930313")
                        .lte("1623816930313"));

        try{
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                                    .query(query)
                                    .aggregation(
                                            AggregationBuilders.terms("test_aggs_terms").field("hypervisor.hypervisor_hostname.keyword")
                                                    .subAggregation(
                                                            AggregationBuilders.topHits("test_aggs_docs").size(1).sort("@timestamp", SortOrder.DESC)
                                                    ))
                                    .size(0)
                                    .trackTotalHits(true)
//							.sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);


            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            resultStr = resultJson.toString(1);
            System.out.println("## elasticsearch response: ["+resultStr+"] toString");
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }


        // ES 통계 쿼리 조회 결과값 Vo 셋팅.
        Terms terms = response.getAggregations().get("test_aggs_terms");
        Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();


        System.out.println("## bucket Hits Size["+buckets.size()+"]");
        try{
            for (Terms.Bucket bucket : buckets) {
                topHits = (ParsedTopHits) bucket.getAggregations().getAsMap().get("test_aggs_docs");
                topHitValue = topHits.getHits().getAt(0);
                aggregatedVo = new OstHypervisorAggregatedVo();

                aggregatedVo.setHypervisorName(bucket.getKeyAsString());

//						System.out.println("## 1 hit result ["+topHitValue+"]");
                Gson gson = new Gson();
                JsonObject detailInfoJson = gson.toJsonTree(topHitValue.getSourceAsMap()).getAsJsonObject();
                Gson prtGson = new GsonBuilder().setPrettyPrinting().create();
                prtGson.toJson(detailInfoJson);
                System.out.println("## HypervisorName["+bucket.getKeyAsString()+"] HashMap to Json result ["+prtGson.toJson(detailInfoJson)+"]");


                aggregatedVo.setHypervisorId(detailInfoJson.get("hypervisor").getAsJsonObject().get("hypervisor_id").getAsString());
                aggregatedVo.setHypervisorName(detailInfoJson.get("hypervisor").getAsJsonObject().get("hypervisor_hostname").getAsString());
                aggregatedVo.setCurStatus(detailInfoJson.get("hypervisor").getAsJsonObject().get("status").getAsString());
                aggregatedVo.setVcpuQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble());
                aggregatedVo.setVcpuUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble());
                aggregatedVo.setMemoryQuota(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble());
                aggregatedVo.setMemoryUsage(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble());
                aggregatedVo.setVcpuUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble() * overCount)) * 100.0);
                aggregatedVo.setMemoryUsagePercent((detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble() * overCount)) * 100.0);

                System.out.println("## Y_TEST vcpu percent ["+(detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("vcpus").getAsDouble() * overCount)) * 100.0+"]");
                System.out.println("## Y_TEST memory percent ["+(detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb_used").getAsDouble() / (detailInfoJson.get("hypervisor").getAsJsonObject().get("memory_mb").getAsDouble() * overCount)) * 100.0+"]");

                aggregatedVo.setTimestamp(detailInfoJson.get("@timestamp").getAsLong());
                voList.add(aggregatedVo);

                if(metricName.equalsIgnoreCase("cpu")){
                    Collections.sort(voList, new Comparator<OstHypervisorAggregatedVo>() {
                        @Override
                        public int compare(OstHypervisorAggregatedVo o1, OstHypervisorAggregatedVo o2) {
                            return o1.getVcpuUsagePercent().compareTo(o2.getVcpuUsagePercent());
                        }
                    });
                    Collections.reverse(voList);
                }else{

                }

            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("## Elasticsearch에서 project instance 정보 setting중 에러발생. ["+e.getMessage()+"]");
        }


        System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
        System.out.println("## elasticsearch response: {"+response.toString()+"} toString");
        for(OstHypervisorAggregatedVo tmpVo: voList){
            System.out.println("## Y_TEST vo["+tmpVo.getHypervisorName()+"], voCpuPercent["+tmpVo.getVcpuUsagePercent()+"]");
        }


    }

    /**
     * ES에 aggs 쿼리 조회 테스트.
     */
    @Test
    public void aggSearch() {

        RestHighLevelClient esClient = elasticsearchClient();
        OstProjectAggregatedVo cpuAggregatedVo = new OstProjectAggregatedVo();
        JSONObject resultJson = null;
        String resultStr = null;
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
                        .gte("1623723159918") // 2021-05-13 08:00:00
                        .lte("1623747483092"));

        TermsAggregationBuilder termagg = AggregationBuilders.terms("tmp_term_test_result").field("project_detail_info.project_id.keyword").size(1000);
//        DiversifiedAggregationBuilder sampleAgg = new DiversifiedAggregationBuilder();
//        sampleAgg.field("author").maxDocsPerValue(MAX_DOCS_PER_AUTHOR).executionHint(randomExecutionHint());
//        sampleAgg.subAggregation(terms("authors").field("author"));
//        sampleAgg.subAggregation(terms("genres").field("genre"));

//        InternalTerms internalTerms = (InternalTerms) aggregation;
//        List<FacetedSearchFacet> facets = new ArrayList<>();
//        for (int i = 0; i < internalTerms.getBuckets().size(); i++) {
//            Terms.Bucket bucket = internalTerms.getBuckets().get(i);
//            facets.add(new FacetedSearchFacet(bucket.getKey(), bucket.getDocCount()));
//        }

//        AggregationBuilder articlesOverTime =
//                AggregationBuilders
//                        .terms("tmp_term_test_result")
//                        .field("project_detail_info.project_name.keyword");
//        articlesOverTime.subAggregation(AggregationBuilders.terms("id").field("auAid"));

        try{
            response = esClient.search(new SearchRequest()
                    .source(new SearchSourceBuilder()
                            .query(query)
                            .aggregation(
                                    AggregationBuilders.terms("domain_term_result").field("domain.keyword").size(1000)
                                            .subAggregation(
                                                    AggregationBuilders.terms("project_name_term_result").field("project_detail_info.project_name.keyword").size(1000)
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
                                    )
                            .size(0)
                            .trackTotalHits(true)
                            .sort(SortBuilders.scoreSort())
                    ), RequestOptions.DEFAULT);


//            List<? extends Bucket> buckets = response.getAggregations().get("tmp_term_test_result");
            Terms terms = response.getAggregations().get("tmp_term_test_result");
            Collection<Terms.Bucket> buckets = (Collection<Terms.Bucket>) terms.getBuckets();
            ParsedAvg quotaAvg = null;
            ParsedAvg usageAvg = null;

            SimpleValue minQuotaUsage = null;
            SimpleValue test = null;

            for (Terms.Bucket bucket : buckets) {
                quotaAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_quota_avg");
                usageAvg = (ParsedAvg) bucket.getAggregations().getAsMap().get("total_usage_avg");
                if((SimpleValue) bucket.getAggregations().getAsMap().get("tmp_pct") != null)
                {
                    minQuotaUsage = (SimpleValue) bucket.getAggregations().getAsMap().get("tmp_pct");
                }else{
//                    System.out.println("key:"+bucket.getKeyAsString()+", quotaAvg["+quotaAvg.getValue()+"], usageAvg["+usageAvg.getValue()+"], quotaAvg-usageAvg["+Double.parseDouble(minQuotaUsage.getValueAsString())+"]");
                }
            }
            System.out.println("## buckets.size() ["+buckets.size()+"]");
            System.out.println("## aggregation toString["+response.getInternalResponse().aggregations().get("tmp_term_test_result")+"]");
            System.out.println("###################################################");


            System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
            resultJson = new JSONObject(response.toString());
            System.out.println("## elasticsearch response: {"+resultJson.toString(1)+"} toString");
            resultStr = resultJson.toString(1);
        }catch (Exception e){
            System.out.println("## ES search ERROR 발생! ["+e.getMessage()+"]");
            e.printStackTrace();
        }



        System.out.println("## elasticsearch response: {"+response.getHits().getTotalHits()+"} hits");
        System.out.println("## elasticsearch response: {"+response.toString()+"} toString");

    }

    @Test
    public void testTimestamp(){
        long unixTime = System.currentTimeMillis();
        Date date = new Date(unixTime);
        Calendar cal = java.util.Calendar.getInstance();
        cal.add(cal.DATE, -7);// 일주일 빼기
        date = cal.getTime();

        long beforeWeek = date.getTime();

        System.out.println("## 일주일전 timestamp["+beforeWeek+"]");


    }

    /**
     * SDK를 사용한 특정 데이터 직렬 조회.
     */
    @Test
    public void TestCallResult(){
        // connect to openstack & connect to openstack (project socpe)
        OSClient.OSClientV3 osClient = null;
        try {
            osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            // project리스트 조회.
            List<? extends Project>  projects = osClient.identity().projects().list();
            // project.compute.Quota는 특정 projectId 줘야함.

            // project.Usage
            List<? extends SimpleTenantUsage> projectUsage = osClient.compute().quotaSets().listTenantUsages();
            // network.Quota&Usage
            List<? extends NetQuota> projectNetwork = osClient.networking().quotas().get();
            // project.Volume.Usage&Quota는 특정 projectId 줘야함.


            Gson gson = new Gson();
            String gsonStr = gson.toJson(projectNetwork);
            JSONArray tmpJobj = null;
            tmpJobj = new JSONArray(gsonStr);
            System.out.println("Call SDK Result : "+ tmpJobj.toString(1));

        } catch (ConnectException | JSONException e) {
            e.printStackTrace();
        }
        Date time = new Date();


    }


    /**
     *  admin 권한으로 모든 프로젝트 list 조회 후 각 프로젝트 별 QuotaProjectVo 셋팅 후 list<QuotaProjectVo> 리턴.
     * @return List<QuotaProjectVo>
     */
    @Test
    public void getSdkAllProjectQuotaList(){

        List<QuotaProjectVo> projectVoList = new ArrayList<>();
        List<ProjectQuotaEntity> projectQuotaEntityList = new ArrayList<>();
        final QuotaProjectVo projectQuotaVo = new QuotaProjectVo();
        final ProjectQuotaEntity projectQuotaEntity = new ProjectQuotaEntity();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            // connect to openstack & connect to openstack (project socpe)
            OSClient.OSClientV3 osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
            Date time = new Date();

            List<? extends Project>  projects = osClient.identity().projects().list();



            projects.parallelStream().forEach(projectVo ->{
                OSClient.OSClientV3 os = null;
                try {
                    os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }
                System.out.println("## Y_TEST project Name ["+projectVo.getName() +"]");
//                System.out.println("Starting " + Thread.currentThread().getName() + ", DATE:, " + new Date());

                projectQuotaVo.setProjectName(projectVo.getName());
                projectQuotaVo.setProjectId(projectVo.getId());
                projectQuotaVo.setComputeQuota(getQuotaCompute(projectVo.getId(), os.compute()));
                projectQuotaVo.setNetworkQuota(getQuotaNetwork(projectVo.getId(), os.networking()));
                projectQuotaVo.setVolumeQuota(getQuotaVolumn(projectVo.getId(), os.blockStorage()));
                projectQuotaVo.setComputeUsage(getUsageCompute(projectVo.getId(), os.compute()));
//                projectQuotaVo.setNetworkUsage(getQuotaNetwork(projectVo.getId(), os.networking()));
                projectQuotaVo.setVolumeUsage(getUsageVolumn(projectVo.getId(), os.blockStorage()));

                projectQuotaVo.setProjectName(projectVo.getName());

                projectQuotaEntity.setProjectId(projectQuotaVo.getProjectId());
                projectQuotaEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
                projectQuotaEntity.setInstanceQuota(projectQuotaVo.getComputeQuota().getInstanceQuota());
                projectQuotaEntity.setInstanceUsage(projectQuotaVo.getComputeUsage().getInstanceUsed());
                projectQuotaEntity.setCpuQuota(projectQuotaVo.getComputeQuota().getCoreQuota());
                projectQuotaEntity.setCpuUsage(projectQuotaVo.getComputeUsage().getCoreUsed());
                projectQuotaEntity.setRamQuota(projectQuotaVo.getComputeQuota().getRamQuota());
                projectQuotaEntity.setRamUsage(projectQuotaVo.getComputeUsage().getRamUsed());
                projectQuotaEntity.setVolumeQuota(projectQuotaVo.getVolumeQuota().getVolumeQuota());
                projectQuotaEntity.setVolumeUsage(projectQuotaVo.getVolumeUsage().getVolumeUsed());
                projectQuotaEntity.setSnapshotQuota(projectQuotaVo.getVolumeQuota().getSnapshotQuota());
                projectQuotaEntity.setSnapshotUsage(projectQuotaVo.getVolumeUsage().getSnapshotUsed());
                projectQuotaEntity.setStorageQuota(projectQuotaVo.getVolumeQuota().getGigabyteQuota());
                projectQuotaEntity.setStorageUsage(projectQuotaVo.getVolumeUsage().getGigabyteUsed());
                projectQuotaEntity.setNetworkQuota(projectQuotaVo.getNetworkQuota().getNetworkQuota());
                projectQuotaEntity.setNetworkUsage(projectQuotaVo.getNetworkUsage().getNetworkUsed());
                projectQuotaEntity.setFloatingIpQuota(projectQuotaVo.getNetworkQuota().getFloatingIpQuota());
                projectQuotaEntity.setFloatingIpUsage(projectQuotaVo.getNetworkUsage().getFloatingIpUsed());
                projectQuotaEntity.setSecurityGroupQuota(projectQuotaVo.getNetworkQuota().getSecurityGroupQuota());
                projectQuotaEntity.setSecurityGroupUsage(projectQuotaVo.getNetworkUsage().getSecurityGroupUsed());
                projectQuotaEntity.setSecurityRuleQuota(projectQuotaVo.getNetworkQuota().getSecurityGroupRuleQuota());
                projectQuotaEntity.setSecurityRuleUsage(projectQuotaVo.getNetworkUsage().getSecurityGroupRuleUsed());
                projectQuotaEntity.setPortQuota(projectQuotaVo.getNetworkQuota().getPortQuota());
                projectQuotaEntity.setPortUsage(projectQuotaVo.getNetworkUsage().getPortUsed());
                projectQuotaEntity.setRouterQuota(projectQuotaVo.getNetworkQuota().getRouterQuota());
                projectQuotaEntity.setRouterUsage(projectQuotaVo.getNetworkUsage().getRouterUsed());
                projectQuotaEntity.setCollectDt(time);

                projectVoList.add(projectQuotaVo);
                projectQuotaEntityList.add(projectQuotaEntity);

            });
            System.out.println("## Y_TEST projectVoList Size ["+projectVoList.size() +"]");

            Gson gson = new Gson();
            String gsonStr = gson.toJson(projectVoList);
            JSONArray tmpJobj = null;
            tmpJobj = new JSONArray(gsonStr);
            System.out.println("Call SDK Result : "+ tmpJobj.toString(1));
            //스탑워치가 돌고있으면 멈추고,
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");

        } catch (ConnectException | JSONException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

    }

//    public void openstackSdkCallResult(){
//
//    }

    /**
     * SDK를 사용한 직렬처리 projectQuota 조회.
     */
    @Test
    public void openstackSdkCallResult(){
        OSClient.OSClientV3 osClient = null;

        List<QuotaProjectVo> projectVoList = new ArrayList<>();
        List<ProjectQuotaEntity> projectQuotaEntityList = new ArrayList<>();
        QuotaProjectVo projectQuotaVo = null;
        ProjectQuotaEntity projectQuotaEntity = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try{
            osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
//            osClient = OpenstackService.projectScopeConnectTestMethod(openStackConnectionService);


            System.out.println("## 진입. osClient["+osClient+"]");
            List<? extends Project>  projects = osClient.identity().projects().list();

            for( Project projectVo : projects){
                projectQuotaVo = new QuotaProjectVo();
                projectQuotaEntity = new ProjectQuotaEntity();
                System.out.println("## Y_TEST project Name ["+projectVo.getName() +"]");
                // connect to openstack (project socpe)
//                osProjectClient = openStackConnectionService.connect(endpoint,domain,projectVo.getName(),user,password);

                projectQuotaVo.setProjectName(projectVo.getName());
                projectQuotaVo.setProjectId(projectVo.getId());
//                projectQuotaVo.setComputeQuota(getQuotaCompute(projectVo.getId(), osClient.compute()));
//                projectQuotaVo.setNetworkQuota(getQuotaNetwork(projectVo.getId(), osClient.networking()));
//                projectQuotaVo.setVolumeQuota(getQuotaVolumn(projectVo.getId(), osClient.blockStorage()));
//
//                projectQuotaVo.setComputeUsage(getUsageCompute(projectVo.getId(), osClient.compute()));
//                projectQuotaVo.setVolumeUsage(getUsageVolumn(projectVo.getId(), osClient.blockStorage()));
//                projectQuotaVo.setProjectName(projectVo.getName());
//
//                projectQuotaEntity.setProjectId(projectQuotaVo.getProjectId());
//                projectQuotaEntity.setProviderId("b4ea91ffbc5b435497dde1c2ad38cccc");
////                System.out.println("## Y_TEST projectQuotaVo.getComputeQuota()["+projectQuotaVo.getComputeQuota()+"]");
//                projectQuotaEntity.setInstanceQuota(projectQuotaVo.getComputeQuota().getInstanceQuota());
//                projectQuotaEntity.setInstanceUsage(projectQuotaVo.getComputeUsage().getInstanceUsed());
//                projectQuotaEntity.setCpuQuota(projectQuotaVo.getComputeQuota().getCoreQuota());
//                projectQuotaEntity.setCpuUsage(projectQuotaVo.getComputeUsage().getCoreUsed());
//                projectQuotaEntity.setRamQuota(projectQuotaVo.getComputeQuota().getRamQuota());
//                projectQuotaEntity.setRamUsage(projectQuotaVo.getComputeUsage().getRamUsed());
//                projectQuotaEntity.setVolumeQuota(projectQuotaVo.getVolumeQuota().getVolumeQuota());
//                projectQuotaEntity.setVolumeUsage(projectQuotaVo.getVolumeUsage().getVolumeUsed());
//                projectQuotaEntity.setSnapshotQuota(projectQuotaVo.getVolumeQuota().getSnapshotQuota());
//                projectQuotaEntity.setSnapshotUsage(projectQuotaVo.getVolumeUsage().getSnapshotUsed());
//                projectQuotaEntity.setStorageQuota(projectQuotaVo.getVolumeQuota().getGigabyteQuota());
//                projectQuotaEntity.setStorageUsage(projectQuotaVo.getVolumeUsage().getGigabyteUsed());
//                projectQuotaEntity.setNetworkQuota(projectQuotaVo.getNetworkQuota().getNetworkQuota());
////                projectQuotaEntity.setNetworkUsage(projectQuotaVo.getNetworkUsage().getNetworkUsed());
//                projectQuotaEntity.setNetworkUsage(0);
//                projectQuotaEntity.setFloatingIpQuota(projectQuotaVo.getNetworkQuota().getFloatingIpQuota());
////                projectQuotaEntity.setFloatingIpUsage(projectQuotaVo.getNetworkUsage().getFloatingIpUsed());
//                projectQuotaEntity.setFloatingIpUsage(0);
//                projectQuotaEntity.setSecurityGroupQuota(projectQuotaVo.getNetworkQuota().getSecurityGroupQuota());
////                projectQuotaEntity.setSecurityGroupUsage(projectQuotaVo.getNetworkUsage().getSecurityGroupUsed());
//                projectQuotaEntity.setSecurityGroupUsage(0);
//                projectQuotaEntity.setSecurityRuleQuota(projectQuotaVo.getNetworkQuota().getSecurityGroupRuleQuota());
////                projectQuotaEntity.setSecurityRuleUsage(projectQuotaVo.getNetworkUsage().getSecurityGroupRuleUsed());
//                projectQuotaEntity.setSecurityRuleUsage(0);
//                projectQuotaEntity.setPortQuota(projectQuotaVo.getNetworkQuota().getPortQuota());
////                projectQuotaEntity.setPortUsage(projectQuotaVo.getNetworkUsage().getPortUsed());
//                projectQuotaEntity.setPortUsage(0);
//                projectQuotaEntity.setRouterQuota(projectQuotaVo.getNetworkQuota().getRouterQuota());
////                projectQuotaEntity.setRouterUsage(projectQuotaVo.getNetworkUsage().getRouterUsed());
//                projectQuotaEntity.setRouterUsage(0);


                projectVoList.add(projectQuotaVo);
                projectQuotaEntityList.add(projectQuotaEntity);

            }
            Gson gson = new Gson();
            String gsonStr = gson.toJson(projectVoList);
            JSONArray tmpJobj = null;
            tmpJobj = new JSONArray(gsonStr);
            System.out.println("Call SDK Result : "+ tmpJobj.toString(1));
            //스탑워치가 돌고있으면 멈추고,
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println("조회까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     *
     */
    @Test
    public void openstackMonoTest() throws InterruptedException, SSLException {

        OSClient.OSClientV3 osClient = null;
        WebClient webClient = null;

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try{
            osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            System.out.println("## 진입. osClient["+osClient+"]");
        WebclientUtil webclientUtil = new WebclientUtil();

        // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
        webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.KEYSTONE_IDENTITY);
            System.out.println("## webclient is ["+webClient+"]");
        }catch (Exception e){
            e.printStackTrace();
        }

        // get proejct metric info
        Mono<com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ProjectMetricVo> prMetricInfos = null;
//       com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ProjectMetricVo prMetricInfos = null;

        try {
            prMetricInfos = webClient.get()
                    .uri("/projects")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(com.okestro.symphony.dashboard.api.openstack.keystone.project.model.webclient.vo.ProjectMetricVo.class);

            System.out.println("## /projects 호출전.");
//            Gson gson = new Gson();
//            String gsonStr = gson.toJson(prMetricInfos);
//            JSONObject tmpJobj = new JSONObject(gsonStr);

            //스탑워치가 돌고있으면 멈추고,
            if (stopWatch.isRunning()) {
                stopWatch.stop();
            }

            System.out.println("구독전까지 걸린 시간 :"+stopWatch.getTotalTimeSeconds()+"초");
//            System.out.println("Call url /projects API Result : "+ tmpJobj.toString(1));

            stopWatch.start();

            ///여기 들어가는 시간이 3~4초걸림
            prMetricInfos.subscribe(result -> {
                System.out.println("### subscribe 진입. 구독전까지 걸린 시간 초기화 후 구독 진입까지 걸린시간:"+stopWatch.getTotalTimeSeconds()+"초");
                Gson gson = new Gson();
                String gsonStr = gson.toJson(result);
                JSONObject tmpJobj = null;
                try {
                    tmpJobj = new JSONObject(gsonStr);
                    System.out.println("Call url /projects API Result : "+ tmpJobj.toString(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Asynchronus 이므로 어느것이 먼저 끝날지 모른다. 스탑워치가 돌고있으면 멈추고,
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }

                // 로그를 찍고, 스탑워치를 다시 돌려줘야 한다. Asynchronus 이므로 순서보장이 안되기 때문에.
                System.out.println(stopWatch.getTotalTimeSeconds());
                stopWatch.start();

            });

        } catch (Exception e) {
            System.out.println("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
        }
        Thread.sleep(3000);

        WebclientUtil webclientUtil = new WebclientUtil();
        // get hypervisors In a Webclient way ---> OS_QUOTA_SETS
        webClient = webclientUtil.getWebClient(osClient, OpenStackConstant.OS_QUOTA_SETS);
        // 모든 인스턴스(vm) metric info 조회.
        Mono<ServerMetricVo> serverUsageMetricInfos = null;

        try {
            serverUsageMetricInfos = webClient.get()
                    .uri("/v2.1/servers/detail?all_tenants=1&project_id=10b86d77e8f041e49b0177909c67deab")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ServerMetricVo.class);

            serverUsageMetricInfos.subscribe( result -> {
                System.out.println("Call url /v2.1/servers/detail API Result : "+ result);

                // Asynchronus 이므로 어느것이 먼저 끝날지 모른다. 스탑워치가 돌고있으면 멈추고,
                if (stopWatch.isRunning()) {
                    stopWatch.stop();
                }

                // 로그를 찍고, 스탑워치를 다시 돌려줘야 한다. Asynchronus 이므로 순서보장이 안되기 때문에.
                System.out.println(stopWatch.getTotalTimeSeconds());
                stopWatch.start();
            });
        } catch (Exception e) {
            log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
        }

        Thread.sleep(10000);
    }

    /**
     * openstack API로 Endpoint 리스트 받기.
     */
    @Test
    public void openstackCallSvcEndpointList() {

        OSClient.OSClientV3 os = null;
        List<? extends Service> services = null;
        List<? extends Endpoint> endPoints = null;

//        HashMap<String, JSONObject> serviceIdMap = new HashMap<String, JSONObject>();
        List<JSONObject> serviceObjArr = new ArrayList<>();
        JSONObject serviceJsonObj = new JSONObject();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            services = os.identity().serviceEndpoints().list();
            endPoints = os.identity().serviceEndpoints().listEndpoints();

            for(Service tmpService : services){
                tmpService = os.identity().serviceEndpoints().get(tmpService.getId());
                System.out.println("#### Y_TEST tmpService Size["+services.size()+"]");
                System.out.println("##### Y_TEST tmpService Value["+tmpService.toString()+"]");
                System.out.println("###################################################");

                serviceJsonObj.put(tmpService.getId(),tmpService.getName());

                System.out.println("## Id:"+tmpService.getId());
                System.out.println("## Name:"+tmpService.getName());
                System.out.println("## Endpoint:"+tmpService.getEndpoints());
            }

            for(Endpoint tmpEndpoint : endPoints){
//                System.out.println("#### Y_TEST tmpEndpoint Size["+endPoints.size()+"]");
//                System.out.println("#### Y_TEST tmpEndpoint Name["+tmpEndpoint.getName()+"]");
//                System.out.println("#### Y_TEST tmpEndpoint ServiceId["+tmpEndpoint.getServiceId()+"]");
//
//                System.out.println("#### Y_TEST tmpEndpoint Type["+tmpEndpoint.getType()+"]");
//                System.out.println("#### Y_TEST tmpEndpoint Description["+tmpEndpoint.getDescription()+"]");
//                System.out.println("#### Y_TEST tmpEndpoint getUrl["+tmpEndpoint.getUrl()+"]");
//                System.out.println("#### Y_TEST tmpEndpoint Links["+tmpEndpoint.getLinks()+"]");

                Iterator iter = serviceJsonObj.keys();
                JSONObject svcJSONObj = new JSONObject();
                while (iter.hasNext()){
                    if(iter.next().toString().equals(tmpEndpoint.getServiceId())){
                        System.out.println("## Y_TEST Service id exist! ["+tmpEndpoint.getServiceId()+"]");
                        System.out.println("## Y_TEST Service Name! ["+serviceJsonObj.get(tmpEndpoint.getServiceId())+"]");
                        System.out.println("## Y_TEST Service url is ["+tmpEndpoint.getUrl()+"]");
                        svcJSONObj.put("ID",tmpEndpoint.getServiceId());
                        svcJSONObj.put("NAME",serviceJsonObj.get(tmpEndpoint.getServiceId()));
                        svcJSONObj.put("END_POINT",tmpEndpoint.getUrl());

                        serviceObjArr.add(svcJSONObj);
                    }
                }
            }

            System.out.println("############################# RESULT ##################################");
            System.out.println("## Y_TEST serviceObjArr["+serviceObjArr.toString()+"]");

            Gson gson = new Gson();
            String gsonStr = serviceObjArr.toString();
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");


        }catch (NullPointerException | ConnectException | JSONException e2){
            System.out.println("## 에러발생!["+e2.getMessage()+"]");
            e2.printStackTrace();
        }
    }

    /**
     * openstack API로 Volume 리스트 받기.
     */
    @Test
    public void openstackCallVolumeList() {

        OSClient.OSClientV3 os = null;
        List<? extends Volume> volumes = null;
        List<VolumeVo> volumeVoList = new ArrayList<>();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            volumes = os.blockStorage().volumes().list();


            for(Volume tmpVolume : volumes){
                System.out.println("##### Y_TEST tmpVolume Value["+tmpVolume.toString()+"]");
                System.out.println("###################################################");

                VolumeVo vo = new VolumeVo();

                vo.setName(tmpVolume.getName());
                vo.setId(tmpVolume.getId());
                vo.setAttachments(tmpVolume.getAttachments());
                vo.setStatus(tmpVolume.getStatus());
                vo.setCreatedAt(tmpVolume.getCreated());
                vo.setDescription(tmpVolume.getDescription());
                vo.setEncrypted(tmpVolume.encrypted());
                vo.setImageRef(tmpVolume.getImageRef());
                vo.setMetaData(tmpVolume.getMetaData());
                vo.setMigrateStatus(tmpVolume.getMigrateStatus());
                vo.setSnapshotId(tmpVolume.getSnapshotId());
                vo.setSize(tmpVolume.getSize());
                vo.setSourceVolid(tmpVolume.getSourceVolid());
                vo.setTenantId(tmpVolume.getTenantId());
                vo.setZone(tmpVolume.getZone());
                vo.setVolumeType(tmpVolume.getVolumeType());
                vo.setHost(tmpVolume.host());

                volumeVoList.add(vo);
            }
            Gson gson = new Gson();
            String gsonStr = gson.toJson(volumeVoList);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");


        }catch (Exception e){
            System.out.println("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }

    /**
     * admin 권한으로 Weclient를 통해 Project 리스트 가져오기.
     */
    @Test
    public void openstackWcCallServerList() {

        OSClient.OSClientV3 os = null;
        List<? extends Project> projectArr = null;
        List<ProjectVo> projectList = new ArrayList<>();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            SslContext sslContext = SslContextBuilder
                    .forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();
            HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));

            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024 * 50)).build(); // set max memory size 1MB;
            WebClient webClient = WebClient.builder()
                    .baseUrl(OpenStackConstant.KEYSTONE_IDENTITY)
                    .defaultHeader(org.apache.http.HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .defaultHeader("X-Auth-Token", os.getToken().getId())
                    .exchangeStrategies(exchangeStrategies) // set exchange strategies
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .build();


            // get metric info
            ProjectMetricVo metricInfos = null;
            try {
                metricInfos = webClient.get()
                        .uri("/projects")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(ProjectMetricVo.class).block();

            } catch (Exception e) {
                // log
                log.error("Webclient 응답 오류 발생! [" + e.getMessage()+"]");
            }


//            projectArr = os.identity().projects().list();


//            os.blockStorage().volumes().list();
            System.out.println("## Y_TEST UnscopeConnection identity.projects.list["+metricInfos+"] size["+metricInfos.getProjects().size()+"]");
//            System.out.println("## Y_TEST UnscopeConnection identity.projects.list["+metricInfos+"]");
            for(ProjectMetricDetailVo project : metricInfos.getProjects()){
                ProjectVo vo = new ProjectVo();

                vo.setName(project.getName());
                vo.setId(project.getId());
                vo.setDesc(project.getDesc());
                vo.setEnabled((project.getLinks().getEnabled() == "false") ? false : true);
                vo.setSelf(project.getLinks().getSelf());
                vo.setRetrievedDt(nowDate);

                projectList.add(vo);
                System.out.println("##### Y_TEST project Value["+vo.toString()+"]");
                System.out.println("###################################################");
            }
//            Type listType = new TypeToken<List<ProjectVo>>() {}.getType();
            Gson gson = new Gson();
//            String gsonStr = gson.toJson(projectList, listType);
            String gsonStr = gson.toJson(projectList);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }

    /**
     * admin 권한으로 Project 리스트 가져오기.
     */
    @Test
    public void openstackCallServerList() {

        OSClient.OSClientV3 os = null;
        List<? extends Project> projectArr = null;
        List<ProjectVo> projectList = new ArrayList<>();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            projectArr = os.identity().projects().list();


//            os.blockStorage().volumes().list();

            System.out.println("## Y_TEST UnscopeConnection identity.projects.list["+os.identity().projects().list()+"]");
            for(Project project : projectArr){

                System.out.println("##### Y_TEST project Value["+project.toString()+"]");
                System.out.println("###################################################");
                ProjectVo vo = new ProjectVo();
                com.okestro.symphony.dashboard.cmm.model.ProjectVo projectVo = new com.okestro.symphony.dashboard.cmm.model.ProjectVo();

                vo.setName(project.getName());
                vo.setId(project.getId());
                vo.setDesc(project.getDescription());
                vo.setEnabled((project.getLinks().get("enabled") == "true") ? true : false);
                vo.setRetrievedDt(nowDate);

                projectVo.setProjectId(project.getId());
                projectVo.setProjectName(project.getName());
                projectVo.setEndpoint(project.getLinks().get("self"));
                projectVo.setDomain("default");

                OSClient.OSClientV3 osClient = openStackConnectionService.connect(projectVo);

                projectList.add(vo);
            }
//            Type listType = new TypeToken<List<ProjectVo>>() {}.getType();
            Gson gson = new Gson();
//            String gsonStr = gson.toJson(projectList, listType);
            String gsonStr = gson.toJson(projectArr);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }

    /**
     * admin 권한으로 Project 리스트 가져오기.
     */
    @Test
    public void openstackCallProjectList() {

        OSClient.OSClientV3 os = null;
        List<? extends Project> projectArr = null;
        List<ProjectVo> projectList = new ArrayList<>();
        // get current date time
        String nowDate = DateUtil.now();
        long epoch = System.currentTimeMillis();


        System.out.println("## Y_TEST epoch ["+epoch+"]");

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            projectArr = os.identity().projects().list();

            System.out.println("## Y_TEST UnscopeConnection identity.projects.list["+os.identity().projects().list()+"]");
            for(Project project : projectArr){
                System.out.println("##### Y_TEST project Value["+project.toString()+"]");
                System.out.println("###################################################");
                ProjectVo vo = new ProjectVo();

                vo.setName(project.getName());
                vo.setId(project.getId());
                vo.setDesc(project.getDescription());
                vo.setEnabled((project.getLinks().get("enabled") == "true") ? true : false);
                vo.setRetrievedDt(nowDate);

                projectList.add(vo);
            }
//            Type listType = new TypeToken<List<ProjectVo>>() {}.getType();
            Gson gson = new Gson();
//            String gsonStr = gson.toJson(projectList, listType);
            String gsonStr = gson.toJson(projectList);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }

    /**
     * admin 권한으로 Project 리스트 가져오기.
     */
    @Test
    public void openstackCallNodeList() {

        OSClient.OSClientV3 os = null;
        List<? extends HostResource> hostArr = null;
        List<? extends Hypervisor> hypervisorArr = null;

        List<? extends Server> servers = null;

        List<NodeVo> nodeList = new ArrayList<>();
        List<HostVo> hostVoList = new ArrayList<>();

        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
            ServerService serverService = os.compute().servers();

            System.out.println("## Y_TEST serverService Size["+serverService.list().size()+"]");
            servers = serverService.list();

            int cnt = 1;
            for(Server tmp : servers){
                System.out.println("## Y_TEST serverService getName["+cnt+"]["+tmp.getName()+"]");
                System.out.println("## Y_TEST serverService getInstanceName["+cnt+"]["+tmp.getInstanceName()+"]");
                System.out.println("## Y_TEST serverService getHypervisorHostname["+cnt+"]["+tmp.getHypervisorHostname()+"]");
                System.out.println("## Y_TEST serverService getImage["+cnt+"]["+tmp.getImage()+"]");
                System.out.println("## Y_TEST serverService getVmState["+cnt+"]["+tmp.getVmState()+"]");
                System.out.println("## Y_TEST serverService getFlavor.getName["+cnt+"]["+tmp.getFlavor().getName()+"]");
                System.out.println("#################################################################");
                cnt ++;
            }

            //hostArr
            hostArr = os.compute().host().list();
            //hypervisorArr
            hypervisorArr = os.compute().hypervisors().list();

            System.out.println("## Y_TEST UnscopeConnection compute().host().list["+hostArr+"]");
            System.out.println("## Y_TEST UnscopeConnection compute().hypervisors().list["+hypervisorArr+"]");
//            log.info("## Y_TEST UnscopeConnection compute().host().list["+hostArr+"]");
//            log.info("## Y_TEST UnscopeConnection compute().hypervisors().list["+hypervisorArr+"]");


            for(HostResource hostResource : hostArr){
                System.out.println("##### Y_TEST hostResource Value["+hostResource.toString()+"]");
                System.out.println("###################################################");
//                log.info("##### Y_TEST hostResource Value["+hostResource.toString()+"]");
//                log.info("###################################################");
                NodeVo vo = new NodeVo();

                vo.setName(hostResource.getHostName());
                nodeList.add(vo);
            }

            for(Hypervisor hypervisor : hypervisorArr){
                System.out.println("##### Y_TEST hypervisor Value["+hypervisor.toString()+"]");
                System.out.println("###################################################");
//                log.info("##### Y_TEST hypervisor Value["+hypervisor.toString()+"]");
//                log.info("###################################################");
                HostVo vo = new HostVo();

                vo.setName(hypervisor.getHypervisorHostname());
                vo.setId(hypervisor.getId());
                vo.setFreeDiskGb(hypervisor.getFreeDisk());
                vo.setFreeRam(hypervisor.getFreeRam());
                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
                vo.setHostIp(hypervisor.getHostIP());
                vo.setVcpus(hypervisor.getVirtualCPU());
                vo.setVcpusUsed(hypervisor.getVirtualUsedCPU());
                vo.setLeastDiskAvailable(hypervisor.getLeastDiskAvailable());
                vo.setState(hypervisor.getState());
                vo.setStatus(hypervisor.getStatus());
//                vo.setProviderName(hypervisor.getService().getHost()); // ?
                vo.setType(hypervisor.getType());
                vo.setHypervisorVersion(hypervisor.getVersion());
                vo.setLocalGb(hypervisor.getLocalDisk());
                vo.setMemoryMb(hypervisor.getLocalMemory());
                vo.setLocalGbUsed(hypervisor.getLocalDiskUsed());
                vo.setMemoryMbUsed(hypervisor.getLocalMemoryUsed());
                vo.setRunningVms(hypervisor.getRunningVM());
                vo.setTimestamp(nowDate);

                hostVoList.add(vo);
            }


            Gson gson = new Gson();
            String hostGsonStr = gson.toJson(nodeList);
            String hypervisorGsonStr = gson.toJson(hostVoList);

            JSONArray tmpHostJsonArr = new JSONArray(hostGsonStr);
            JSONArray tmpHypervisorJsonArr = new JSONArray(hypervisorGsonStr);



            System.out.println("========Response Host Result Convert Gson================");
            System.out.println("gson Result["+tmpHostJsonArr.toString(1)+"]");
            System.out.println("========Response Hypervisor Result Convert Gson================");
            System.out.println("gson Result["+tmpHypervisorJsonArr.toString(1)+"]");
//            log.info("========Response Host Result Convert Gson================");
//            log.info("gson Result["+tmpHostJsonArr.toString(1)+"]");
//            log.info("========Response Hypervisor Result Convert Gson================");
//            log.info("gson Result["+tmpHypervisorJsonArr.toString(1)+"]");



        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }


    /**
     * 하드코딩된 유저의 해당 프로젝트 vm 정보 조회.
     * @return
     */
    @Test
    public void openstackRetriveVmList() {

        OSClient.OSClientV3 os = null;
        VmVo vmVo = new VmVo();
        List<? extends Project> projectList = new ArrayList<>();
        List<? extends Server> servers = null;
        List<com.okestro.symphony.dashboard.cmm.model.ProjectVo> projectVoList = new ArrayList<>();
        List<VmVo> vmVoList = new ArrayList<>();
        com.okestro.symphony.dashboard.cmm.model.ProjectVo projectVo = null;

        // get current date time
        String nowDate = DateUtil.now();




        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);
            projectList = os.identity().projects().list();
            log.info("## Y_TEST projectList Size["+projectList.size()+"]");

            for(Project project : projectList){
                projectVo = new com.okestro.symphony.dashboard.cmm.model.ProjectVo();
                projectVo.setProjectId(project.getId());
                projectVo.setProjectName(project.getName());
//                projectVo.setDomain(((project.getDomain().getName() == null) ? "" : project.getDomain().getName()));
                projectVo.setEndpoint(project.getLinks().get("self"));

                System.out.println("## Y_TEST project.getLisnks.size["+project.getLinks().size()+"]");
                System.out.println("## Y_TEST project.getLisnks["+project.getLinks().get("self")+"]");


                projectVoList.add(projectVo);
            }

            ServerService serverService = os.compute().servers();


            System.out.println("## Y_TEST serverService Size["+serverService.list().size()+"]");
            servers = serverService.list();

            int cnt = 1;
            for(Server tmp : servers){
                vmVo.setUserId(tmp.getUserId());
                System.out.println("## Y_TEST serverService getName["+cnt+"]["+tmp.getName()+"]");
                System.out.println("## Y_TEST serverService getInstanceName["+cnt+"]["+tmp.getInstanceName()+"]");
                System.out.println("## Y_TEST serverService getHypervisorHostname["+cnt+"]["+tmp.getHypervisorHostname()+"]");
                System.out.println("## Y_TEST serverService getImage["+cnt+"]["+tmp.getImage()+"]");
                System.out.println("## Y_TEST serverService getVmState["+cnt+"]["+tmp.getVmState()+"]");
                System.out.println("## Y_TEST serverService getFlavor.getName["+cnt+"]["+tmp.getFlavor().getName()+"]");
                System.out.println("#################################################################");
                cnt ++;
            }




            Gson gson = new Gson();
            String vmGsonStr = gson.toJson(vmVoList);

            JSONArray tmpVmJsonArr = new JSONArray(vmGsonStr);



            System.out.println("========Response Vm Result Convert Gson================");
            System.out.println("gson Result["+tmpVmJsonArr.toString(1)+"]");


        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }




    /**
     * admin 권한으로 hypervisor 리스트 가져오기.
     */
    @Test
    public void openstackCallHypervisorList() {

        OSClient.OSClientV3 os = null;
        List<? extends Hypervisor> hyperArr = null;
        List<HostVo> hyperList = new ArrayList<>();

        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            ServerService server = os.compute().servers();

            System.out.println("## Y_TEST servers.listAll.size ["+server.listAll(false).size()+"]");
            System.out.println("## Y_TEST servers.list.size ["+server.list().size()+"]");
            System.out.println("## Y_TEST servers.list ["+server.list()+"]");


            hyperArr = os.compute().hypervisors().list();


            System.out.println("## Y_TEST UnscopeConnection compute.hypervisors.list["+os.compute().hypervisors().list()+"]");
            for(Hypervisor hypervisor : hyperArr){
                System.out.println();
                System.out.println("##### Y_TEST hypervisors Value["+hypervisor.toString()+"]");
                System.out.println("###################################################");

                HostVo vo = new HostVo();

                vo.setId(hypervisor.getId());
                vo.setName(hypervisor.getHypervisorHostname());
                vo.setType(hypervisor.getType());
                vo.setStatus(hypervisor.getStatus());
                vo.setState(hypervisor.getState());
                vo.setHostIp(hypervisor.getHostIP());
                vo.setCurrentWorkload(hypervisor.getCurrentWorkload());
                vo.setLeastDiskAvailable(hypervisor.getLeastDiskAvailable());
                vo.setHypervisorVersion(hypervisor.getVersion());
                vo.setRunningVms(hypervisor.getRunningVM());
                vo.setVcpus(hypervisor.getVirtualCPU());
                vo.setVcpusUsed(hypervisor.getVirtualUsedCPU());
                vo.setLocalGb(hypervisor.getLocalDisk());
                vo.setLocalGbUsed(hypervisor.getLocalDiskUsed());
                vo.setFreeDiskGb(hypervisor.getFreeDisk());
                vo.setMemoryMb(hypervisor.getLocalMemory());
                vo.setMemoryMbUsed(hypervisor.getLocalMemoryUsed());
                vo.setFreeRam(hypervisor.getFreeRam());

                hyperList.add(vo);
            }
            Gson gson = new Gson();
            String gsonStr = gson.toJson(hyperList);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }


    /**
     * admin 권한으로 InstanceType 리스트 가져오기.
     */
    @Test
    public void openstackCallInstanceTypeList() {

        OSClient.OSClientV3 os = null;
        List<? extends Flavor> flavors = null;
        List<FlavorVo> flavorList = new ArrayList<>();
        // get current date time
        String nowDate = DateUtil.now();

        try{
            os = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            flavors = os.compute().flavors().list();

            System.out.println("## Y_TEST UnscopeConnection compute.flavors.list["+os.compute().flavors().list()+"]");
            for(Flavor item : flavors){
                System.out.println("##### Y_TEST flavors Value["+item.toString()+"]");
                System.out.println("###################################################");
                FlavorVo flavorVo = new FlavorVo();

                flavorVo.setName(item.getName());
                flavorVo.setId(item.getId());
                flavorVo.setRam(item.getRam());
                flavorVo.setVcpus(item.getVcpus());
                flavorVo.setDisk(item.getDisk());
                flavorVo.setEphemeral(item.getEphemeral());
                flavorVo.setSwap(item.getSwap());
                flavorVo.setRxtxFactor(item.getRxtxFactor());
                flavorVo.setPublic(item.isPublic());
                flavorVo.setDisabled(item.isDisabled());

                flavorList.add(flavorVo);
            }
            Gson gson = new Gson();
            String gsonStr = gson.toJson(flavorList);
            JSONArray tmpJArr = new JSONArray(gsonStr);


            System.out.println("========Response Convert Gson================");
            System.out.println("gson Result["+tmpJArr.toString(1)+"]");

        }catch (Exception e){
            log.error("## 에러발생!["+e.getMessage()+"]");
            e.printStackTrace();
        }
    }


    /**
     * retrieve summary
     */
    @Test
    public void retrieveSummary() {
        SummaryVo summary = new SummaryVo();
        String resultStr = null;
        List<? extends AvailabilityZone> availabilityZones = new ArrayList<>();


        try {
            int vcpus = 0, ram = 0;

            // connect to openstack
            OSClient.OSClientV3 osClient = OpenstackService.UnscopeConnectTestMethod(openStackConnectionService);

            List<? extends Region> regionList = osClient.identity().regions().list();


//            System.out.println("## Y_TEST projectVo");

            // get project id
            String projectId = osClient.getToken().getProject().getId();
            System.out.println("## project Name ["+osClient.getToken().getProject().getName()+"]");
            System.out.println("## project ID ["+projectId+"]");
//            System.out.println("## project ID ["+projectId+"]");
            availabilityZones = osClient.compute().zones().list();
            System.out.println("## Y_TEST ZoneListSize["+osClient.compute().zones().list().size()+"]");

            for(AvailabilityZone availabilityZone : availabilityZones){
                System.out.println("## Y_TEST availabilityZone Name ["+availabilityZone.getZoneName()+"]");
                System.out.println("## Y_TEST availabilityZone ZoneState["+availabilityZone.getZoneState()+"]");
                System.out.println("## Y_TEST availabilityZone ZoneHost["+availabilityZone.getHosts()+"]");
            }


            // get servers
            List<? extends Server> servers = osClient.compute().servers().list();
            Gson gson = new Gson();
            try {
                String gsonStr = gson.toJson(servers);
                JSONArray tmpJArr = new JSONArray(gsonStr);
                resultStr = tmpJArr.toString(1);

            }catch (JSONException e){
                log.error(e.getMessage(),e);
            }
            System.out.println("### server List["+resultStr+"]");
//            System.out.println("### server List["+convertDataUtil.convertListDataJsonArrToStr(servers)+"]");

            for (Server server : servers) {
                vcpus = vcpus + server.getFlavor().getVcpus();
                ram = ram + server.getFlavor().getRam();
            }

            // get compute quota
            QuotaSet quotaSet = osClient.compute().quotaSets().get(projectId);
            summary.setInstances(quotaSet.getInstances());
            summary.setInstancesUsed(osClient.compute().servers().list().size());
            summary.setVcpus(quotaSet.getCores());
            summary.setVcpusUsed(vcpus);
            summary.setRam(quotaSet.getRam());
            summary.setRamUsed(ram);

            // get network quota
            NetQuota netQuota = osClient.networking().quotas().get(projectId);
            summary.setIps(netQuota.getFloatingIP());
            summary.setIpsUsed(osClient.networking().floatingip().list(Collections.unmodifiableMap(new HashMap<String, String>() {{put("tenant_id", projectId);}})).size());
//            summary.setSecurityGroups(netQuota.getSecurityGroup());
//            summary.setSecurityGroupsUsed(osClient.compute().securityGroups().list().size());
//            summary.setSecurityGroupRules(netQuota.getSecurityGroupRule());
//            summary.setSecurityGroupRulesUsed(osClient.networking().securityrule().list(Collections.unmodifiableMap(new HashMap<String, String>() {{put("tenant_id", projectId);}})).size());

            // get block storage quota
            BlockQuotaSet blockQuotaSet = osClient.blockStorage().quotaSets().get(projectId);
            summary.setVolumes(blockQuotaSet.getVolumes());
            summary.setVolumesUsed(osClient.blockStorage().volumes().list().size());
            summary.setGigabytes(blockQuotaSet.getGigabytes());
            summary.setGigabytesUsed(osClient.blockStorage().getLimits().getAbsolute().getTotalGigabytesUsed());


            try {
                String gsonStr = gson.toJson(summary);
                JSONObject tmpJArr = new JSONObject(gsonStr);
                resultStr = tmpJArr.toString(1);

            }catch (JSONException e){
                log.error(e.getMessage(),e);
            }

//            System.out.println("## Y_TEST Dashboard Data Summary result["+resultStr+"]");
            System.out.println("## Y_TEST Dashboard Data Summary result["+resultStr+"]");
        } catch (ConnectException e)  {
            // log
            log.error(e.getMessage());
        }

//        return summary;
    }


    private QuotaComputeVo getQuotaCompute(String projectId, ComputeService compute) {
        QuotaSet computeQuota = compute.quotaSets().get(projectId);
//        SimpleTenantUsage computeUsage = compute.quotaSets().getTenantUsage(projectId);

        QuotaComputeVo result = new QuotaComputeVo();
        result.setInstanceQuota(computeQuota.getInstances());
//        result.setInstanceUsed((computeUsage.getServerUsages() == null ? 0 : computeUsage.getServerUsages().size()));
        result.setCoreQuota(computeQuota.getCores());
//        result.setCoreUsed((computeUsage.getTotalVcpusUsage() == null ? 0.0 : Double.parseDouble(String.format("%.12f",computeUsage.getTotalVcpusUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue()))));
        result.setRamQuota(computeQuota.getRam());
//        result.setRamUsed(computeUsage.getTotalMemoryMbUsage()== null ? 0.0 : Double.parseDouble(String.format("%.12f",computeUsage.getTotalMemoryMbUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue())));

        result.setMetadataItemsQuota(computeQuota.getMetadataItems());
        result.setInjectFilesQuota(computeQuota.getInjectedFiles());
        result.setInjectedFileContentBytesQuota(computeQuota.getInjectedFileContentBytes());
        result.setInjectedFilePathBytesQuota(computeQuota.getInjectedFilePathBytes());
        result.setKeyPairsQuota(computeQuota.getKeyPairs());

        return result;

    }

    private UsageComputeVo getUsageCompute(String projectId, ComputeService compute) {
//        QuotaSet computeQuota = compute.quotaSets().get(projectId);
        SimpleTenantUsage computeUsage = compute.quotaSets().getTenantUsage(projectId);

        UsageComputeVo result = new UsageComputeVo();
//        result.setInstanceQuota(computeQuota.getInstances());
        result.setInstanceUsed((computeUsage.getServerUsages() == null ? 0 : computeUsage.getServerUsages().size()));
//        result.setCoreQuota(computeQuota.getCores());
        result.setCoreUsed((computeUsage.getTotalVcpusUsage() == null ? 0 : Integer.parseInt(String.format("%.12f",computeUsage.getTotalVcpusUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue()))));
//        result.setRamQuota(computeQuota.getRam());
        result.setRamUsed(computeUsage.getTotalMemoryMbUsage()== null ? 0 : Integer.parseInt(String.format("%.12f",computeUsage.getTotalMemoryMbUsage().setScale(12, BigDecimal.ROUND_CEILING).doubleValue())));


        return result;

    }


    private QuotaNetworkVo getQuotaNetwork(String projectId, NetworkingService networking) {
        NetQuota netQuota = networking.quotas().get(projectId);

        QuotaNetworkVo result = new QuotaNetworkVo();

        result.setSubnetQuota(netQuota.getSubnet());
        result.setRouterQuota(netQuota.getRouter());
        result.setPortQuota(netQuota.getPort());
        result.setNetworkQuota(netQuota.getNetwork());
        result.setFloatingIpQuota(netQuota.getFloatingIP());
        result.setSecurityGroupQuota(netQuota.getSecurityGroup());
        result.setSecurityGroupRuleQuota(netQuota.getSecurityGroupRule());

        return result;
    }


    private QuotaVolumnVo getQuotaVolumn(String projectId, BlockStorageService blockStorage) {
        BlockQuotaSet blockQuotaSet = blockStorage.quotaSets().get(projectId);
//        BlockQuotaSetUsage blockQuotaSetUsage = blockStorage.quotaSets().usageForTenant(projectId);


        QuotaVolumnVo result = new QuotaVolumnVo();
        result.setVolumeQuota(blockQuotaSet.getVolumes());
        result.setSnapshotQuota(blockQuotaSet.getSnapshots());
        result.setGigabyteQuota(blockQuotaSet.getGigabytes());

        return result;
    }

    private UsageVolumnVo getUsageVolumn(String projectId, BlockStorageService blockStorage) {
//        BlockQuotaSet blockQuotaSet = blockStorage.quotaSets().get(projectId);
        BlockQuotaSetUsage blockQuotaSetUsage = blockStorage.quotaSets().usageForTenant(projectId);


        UsageVolumnVo result = new UsageVolumnVo();
        result.setVolumeUsed(blockQuotaSetUsage.getVolumes().getInUse());
        result.setSnapshotUsed(blockQuotaSetUsage.getSnapshots().getInUse());
        result.setGigabyteUsed(blockQuotaSetUsage.getGigabytes().getInUse());

        return result;
    }

    private VmVo getInstance(Server server, String projectName) {
        VmVo instance = new VmVo();

        //        instance.setProviderId(providerId);
//        instance.setProviderName(providerName);
        instance.setProjectName(projectName);

        instance.setHostId(server.getHostId());
        instance.setHostName(server.getHost());

        instance.setId(server.getId());
        instance.setName(server.getName());
        instance.setPowerStatus(server.getPowerState());
        instance.setStatus(server.getStatus().value());
        instance.setTaskState(server.getTaskState());

//        System.out.println("server.getPowerState:" + server.getPowerState()); // 전원상태
//        System.out.println("server.getStatus().value:" + server.getStatus().value()); // 상태
//        System.out.println("server.getTaskState:" + server.getTaskState()); // 작업 - 사용할까?

//        instance.setDisk(server.getDiskConfig().name()); // 사용 안함
        instance.setHostId(server.getHostId());
        instance.setHostName(server.getHost());
        instance.setAvailabilityZone(server.getAvailabilityZone());
//        instance.setAvailabilityZone("nova"); // 임시로 고정
//        instance.setCreated(server.getCreated().getTime());
        instance.setLaunched(server.getLaunchedAt() != null ? server.getLaunchedAt().getTime() : 0);
        instance.setImageId(server.getImageId());
        instance.setFlavorId(server.getFlavorId());
        instance.setKeyName(server.getKeyName());

        // 20.11.17 jd.eom 볼륨일 경우 이미지 가져오기
        List<String> volumeList = server.getOsExtendedVolumesAttached();
        if(volumeList!=null && volumeList.size()>0){
//            String mainVolumeId = volumeList.get(0);
//            instance.setVolumeId(mainVolumeId);
            instance.setVolumeId(volumeList);

        }

        // 20.10.15 jd.eom 보안그룹 가져오기위함.
        List<? extends SecurityGroup> sgExList = server.getSecurityGroups();
        List<SecGroupVo> sgList = new ArrayList<>();
        if(sgExList!=null){
            for(SecurityGroup vo : sgExList) {
                SecGroupVo sgVo = new SecGroupVo();
                sgVo.setName(vo.getName());
                sgList.add(sgVo);
            }
        }
        instance.setSecGroupList(sgList);

//        instance.setImageInfo(server.getImage() != null ? new String[]{server.getImage().getId(), server.getImage().getName()} : new String[]{"-", "-"});
//        instance.setFlavorInfo(new String[]{server.getFlavor().getId(), server.getFlavor().getName()});

        server.getAddresses().getAddresses().forEach((key, value) -> {
            for (Address address : value) {
                instance.setMacAddress(address.getMacAddr());
                if (address.getType().equals("fixed")) {
                    List<String> fixedAddresses = instance.getFixedIpAddresses() == null ? new ArrayList<>() : instance.getFixedIpAddresses();
                    fixedAddresses.add(address.getAddr());
                    instance.setFixedIpAddresses(fixedAddresses);
                } else if (address.getType().equals("floating")) {
                    List<String> floatingAddresses = instance.getFloatingIpAddresses() == null ? new ArrayList<>() : instance.getFloatingIpAddresses();
                    floatingAddresses.add(address.getAddr());
                    instance.setFloatingIpAddresses(floatingAddresses);
                }

            }
        });

        return instance;
    }

    public RestHighLevelClient elasticsearchClient() {

        //@ TODO-useSsl, withBasicAuth
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("100.0.0.163:9200")
                .withSocketTimeout(Duration.ofSeconds(30))
                .withBasicAuth("elastic","okestro2018")
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

}
