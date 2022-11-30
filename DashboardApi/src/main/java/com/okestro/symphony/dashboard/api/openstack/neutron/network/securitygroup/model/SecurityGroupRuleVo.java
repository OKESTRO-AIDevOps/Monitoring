/*
 * Developed by yy.go@okestro.com on 2020-07-16
 * Last modified 2020-07-16 17:35:20
 */

package com.okestro.symphony.dashboard.api.openstack.neutron.network.securitygroup.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityGroupRuleVo extends CommonVo {

    private String direction;
    private String protocol; /*프로토콜*/
    private String description;  /*설명*/
    private List<String> tags;
    private int portRangeMax; /*포트*/
    private int portRangeMin;
    private String updatedAt;
    private int revisionNumber;
    private String id; /*아이디*/
    private String remoteGroupId;
    private String remoteIpPrefix;
    private String createdAt;
    private String etherType;
    private String projectId;
    private String name;
    private String crud ;


    /**
     * 보안 그룹 규칙 삭제 시 필요한 id값
     */
    private String deleteTargetId;

//    private String id;
//    private IPProtocol protocol; /*프로토콜*/
//    private String cidr; /*접근 소스*/
//    private int port; /*포트*/
//    private String description; /*설명*/
}
