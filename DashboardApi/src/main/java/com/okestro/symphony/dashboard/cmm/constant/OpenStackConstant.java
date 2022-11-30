/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.constant;

public class OpenStackConstant {

    // prefix temporary directory
    public static final String PREFIX_TEMP_DIR = "tf-";

    // prefix vault path
    public static final String PREFIX_VAULT = "/secret/data/";
    public static final String DATA = "data";
    public static final String PROJECT = "project";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ENDPOINT = "endpoint";
    public static final String DOMAIN = "domain";

    // endpoints
//    public static final String NOVA_COMPUTE = "https://89.98.80.115:5000/compute/v2.1";
//    public static final String OS_QUOTA_DEFAULT_SETS = "https://89.98.80.115:8774/v2.1";
//    public static final String OS_QUOTA_SETS = "https://89.98.80.115:8774";
//    public static final String KEYSTONE_IDENTITY = "https://89.98.80.115:5000/v3";
    public static final String NOVA_COMPUTE = "http://100.0.0.189:5000/compute/v2.1";
    public static final String OS_QUOTA_DEFAULT_SETS = "http://100.0.0.189:8774/v2.1";
    public static final String OS_QUOTA_SETS = "http://100.0.0.189:8774";
    public static final String KEYSTONE_IDENTITY = "http://100.0.0.189:5000/v3";


    // openstack metric type
    public static final String METRIC_INSTANCE = "instance";
    public static final String METRIC_INSTANCE_DISK = "instance_disk";
    public static final String METRIC_INSTANCE_NETWORK_INTERFACE = "instance_network_interface";

    // operation state
    public static final String SUCCEEDED = "SUCCEEDED";
    public static final String FAILED = "FAILED";

    // openstack filter
    public static final String OS_FILTER_IMAGE_LIMIT = "limit";
    public static final String OS_FILTER_IMAGE_NAME = "name";
    public static final String OS_FILTER_PROJECT_ID = "project_id";


    // result message
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";
    public static final String EXIST = "exist";
    public static final String FALSE ="false";
    public static final String TRUE ="true";

    // openstack network
    public static final String NETWORK_AVAILABLE_AREA = "nova";
    public static final String NETWORK_CREATE_SUBNET_NAME = "Subnet";
    public static final String NETWORK_CREATE_SUBNET_START_IP = "192.168.";
    public static final String NETWORK_CREATE_SUBNET_END_CIDR_IP = ".0/24";
    public static final String NETWORK_CREATE_SUBNET_GATEWAY_IP_END = ".1";

    // openstack network device_Owner
    public static final String COMPUTE_NOVA = "compute:nova";
    public static final String NETWORK_DHCP = "network:dhcp";
    public static final String NETWORK_ROUTER_INTERFACE = "network:router_interface";
    public static final String NETWORK_ROUTER_INTERFACE_DISTRBUTED = "network:router_interface_distributed";
    public static final String NETWORK_ROUTER_CENTRALIZED_SNAT  = "network:router_centralized_snat";
    public static final String NEUTRON_LOADBALANCERV2 = "neutron:LOADBALANCERV2";
    public static final String NETWORK_ROUTER_GATEWAY = "network:router_gateway";
    public static final String NETWORK_ACTIVE = "ACTIVE";

    // openstack baremetal api version
    public static final String BAREMETAL_API_VERSION = "1.38";   // queens version

    // portal user code
    public static final String PORTAL_ADMIN_GOVEE = "A01"; // 포털 관리자(공무원)
    public static final String PORTAL_ADMIN_GENERAL = "A11"; // 포털 관리자(일반 사용자)
    public static final String ORG_ADMIN_GOVEE = "A02"; // 입주기관 관리자(공무원)
    public static final String ORG_ADMIN_GENERAL = "A12"; // 입주기관 관리자(일반 사용자)
    public static final String PROJECT_ADMIN_GOVEE = "U02"; // 프로젝트 관리자(공무원)
    public static final String PROJECT_ADMIN_GENERAL = "U12"; // 프로젝트 관리자(일반 사용자)
    public static final String PROJECT_MEM_GENERAL = "U13"; // 프로젝트 멤버
    public static final String PROJECT_MEM_CLI = "U14"; // 프로젝트 멤버(CLI 사용 가능)
    public static final String LEAVE_USER = "E99"; // 탈퇴한(삭제된) 사용자

    // project status code
    public static final String PROJECT_CREATED = "PS01"; // 생성 완료

    // audit log action code(create, update, delete)
    public static final String AUDIT_LOG_CREATE = "C";
    public static final String AUDIT_LOG_UPDATE = "U";
    public static final String AUDIT_LOG_DELETE = "D";

    // websocket request msg
    public static final String WEBSOCKET_REQUEST_CREATING = "websocket.request.creating";
    public static final String WEBSOCKET_REQUEST_UPDATING = "websocket.request.updating";
    public static final String WEBSOCKET_REQUEST_DELETING = "websocket.request.deleting";
    public static final String WEBSOCKET_REQUEST_STATUS_CHANGE_SUCCESS = "websocket.status.change.success";
    public static final String WEBSOCKET_REQUEST_STATUS_CHANGING = "websocket.status.changing";
    public static final String WEBSOCKET_REQUEST_SUSPENDING = "websocket.request.suspending";
    public static final String WEBSOCKET_REQUEST_STARTING = "websocket.request.starting";
    public static final String WEBSOCKET_REQUEST_CONNECTING = "websocket.request.connecting";
    public static final String WEBSOCKET_REQUEST_DISCONNECTING = "websocket.request.disconnecting";
    public static final String WEBSOCKET_REQUEST_MEMBER_UPDATING = "websocket.request.member.updating";
    public static final String WEBSOCKET_REQUEST_MEMBER_UPDATED = "websocket.request.member.updated";
    public static final String WEBSOCKET_REQUEST_MEMBER_UPDATE_ERROR = "websocket.request.member.update.error";
    public static final String WEBSOCKET_REQUEST_SUSPENDED = "websocket.request.suspended";
    public static final String WEBSOCKET_REQUEST_STATUS_CHANGE_ERROR = "websocket.status.change.error";
    public static final String WEBSOCKET_REQUEST_STARTED = "websocket.request.started";
    public static final String WEBSOCKET_REQUEST_CREATING_WEBSOCKET = "websocket.request.creating.websocket";
    public static final String WEBSOCKET_REQUEST_CREATING_WEBSOCKET_ERROR = "websocket.request.creating.websocket.error";
    public static final String WEBSOCKET_REQUEST_ADDING_SECURITYGROUP_RULE = "websocket.request.adding.securitygroup.rule";
    public static final String WEBSOCKET_REQUEST_ADDED_SECURITYGROUP_RULE = "websocket.request.added.securitygroup.rule";
    public static final String WEBSOCKET_REQUEST_ADD_SECURITYGROUP_RULE_ERROR = "websocket.request.add.securitygroup.rule.error";
    public static final String WEBSOCKET_REQUEST_DELETING_SECURITYGROUP_RULE = "websocket.request.deleting.securitygroup.rule";
    public static final String WEBSOCKET_REQUEST_DELETED_SECURITYGROUP_RULE = "websocket.request.deleted.securitygroup.rule";
    public static final String WEBSOCKET_REQUEST_DELETE_SECURITYGROUP_RULE_ERROR = "websocket.request.delete.securitygroup.rule.error";
    public static final String WEBSOCKET_REQUEST_OPENSTACK_CONNECTION_ERROR = "websocket.request.connection.error";
    //	public static final String WEBSOCKET_REQUEST_OPENSTACK_CONNECTION_ERROR = "오픈스택 서버와 연결할 수 없습니다.";
    public static final String WEBSOCKET_REQUEST_OPENSTACK_SERVER_ERROR = "websocket.request.server.error";

    // audit message id
    public static final String AUDIT_MSG_ID_COMMON_CREATE_SUCCESS = "audit.common.create.success";
    public static final String AUDIT_MSG_ID_COMMON_UPDATE_SUCCESS = "audit.common.update.success";
    public static final String AUDIT_MSG_ID_COMMON_DELETE_SUCCESS = "audit.common.delete.success";
    public static final String AUDIT_MSG_ID_COMMON_STATUS_CHANGE_SUCCESS = "audit.common.status.change.success";
    public static final String AUDIT_MSG_ID_COMMON_CREATE_ERROR = "audit.common.create.error";
    public static final String AUDIT_MSG_ID_COMMON_UPDATE_ERROR = "audit.common.update.error";
    public static final String AUDIT_MSG_ID_COMMON_SATTUS_CHANGE_ERROR = "audit.common.status.change.error";
    public static final String AUDIT_MSG_ID_COMMON_DELETE_ERROR = "audit.common.delete.error";
    public static final String AUDIT_MSG_ID_COMMON_CONNECT_SUCCESS = "audit.common.connect.success";
    public static final String AUDIT_MSG_ID_COMMON_CONNECT_ERROR = "audit.common.connect.error";
    public static final String AUDIT_MSG_ID_COMMON_DISCONNECT_SUCCESS = "audit.common.disconnect.success";
    public static final String AUDIT_MSG_ID_COMMON_DISCONNECT_ERROR = "audit.common.disconnect.error";
    public static final String AUDIT_COMMON_CREATE_NULLPOINT_ERROR = "audit.common.create.nullpoint.error";
    public static final String AUDIT_COMMON_UPDATE_NULLPOINT_ERROR ="audit.common.update.nullpoint.error";
    public static final String AUDIT_COMMON_DELETE_NULLPOINT_ERROR ="audit.common.delete.nullpoint.error";

    public static final String AUDIT_MSG_ID_VM_NAME = "audit.vm.name";
    public static final String AUDIT_MSG_ID_KEYPAIR_NAME = "audit.keypair.name";
    public static final String AUDIT_MSG_ID_IMAGE_NAME = "audit.image.name";
    public static final String AUDIT_MSG_ID_NETWORK_NAME = "audit.network.name";
    public static final String AUDIT_MSG_ID_LOADBALANCER_NAME = "audit.loadbalancer.name";
    public static final String AUDIT_MSG_ID_FLOATINGIP_NAME = "audit.floatingip.name";
    public static final String AUDIT_MSG_ID_SECURITYGROUP_NAME = "audit.securitygroup.name";
    public static final String AUDIT_MSG_ID_ROUTER_NAME = "audit.router.name";
    public static final String AUDIT_MSG_ID_VOLUMN_NAME = "audit.volumn.name";
    public static final String AUDIT_MSG_ID_SNAPSHOT_NAME = "audit.snapshot.name";
    public static final String AUDIT_MSG_ID_OBJECTSTORAGE_NAME = "audit.objectstorage.name";
    public static final String AUDIT_MSG_ID_OBJECTSTORAGE_CONTAINER_NAME = "audit.objectstorage.container.name";
    public static final String AUDIT_MSG_ID_OBJECTSTORAGE_FILE_NAME = "audit.objectstorage.file.name";
    public static final String AUDIT_MSG_ID_OBJECTSTORAGE_FOLDER_NAME = "audit.objectstorage.folder.name";
    public static final String AUDIT_MSG_ID_INSTANCETYPE_NAME = "audit.instancetype.name";
    public static final String AUDIT_MSG_ID_PROJECT_NAME = "audit.project.name";
    public static final String AUDIT_MSG_ID_USER_NAME = "audit.user.name";
    public static final String AUDIT_MSG_ID_ORCHESTRATION_NAME = " audit.orchestration.name";



}
