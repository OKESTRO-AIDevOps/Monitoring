package com.okestro.symphony.dashboard.api.batchquery.fcd;

import com.okestro.symphony.dashboard.api.batchquery.model.ReqBodyDTO;

public interface BatchQueryFacade {
    void startBatchQuery(ReqBodyDTO reqBodyDTO);
}
