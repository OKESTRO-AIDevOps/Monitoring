package com.okestro.symphony.dashboard.api.dashboard.svc;

import com.okestro.symphony.dashboard.api.dashboard.model.MenuDTO;
import com.okestro.symphony.dashboard.api.dashboard.model.ProviderDTO;

import java.util.List;

public interface DashboardService {
    List<ProviderDTO> providerInfoList(String prvdType);

    MenuDTO providerMenuList(String prvdId);
}
