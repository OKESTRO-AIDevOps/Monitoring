package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectVo extends CommonVo {

    private String id;
    private String timestamp;
    private String name;
    private String desc;
    private String providerId;
    private boolean enabled;
    private String domainId;
    private String domainNm;
    private String self;

    private String retrievedDt;

    private boolean adminRoleBind;

}
