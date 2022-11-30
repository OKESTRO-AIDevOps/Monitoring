package com.okestro.symphony.dashboard.api.meta.svc;

import com.okestro.symphony.dashboard.api.meta.model.HostDTO;
import com.okestro.symphony.dashboard.api.meta.model.ProviderDTO;
import com.okestro.symphony.dashboard.api.meta.model.VmDTO;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface MetaService {

    List<ProviderDTO> providerInfoList();

    List<VmDTO> vmInfoList(String prvdId);

    List<HostDTO> hostInfoList(String prvdId);

    LinkedHashMap<String, Object> metaInfoList(String prvdId);
}
