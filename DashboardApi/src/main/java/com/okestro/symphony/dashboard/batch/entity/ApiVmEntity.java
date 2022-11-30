package com.okestro.symphony.dashboard.batch.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "VM")
@IdClass(ApiVmPK.class)
@NoArgsConstructor
public class ApiVmEntity implements Serializable {

    @Id
    @Column(name = "VM_ID", length = 36)
    private String vmId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "PROVIDER_NAME", length = 255)
    private String providerName;

    @Column(name = "CREAT_DT")
    private Date createDt;

    @Column(name = "HOST_ID", length = 60)
    private String hostId;

    @Column(name = "HOST_NAME", length = 18)
    private String hostName;

    @Column(name = "PROJECT_NAME", length = 36)
    private String projectName;

    @Column(name = "PROJECT_ID", length = 36)
    private String projectId;

    @Column(name = "VM_NAME", length = 255)
    private String vmName;

    @Column(name = "AZ_NAME", length = 255)
    private String azName;

    @Column(name = "POWER_STATE", length = 16)
    private String powerState;

    @Column(name = "VM_STATE", length = 16)
    private String vmState;

    @Column
    private Integer cpu;
    @Column
    private Integer ram;
    @Column
    private Integer disk;

}
