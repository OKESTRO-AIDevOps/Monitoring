/*
 * Developed by bhhan@okestro.com on 2021-02-03
 * Last Modified 2020-02-10 11:14:27
 */

package com.okestro.symphony.dashboard.api.elastic.vm.model.openstack.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import java.util.LinkedHashMap;


@Data
public class ProjectInfoVo {

    private String id;
    private String timestamp;

    private String projectId;
    private String name;
    private String domainId;
    private String description;
    private boolean enabled;
    private String parentId;
    private boolean isDomain;
    private LinkedHashMap<String,Object> tags;
    private LinkedHashMap<String,Object> options;
    private LinkedHashMap<String,Object> links;


}
