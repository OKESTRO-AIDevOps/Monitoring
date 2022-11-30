package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VM")
@IdClass(ComputeVmPK.class)
@NoArgsConstructor
public class ComputeVmEntity implements Serializable {
    @Id
    @Column(name = "VM_ID", length = 36)
    private String vmId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "VM_NAME", length = 255)
    private String vmName;

    @Column(name = "PROVIDER_NAME", length = 255)
    private String providerName;

    @Column(name = "PROJECT_ID", length = 36)
    private String projectId;

    @Column(name = "PROJECT_NAME", length = 36)
    private String projectName;

    @Column(name = "HOST_ID", length = 36)
    private String hostId;

    @Column(name = "HOST_NAME", length = 18)
    private String hostName;

    @Column(name = "AZ_NAME", length = 255)
    private String azName;

    @Column(name = "CREAT_DT", length = 50)
    private Date creatDt;

    @Column(name = "POWER_STATE", length = 16)
    private String powerState;

    @Column(name = "VM_STATE", length = 16)
    private String vmState;

    @Column(name = "CPU")
    private Integer cpu;

    @Column(name = "RAM")
    private Integer ram;

    @Column(name = "DISK")
    private Integer disk;


}
