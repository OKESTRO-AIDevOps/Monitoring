package com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "V_HOST_LASTED")
@IdClass(VHostLastedPK.class)
@NoArgsConstructor
public class VHostLastedEntity implements Serializable {

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

    @Column(name = "PROVIDER_NAME", length = 255)
    private String providerName;

    @Column(name = "VCPU_TOTAL", length = 20)
    private int vcpuTotal;

    @Column(name = "VCPU_USAGE", length = 20)
    private int vcpuUsage;

    @Column(name = "RAM_TOTAL", length = 20)
    private int ramTotal;

    @Column(name = "RAM_USAGE", length = 20)
    private int ramUsage;

    @Column(name = "LOCAL_STORAGE_TOTAL", length = 20)
    private int localStorageTotal;

    @Column(name = "LOCAL_STORAGE_USAGE", length = 20)
    private int localStorageUsage;

    @Column(name = "VM_NUM", length = 11)
    private int vmNum;

    @Column(name = "STATE", length = 16)
    private String state;
}
