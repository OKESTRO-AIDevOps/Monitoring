package com.okestro.symphony.dashboard.api.openstack.keystone.project.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class UsageComputeVo {

    private int instanceUsed;
    private int coreUsed;
    private int ramUsed;

    private int metadataItemsUsed;
    private int injectFilesUsed;
    private int injectedFileContentBytesUsed;
    private int injectedFilePathBytesUsed;
    private int keyPairsUsed;

}
