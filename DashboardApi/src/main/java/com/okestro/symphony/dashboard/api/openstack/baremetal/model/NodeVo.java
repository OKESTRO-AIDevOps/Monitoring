/*
 * Developed by bhhan@okestro.com on 2020-07-17
 * Last modified 2020-07-17 13:06:41
 */

package com.okestro.symphony.dashboard.api.openstack.baremetal.model;

import com.okestro.symphony.dashboard.cmm.model.CommonVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NodeVo extends CommonVo {
    private String uuid;
    private String name;
    private String instanceUuid;
    private String powerState;
    private String targetPowerState;
    private String lastError;
    private String provisionState;
    private String reservation;
    private String provisionUpdatedAt;
    private boolean maintenance;
    private String maintenanceReason;
    private String targetProvisionState;
    private boolean consoleEnabled;
    private String driver;
    private String resourceClass;
    private String chassisUuid;
    private String updatedAt;
    private String createdAt;

    private List<com.okestro.symphony.dashboard.api.openstack.baremetal.model.MapperVo> propertyList;
    private List<com.okestro.symphony.dashboard.api.openstack.baremetal.model.MapperVo> extraList;
    private com.okestro.symphony.dashboard.api.openstack.baremetal.model.DriverVo driverVo;

    private Map<String, Object> driverInfo;
    private Map<String, Object> instanceInfo;
    private Map<String, Object> properties;
    private Map<String, Object> extra;
}