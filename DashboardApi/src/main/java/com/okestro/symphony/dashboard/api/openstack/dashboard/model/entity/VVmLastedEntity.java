package com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "V_VM_LASTED")
@IdClass(VVmLastedPK.class)
@NoArgsConstructor
public class VVmLastedEntity implements Serializable {
    @Id
    @Column(name = "VM_ID", length = 36)
    private String vmId;

    @Column(name = "VM_NAME", length = 255)
    private String vmName;

    @Column(name = "PROJECT_ID", length = 36)
    private String projectId;

    @Column(name = "PROJECT_NAME", length = 255)
    private String projectName;

    @Column(name = "HOST_ID", length = 60)
    private String hostId;

    @Column(name = "HOST_NAME", length = 255)
    private String hostName;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Column(name = "PROVIDER_NAME", length = 255)
    private String providerName;

    @Column(name = "CREAT_DT", length = 50)
    private Date creatDt;

    @Column(name = "AZ_NAME", length = 255)
    private String azName;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "POWER_STATE", length = 16)
    private String powerState;

    @Column(name = "VM_STATE", length = 16)
    private String vmState;

    @Column(name = "CPU", length = 20)
    private int cpu;

    @Column(name = "RAM", length = 20)
    private int ram;

    @Column(name = "DISK", length = 20)
    private int disk;















}
