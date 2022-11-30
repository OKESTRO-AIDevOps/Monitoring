package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "keystone.project")//, schema = "keystone"
public class KeystoneProjectEntity implements Serializable {

    @Id
    @Column(length = 64)
    private String id;
    @Column(name = "name", length = 64)
    private String name;
    @Column(name = "extra")
    private String extra;
    @Column(name = "description")
    private String description;
    @Column(name = "enabled")
    private int enabled;
    @Column(name = "domain_id", length = 64)
    private String domainId;
    @Column(name = "parent_id", length = 64)
    private String parentId;
    @Column(name = "is_domain")
    private int isDomain = 0;
}
