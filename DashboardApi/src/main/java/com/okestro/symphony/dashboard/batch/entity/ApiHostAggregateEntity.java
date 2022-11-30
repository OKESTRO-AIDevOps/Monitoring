package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "HOST_AGGREGATE")
@IdClass(ApiHostAggregatePK.class)
@NoArgsConstructor
public class ApiHostAggregateEntity implements Serializable {

    @Id
    @Column(name = "HOST_AGGREGATE_ID", length = 36)
    private String hostAggregateId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "HOST_AGGREGATE_NAME", length = 255)
    private String hostAggregateName;

    @Column(name = "AZ_NAME", length = 255)
    private String azName;

}