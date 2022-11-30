package com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Data
@Table(name = "PROJECT_QUOTA")
@IdClass(ProjectPK.class)
@NoArgsConstructor
public class ProjectQuotaEntity implements Serializable {

    @Id
    @Column(name = "PROJECT_ID", length = 36)
    private String projectId;

    @Id
    @Column(name = "PROVIDER_ID", length = 36)
    private String providerId;

    @Id
    @Column(name = "COLLECT_DT", length = 50)
    private Date collectDt;

    @Column(name = "INSTANCE_QUOTA", length = 36)
    private int instanceQuota;

    @Column(name = "INSTANCE_USAGE", length = 20)
    private int instanceUsage;

    @Column(name = "CPU_QUOTA", length = 20)
    private int cpuQuota;

    @Column(name = "CPU_USAGE", length = 20)
    private int cpuUsage;

    @Column(name = "RAM_QUOTA", length = 20)
    private int ramQuota;

    @Column(name = "RAM_USAGE", length = 20)
    private int ramUsage;

    @Column(name = "VOLUME_QUOTA", length = 20)
    private int volumeQuota;

    @Column(name = "VOLUME_USAGE", length = 20)
    private int volumeUsage;

    @Column(name = "SNAPSHOT_QUOTA", length = 20)
    private int snapshotQuota;

    @Column(name = "SNAPSHOT_USAGE", length = 20)
    private int snapshotUsage;

    @Column(name = "STORAGE_QUOTA", length = 20)
    private int storageQuota;

    @Column(name = "STORAGE_USAGE", length = 20)
    private int storageUsage;

    @Column(name = "NETWORK_QUOTA", length = 20)
    private int networkQuota;

    @Column(name = "NETWORK_USAGE", length = 20)
    private int networkUsage;

    @Column(name = "FLOATING_IP_QUOTA", length = 20)
    private int floatingIpQuota;

    @Column(name = "FLOATING_IP_USAGE", length = 20)
    private int floatingIpUsage;

    @Column(name = "SECURITY_GROUP_QUOTA", length = 20)
    private int securityGroupQuota;

    @Column(name = "SECURITY_GROUP_USAGE", length = 20)
    private int securityGroupUsage;

    @Column(name = "SECURITY_RULE_QUOTA", length = 20)
    private int securityRuleQuota;

    @Column(name = "SECURITY_RULE_USAGE", length = 20)
    private int securityRuleUsage;

    @Column(name = "PORT_QUOTA", length = 20)
    private int portQuota;

    @Column(name = "PORT_USAGE", length = 20)
    private int portUsage;

    @Column(name = "ROUTER_QUOTA", length = 20)
    private int routerQuota;

    @Column(name = "ROUTER_USAGE", length = 20)
    private int routerUsage;

}
