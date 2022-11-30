package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class QuotaComputeVo {
    private int instanceQuota;
    private int coreQuota;
    private int ramQuota;

    // 프로젝트 쿼터 셋에 필요한 추가 정보
    private int metadataItemsQuota;
    private int injectFilesQuota;
    private int injectedFileContentBytesQuota;
    private int injectedFilePathBytesQuota;
    private int keyPairsQuota;

}
