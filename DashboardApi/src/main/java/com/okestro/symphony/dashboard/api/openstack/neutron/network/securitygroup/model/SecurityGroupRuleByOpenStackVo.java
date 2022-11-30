/*
 * Developed by yy.go@okestro.com on 2020-07-26
 * Last modified 2020-07-26 17:14:26
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;

import com.google.gson.annotations.SerializedName;
import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityGroupRuleByOpenStackVo extends CommonVo {
    private String direction;
    private String protocol;
    private String description;
    private List<String> tags;
    private int portRangeMax;
    private int portRangeMin;
    private String updatedAt;
    private int revisionNumber;
    private String id;
    private String remoteGroupId;
    private String remoteIpPrefix;
    private String createdAt;

    @SerializedName("ethertype")
    private String etherType;

    private String projectId;
    private String name;
    private String crud;

}
