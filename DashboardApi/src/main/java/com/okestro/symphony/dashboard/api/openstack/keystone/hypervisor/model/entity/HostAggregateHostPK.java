package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class HostAggregateHostPK implements Serializable {
    private String hostAggregateId;
    private String hostId;
    private String providerId;
    private Date collectDt;
}
