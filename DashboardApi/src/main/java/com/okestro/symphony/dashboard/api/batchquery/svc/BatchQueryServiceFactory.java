package com.okestro.symphony.dashboard.api.batchquery.svc;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BatchQueryServiceFactory {
    private final Map<String, BatchQueryService> batchQueryServiceMap;

    public BatchQueryServiceFactory(List<BatchQueryService> batchQueryServices) {
        batchQueryServiceMap = batchQueryServices.stream().collect(Collectors.toMap(BatchQueryService::getServiceCode, Function.identity()));
    }

    public BatchQueryService getService(String svcCode) {
        return batchQueryServiceMap.get(svcCode);
    }
}
