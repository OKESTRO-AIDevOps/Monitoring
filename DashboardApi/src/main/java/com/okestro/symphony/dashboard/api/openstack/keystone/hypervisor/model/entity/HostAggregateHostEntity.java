package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "HOST_AGGREGATE_HOST")
@IdClass(HostAggregateHostPK.class)
@NoArgsConstructor
public class HostAggregateHostEntity implements Serializable {

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

}
