package com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "HOST")
@IdClass(HostPK.class)
@NoArgsConstructor
public class HostEntity implements Serializable {

    @Id
    @Column(name = "HOST_ID", length = 36)
    private String hostId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "HOST_NAME", length = 255)
    private String hostName;

    @Column(name = "VCPU_TOTAL")
    private int vcpuTotal;

    @Column(name = "VCPU_USAGE")
    private int vcpuUsage;

    @Column(name = "RAM_TOTAL")
    private int ramTotal;

    @Column(name = "RAM_USAGE")
    private int ramUsage;

    @Column(name = "LOCAL_STORAGE_TOTAL")
    private int localStorageTotal;

    @Column(name = "LOCAL_STORAGE_USAGE")
    private int localStorageUsage;

    @Column(name = "VM_NUM")
    private int vmNum;

    @Column(name = "STATE", length = 16)
    private String state;


}
