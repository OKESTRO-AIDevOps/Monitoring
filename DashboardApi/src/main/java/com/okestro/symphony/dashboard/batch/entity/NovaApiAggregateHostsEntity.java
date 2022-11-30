package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "nova_api.aggregate_hosts")
@NoArgsConstructor
public class NovaApiAggregateHostsEntity implements Serializable {
    @Id
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(length = 255)
    private String host;

    @Column(name = "aggregate_id")
    private Integer aggregateId;
}
