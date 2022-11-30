package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "nova_api.aggregate_metadata")
@NoArgsConstructor
public class NovaApiAggregateMetadataEntity implements Serializable {
    @Id
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="id")
//    private NovaApiAggregatesEntity aggregates;
    @Column(name = "aggregate_id")
    private Integer aggregateId;

    @Column(length = 255)
    private String key;

    @Column(length = 255)
    private String value;

//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "trigger_name", referencedColumnName = "sched_name", insertable = false, updatable = false)
//    private SchedulerJobInfo schedulerJobInfo;
}
