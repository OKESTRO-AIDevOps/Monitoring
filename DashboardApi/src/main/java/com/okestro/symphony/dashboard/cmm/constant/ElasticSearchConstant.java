/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.constant;

public class ElasticSearchConstant {

    // endpoints
    public static final String ES_UPDATE_HYPERVISOR = "http://localhost:8081/symphony/dashboard/api/es/index/hypervisor";
    public static final String ES_UPDATE_PROJECT = "http://localhost:8081/symphony/dashboard/api/es/index/projects";
    public static final String ES_UPDATE_PROJECT_DETAIL = "http://localhost:8081/symphony/dashboard/api/es/index/projectDetail";
    public static final String ES_UPDATE_VM = "http://localhost:8081/symphony/dashboard/api/es/index/vm";

    // openstack index names
    public static final String ES_INDEX_META_PROJECT = "ost-metric-projects";
    public static final String ES_INDEX_META_PROJECT_DETAIL = "ost-metric-projects-detail";
    public static final String ES_INDEX_META_HYPERVISOR = "ost-metric-hypervisor";


    // elasticsearch에 저장한 openstack meta info type
    public static final String META_HYPERVISOR = "metaHypervisor";
    public static final String META_HOST = "metaHost";
    public static final String META_PROJECT = "metaProject";
    public static final String META_PROJECT_DETAIL = "metaProjectDetail";

    // elasticsearch에 저장한 openstack metric info type
    public static final String HYPERVISOR_METRIC = "hypervisor";
    public static final String PROJECT_METRIC_CPU = "CpuAgg";
    public static final String PROJECT_METRIC_RAM = "RamAgg";
    public static final String PROJECT_METRIC_DISK = "DiskAgg";
    public static final String PROJECT_METRIC_INSTANCE = "InstanceAgg";
    public static final String PROJECT_METRIC_NETWORK = "NetworkAgg";

    // operation state
    public static final String SUCCEEDED = "SUCCEEDED";
    public static final String FAILED = "FAILED";

    // openstack filter
    public static final String OS_FILTER_IMAGE_LIMIT = "limit";
    public static final String OS_FILTER_IMAGE_NAME = "name";
    public static final String OS_FILTER_PROJECT_ID = "project_id";


    // result message
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String EXIST = "exist";
    public static final String FALSE ="false";
    public static final String TRUE ="true";







}
