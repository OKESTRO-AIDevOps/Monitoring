package com.okestro.symphony.dashboard.batch.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Data
@Table(name = "nova.compute_nodes")
public class NovaComputeNodesEntity implements Serializable {
    @Id
    @Column
    private Integer id;
    @Column(name = "cpu_allocation_ratio")
    private Float cpuAllocationRatio;
    @Column(name = "cpu_info")//mediumtext (json parsing 필요한가?)
    private String cpuInfo;
    @Column(name = "created_at") // data type : Timestamp가 맞을까?
    private Timestamp createdAt;
    @Column(name = "current_workload")
    private Integer currentWorkload;
    @Column(name = "deleted")
    private Integer deleted;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
    @Column(name = "disk_allocation_ratio")
    private Float diskAllocationRatio;
    @Column(name = "disk_available_least")
    private Integer distAvailableLeast;
    @Column(name = "extra_resources")
    private String extraResources;//text
    @Column(name = "free_disk_gb")
    private Integer freeDiskGb;
    @Column(name = "free_ram_mb")
    private Integer freeRamMb;
    @Column(name = "host", length = 255)
    private String host;
    @Column(name = "host_ip", length = 39)
    private String hostIp;
    @Column(name = "hypervisor_hostname", length = 255)
    private String hypervisorHostname;
    @Column(name = "hypervisor_type")//mediumtext
    private String hypervisorType;
    @Column(name = "hypervisor_version")
    private Integer hypervisorVersion;
    @Column(name = "local_gb")
    private Integer localGb;
    @Column(name = "local_gb_used")
    private Integer localGbUsed;
    @Column(name = "mapped")
    private Integer mapped;
    @Column(name = "memory_mb")
    private Integer memoryMb;
    @Column(name = "memory_mb_used")
    private Integer memoryMbUsed;
    @Column(name = "metrics")
    private String metrics;//text
    @Column(name = "numa_topology") //text
    private String numaTopology;
    @Column(name = "pci_stats") //text
    private String pciStats;
    @Column(name = "ram_allocation_ratio")//Float
    private Float ramAllocationRatio;
    @Column(name = "running_vms")
    private Integer runningVms;
    @Column(name = "service_id")
    private String serviceId;
    @Column(name = "stats") // text
//    @Convert(converter= StatsConverter.class)
//    private Stats stats;
    private String stats;
    @Column(name = "supported_instances")//text
    private String supportedInstances;
    @Column(name = "updated_at")//datetime
    private Timestamp updatedAt;
    @Column(name = "uuid", length = 36)
    private String uuid;
    @Column(name = "vcpus")
    private Integer vcpus;
    @Column(name = "vcpus_used")
    private Integer vcpusUsed;
}
