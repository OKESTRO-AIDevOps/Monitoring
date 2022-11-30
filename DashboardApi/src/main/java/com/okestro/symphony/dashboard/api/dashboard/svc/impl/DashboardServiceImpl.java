package com.okestro.symphony.dashboard.api.dashboard.svc.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okestro.symphony.dashboard.api.dashboard.model.MenuDTO;
import com.okestro.symphony.dashboard.api.dashboard.model.MenuInfo;
import com.okestro.symphony.dashboard.api.dashboard.svc.DashboardService;
import com.okestro.symphony.dashboard.api.entity.ProviderInfo;
import com.okestro.symphony.dashboard.api.dashboard.model.ProviderDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {
    private EntityManager entityManager;

    @Autowired
    DashboardServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<ProviderDTO> providerInfoList(String prvdType) {
        final String query = String.format("SELECT JSON_EXTRACT(JSON_DATA, CONCAT('$.', :TYPE)) AS JSON_DATA FROM T_PROVIDER WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER)");

        Query aQuery = (Query) entityManager.createNativeQuery(query);
        aQuery.setParameter("TYPE", prvdType);

        final String aData = aQuery.getSingleResult().toString();

        List<ProviderDTO> providerList = new ArrayList<>();

        Gson gson = new Gson();
        List<ProviderInfo> providerSrcList = gson.fromJson(aData, new TypeToken<ArrayList<ProviderInfo>>(){}.getType());

        for(ProviderInfo providerInfo: providerSrcList) {
            ProviderDTO provider = new ProviderDTO();
            provider.setType(prvdType);
            provider.setPrvdId(providerInfo.getId());

            providerList.add(provider);
        }

        return providerList;
    }

    @Override
    @Transactional
    public MenuDTO providerMenuList(String prvdId) {
        final String query = String.format("SELECT JSON_UNQUOTE(JSON_EXTRACT(JSON_KEYS(JSON_DATA), '$[0]')) AS JSON_KEY, JSON_EXTRACT(JSON_DATA, CONCAT('$.', JSON_UNQUOTE(JSON_EXTRACT(JSON_KEYS(JSON_DATA), '$[0]')))) AS JSON_DATA FROM T_PROVIDER_MENU WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER_MENU) AND PRVD_ID = :PRVD_ID");

        Query aQuery = (Query) entityManager.createNativeQuery(query);
        aQuery.setParameter("PRVD_ID", prvdId);

        final Object fData = aQuery.getResultList().stream().findFirst().orElse(null);
        final Object[] fArr = (Object[]) fData;

        final String prvdType = fArr[0].toString();
        final String aData = fArr[1].toString();

        Gson gson = new Gson();
        List<MenuInfo> menuInfoSrcList = gson.fromJson(aData, new TypeToken<ArrayList<MenuInfo>>(){}.getType());

        MenuDTO menuList = new MenuDTO();
        menuList.setType(prvdType);
        menuList.setId(prvdId);
        menuList.setList(menuInfoSrcList);

        return menuList;
    }
}
