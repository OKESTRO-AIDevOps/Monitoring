package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectPK implements Serializable {
    private String projectId;
    private String providerId;
    private Date collectDt;
}
