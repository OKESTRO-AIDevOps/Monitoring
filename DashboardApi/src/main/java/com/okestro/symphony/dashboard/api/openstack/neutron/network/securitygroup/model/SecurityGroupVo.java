/*
 * Developed by yy.go@okestro.com on 2020-07-16
 * Last modified 2020-07-16 17:34:35
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;


import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;
import org.openstack4j.model.compute.IPProtocol;

import java.util.List;

@Getter
@Setter
public class SecurityGroupVo extends CommonVo {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private IPProtocol protocol;
    private List<SecurityGroupRuleVo> secGroupRule;
    private List<SecurityGroupRuleVo> delSecRules;
    private List<SecurityGroupRuleVo> addedSecRules;
    private int numberOfRules;
    private String crud ;

    private List<SecurityGroupRuleByOpenStackVo> securityGroupRules;

    /**
     * openstack4j 에는 없으나 openstack API를 통해서 얻을수 있는 정보
     */
    private List<String> tags;
    private String createdAt;
    private String updatedAt;
    private String projectId;
    private int revisionNumber;



}
