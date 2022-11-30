package com.okestro.symphony.dashboard.api.batchquery.fcd.impl;

import com.okestro.symphony.dashboard.api.batchquery.fcd.BatchQueryFacade;
import com.okestro.symphony.dashboard.api.batchquery.model.ReqBodyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchQueryFacadeImpl implements BatchQueryFacade {
    public void startBatchQuery(ReqBodyDTO reqBodyDTO) {
        log.info("reqBodyDTO -> {}", reqBodyDTO);
    }
}
