package com.okestro.symphony.dashboard.batch.entity;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "nova_api.aggregates")
@NoArgsConstructor
public class NovaApiAggregatesEntity implements Serializable {
    @Id
    private Integer id;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;


    @Column(length = 255)
    private String name;

    @Column(length = 36)
    private String uuid;

    /**
     * SYMPHONY.AZ_NAME 값을 위해 만들어 둔 필드
     * */
    @Transient
    private String azZone;

//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name="aggregate_id")
//    private NovaApiAggregateMetadataEntity aggregateMetadata;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="id")
//    private NovaApiAggregatesEntity aggregates;

//    @Column(name = "deleted_at")
//    private Date deletedAt;
//    @Column
//    private Integer deleted;
}
