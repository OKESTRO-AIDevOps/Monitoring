/*
 * Developed by yg.kim2@okestro.com on 2021-02-24
 * Last modified 2021-02-24 17:21:01
 */

package com.okestro.symphony.dashboard.cmm.constant;

public class DatabaseConstant {

    /**
     * 1.  Symphony mariaDb 설정
     */
    public static final String MARIA_KEYSTONE_PROJECT_REPO = "com.okestro.symphony.dashboard.api.openstack.keystone.project.repo";
    public static final String MARIA_NOVA_COMPUTE_VM_REPO = "com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.repo";
    public static final String MARIA_KEYSTONE_HYPERVISOR_REPO = "com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.repo";
    public static final String MARIA_DASHBOARD_REPO = "com.okestro.symphony.dashboard.api.openstack.dashboard.repo";

    // ONLY BATCH
    public static final String MARIA_BATCH_AGGREGATE_REPO = "com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.aggregate.repo";
    public static final String MARIA_BATCH_HOST_REPO = "com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.host.repo";
    public static final String MARIA_BATCH_PROJECT_REPO = "com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.project.repo";
    public static final String MARIA_BATCH_PROJECT_QUOTA_REPO = "com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.quota.repo";
    public static final String MARIA_BATCH_VM_REPO = "com.okestro.symphony.dashboard.batch.collect.type.symphony.mariadb.vm.repo";

    public static final String MARIA_ENTITY_MANAGER_REF= "mainEntityManager";

    public static final String MARIA_TRANSACTION_MANAGER_REF = "mainTransactionManager";

    public static final String[] MARIA_ENTITY = {"com.okestro.symphony.dashboard.api.entity"
//            , "com.okestro.symphony.dashboard.api.openstack.keystone.project.model.entity"
//            , "com.okestro.symphony.dashboard.api.openstack.nova.compute.vm.model.entity"
//            , "com.okestro.symphony.dashboard.api.openstack.keystone.hypervisor.model.entity"
//            , "com.okestro.symphony.dashboard.api.openstack.dashboard.model.entity"
//            , "com.okestro.symphony.dashboard.batch.entity" // ONLY BATCH
    };

    /**
     * 2.  openstack mariaDb 설정
     */
    public static final String[] OST_BATCH_ENTITY = {
            "com.okestro.symphony.dashboard.batch.entity"
    };

    public static final String OST_BATCH_AGGREGATE_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.aggregate.repo";
    public static final String OST_BATCH_HOST_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.host.repo";
    public static final String OST_BATCH_HYPERVISOR_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.hypervisor.repo";
    public static final String OST_BATCH_PROJECT_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.project.repo";
    public static final String OST_BATCH_QUOTA_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.quota.repo";
    public static final String OST_BATCH_VM_REPO = "com.okestro.symphony.dashboard.batch.collect.type.openstack.mariadb.vm.repo";

    public static final String OST_BATCH_ENTITY_MANAGER_REF = "ostMariaEntityManager";

    public static final String OST_BATCH_TRANSACTION_MANAGER_REF = "ostMariaTransactionManager";
}
