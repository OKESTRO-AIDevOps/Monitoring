package com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "instances")
public class InstancesEntity implements Serializable {
    @Id
    @Column
    private Integer id;
    @Column(name = "access_ip_v4", length = 39)
    private String accessIpV4;
    @Column(name = "access_ip_v6", length = 39)
    private String accessIpV6;
    @Column(name = "architecture", length = 255)
    private String architecture;
    @Column(name = "auto_disk_config")
    private Integer autoDiskConfig;
    @Column(name = "availability_zone",length = 255)
    private String availabilityZone;
    @Column(name = "cell_name", length = 255)
    private String cellName;
    @Column(name = "cleaned")
    private Integer cleaned;
    @Column(name = "config_drive", length = 255)
    private String configDrive;
    @Column(name = "created_at")//datetime
    private Timestamp createdAt;


    @Column(name = "default_ephemeral_device", length = 255)
    private String defaultEphemeralDevice;
    @Column(name = "default_swap_device", length = 255)
    private String defaultSwapDevice;
    @Column(name = "deleted")
    private String deleted;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
    @Column(name = "disable_terminate")
    private Integer disableTerminate;
    @Column(name = "display_description", length = 255)
    private String displayDescription;
    @Column(name = "display_name", length = 255)
    private String displayName;
    @Column(name = "ephemeral_gb")
    private Integer ephemeralGb;
    @Column(name = "ephemeral_key_uuid", length = 36)
    private String epemeralkeyUuid;
    @Column(name = "hidden")
    private Integer hidden;
    @Column(name = "host", length = 255)
    private String host;
    @Column(name = "hostname", length = 255)
    private String hostname;
    @Column(name = "image_ref", length = 255)
    private String imageRef;
    @Column(name = "instance_type_id")
    private Integer instanceTypeId;
    @Column(name = "internal_id")// Can not set int field to null value << The primitive datatype int isn't nullable.
    private Integer internalId;
    @Column(name = "kernel_id", length = 255)
    private String kernel_id;
    @Column(name = "key_data") //mediumtext
    private String keyData;
    @Column(name = "key_name", length = 255)
    private String keyName;
    @Column(name = "launch_index")
    private Integer launchIndex;
    @Column(name = "launched_at")//datetime
    private String launchedAt;
    @Column(name = "locked")
    private Integer locked;
    @Column(name = "locked_by")//enum(owner, admin)
    private String lockedBy;
//    @Column(name = "memory_mb")
//    private Integer memoryMb;
    @Column(name = "node", length = 255)
    private String node;
    @Column(name = "os_type", length = 255)
    private String osType;
    @Column(name = "power_state")
    private Integer powerState;
    @Column(name = "progress")
    private Integer progress;
    @Column(name = "project_id", length = 255)
    private String projectId;
    @Column(name = "ramdisk_id", length = 255)
    private String ramdiskId;
    @Column(name = "reservation_id", length = 255)
    private String reservationId;
    @Column(name = "root_device_name", length = 255)
    private String rootDeviceName;
    @Column(name = "root_gb")
    private Integer rootGb;
    @Column(name = "shutdown_terminate")
    private Integer shutdownTerminate;
    @Column(name = "task_state", length = 255)
    private String taskState;
    @Column(name = "terminated_at")
    private Timestamp terminatedAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "user_data") //mediumtext
    private String userData;
    @Column(name = "user_id", length = 255)
    private String userId;
    @Column(name = "uuid", length = 36)
    private String uuid;
    @Column(name = "vcpus")
    private Integer vcpus;
    @Column(name = "vm_mode", length = 255)
    private String vmMode;
    @Column(name = "vm_state", length = 255)
    private String vmState;

}
