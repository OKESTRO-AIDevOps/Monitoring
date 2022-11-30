package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SecurityGroupVo extends CommonVo {

    private int index; /**화면에서 추가 삭제 용도**/

    private String id;
    private String tenantId;
    private String description;
    private String name;
    private List<RuleVo> rules;
}
