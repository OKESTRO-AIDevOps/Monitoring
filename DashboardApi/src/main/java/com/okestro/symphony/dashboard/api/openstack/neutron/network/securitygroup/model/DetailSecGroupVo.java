/*
 * Developed by yy.go@okestro.com on 2020-07-26
 * Last modified 2020-07-26 18:13:12
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DetailSecGroupVo extends CommonVo {
    private String description;
    private List<String> tags;
    private String tenantId;
    private String createdAt;
    private String updatedAt;
    private String name;
    private int revisionNumber;
    private String projectId;
    private String id;
    private List<SecurityGroupRuleByOpenStackVo> securityGroupRules;
}
