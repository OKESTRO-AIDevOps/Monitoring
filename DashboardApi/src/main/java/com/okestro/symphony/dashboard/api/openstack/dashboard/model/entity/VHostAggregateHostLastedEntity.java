package com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "V_HOST_AGGREGATE_HOST_LASTED")
@IdClass(VHostAggregateHostLastedPK.class)
@NoArgsConstructor
public class VHostAggregateHostLastedEntity implements Serializable {
    @Id
    @Column(name = "HOST_AGGREGATE_ID", length = 36)
    private String hostAggregateId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Id
    @Column(name = "HOST_ID", length = 36)
    private String hostId;

    @Column(name = "HOST_NAME", length = 255)
    private String hostName;

    @Column(name = "PROVIDER_NAME", length = 255)
    private String providerName;

    @Column(name = "AZ_NAME", length = 255)
    private String azName;
}
