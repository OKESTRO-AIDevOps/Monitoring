package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Data

@Table(name = "neutron.quotas")
public class NeutronQuotaEntity implements Serializable {
    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "project_id", length = 255)
    private String projectId;

    @Column(name = "resource", length = 255)
    private String resource;

    @Column(name = "limit")
    private Integer hardLimit;
}