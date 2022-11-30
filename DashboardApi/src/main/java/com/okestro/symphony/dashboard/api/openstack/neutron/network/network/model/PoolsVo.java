package com.okestro.symphony.dashboard.api.openstack.neutron.network.network.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PoolsVo extends CommonVo {
    private String crud;
    private String start;
    private String end;
    private int number;
    private Boolean selectAt;
}
