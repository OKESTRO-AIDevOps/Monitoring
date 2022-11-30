package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "nova.floating_ips")
public class NovaFloatingIpsEntity implements Serializable {
    @Id
    private Integer id;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
    @Column(name = "address", length = 39)
    private String address;
    @Column(name = "fixed_ip_id")
    private Integer fixedIpId;
    @Column(name = "project_id", length = 255)
    private String projectId;
    @Column(name = "host", length = 255)
    private String host;
    @Column(name = "auto_assigned")
    private Integer autoAssigned;
    @Column(name = "pool", length = 255)
    private String pool;
    @Column(name = "interface", length = 255) // interface 예약어 사용불가
    private String interfaceNm;
    @Column(name = "deleted")
    private Integer deleted;
//    @Column(name = "")
//    private ;
}

