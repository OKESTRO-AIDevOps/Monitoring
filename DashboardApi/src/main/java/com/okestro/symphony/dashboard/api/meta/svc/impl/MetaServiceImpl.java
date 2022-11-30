package com.okestro.symphony.dashboard.api.meta.svc.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okestro.symphony.dashboard.api.entity.ProviderInfo;
import com.okestro.symphony.dashboard.api.meta.model.HostDTO;
import com.okestro.symphony.dashboard.api.meta.model.ProviderDTO;
import com.okestro.symphony.dashboard.api.meta.model.VmDTO;
import com.okestro.symphony.dashboard.api.meta.svc.MetaService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Slf4j
@Service
@Transactional
public class MetaServiceImpl implements MetaService {

    private EntityManager entityManager;

    @Autowired
    MetaServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<ProviderDTO> providerInfoList() {
        final String query = String.format("SELECT CREATED_AT, JSON_KEYS(JSON_DATA) AS JSON_DATA FROM T_PROVIDER WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER)");

        Query aQuery = (Query) entityManager.createNativeQuery(query);

        final Object fData = aQuery.getResultList().stream().findFirst().orElse(null);
        final Object[] fArr = (Object[]) fData;

        final Long creatAt = Long.parseLong(fArr[0].toString());
        final String aData = fArr[1].toString();

        List<ProviderDTO> providerList = new ArrayList<>();
        Gson gson = new Gson();
        ArrayList<String> providerTypeList = gson.fromJson(aData, ArrayList.class);

        if (providerTypeList != null && providerTypeList.isEmpty() == false) {
            String pwd = "";
            for(String col: providerTypeList) {

                final String subQuery = String.format("SELECT JSON_EXTRACT(JSON_DATA, CONCAT('$.', :TYPE)) AS JSON_DATA FROM T_PROVIDER WHERE CREATED_AT = :CREATED_AT");

                Query bQuery = (Query) entityManager.createNativeQuery(subQuery);
                bQuery.setParameter("TYPE", col);
                bQuery.setParameter("CREATED_AT", creatAt);

                final String bData = bQuery.getSingleResult().toString();

                List<ProviderInfo> providerSrcList = gson.fromJson(bData, new TypeToken<ArrayList<ProviderInfo>>(){}.getType());

                for(ProviderInfo providerInfo: providerSrcList) {
                    ProviderDTO provider = new ProviderDTO();
                    provider.setId(providerInfo.getId());
                    provider.setType(col);

                    providerList.add(provider);
                }
            }
            return providerList;
        }
        else
            return null;
    }

    @Override
    @Transactional
    public List<VmDTO> vmInfoList(String prvdId) {
        final String query = String.format("SELECT JSON_EXTRACT(JSON_DATA, '$.vms') AS JSON_DATA FROM T_PROVIDER_META WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER_META) AND PRVD_ID = :PRVD_ID");

        Query aQuery = (Query) entityManager.createNativeQuery(query);
        aQuery.setParameter("PRVD_ID", prvdId);

        final String aData = aQuery.getSingleResult().toString();

        Gson gson = new Gson();

        List<VmDTO> vmDTOList = gson.fromJson(aData, new TypeToken<ArrayList<VmDTO>>(){}.getType());

        return vmDTOList;
    }

    @Override
    @Transactional
    public List<HostDTO> hostInfoList(String prvdId) {
        final String query = String.format("SELECT JSON_EXTRACT(JSON_DATA, '$.hosts') AS JSON_DATA FROM T_PROVIDER_META WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER_META) AND PRVD_ID = :PRVD_ID");

        Query aQuery = (Query) entityManager.createNativeQuery(query);
        aQuery.setParameter("PRVD_ID", prvdId);

        final String aData = aQuery.getSingleResult().toString();

        Gson gson = new Gson();

        List<HostDTO> hostDTOList = gson.fromJson(aData, new TypeToken<ArrayList<HostDTO>>() {
        }.getType());

        return hostDTOList;
    }

    @Override
    @Transactional
    public LinkedHashMap<String, Object> metaInfoList(String prvdId) {
        final String query = String.format("SELECT JSON_EXTRACT(JSON_DATA, '$[0]') AS JSON_DATA FROM T_PROVIDER_META WHERE CREATED_AT = (SELECT MAX(CREATED_AT) FROM T_PROVIDER_META) AND PRVD_ID = :PRVD_ID");

        Query aQuery = (Query) entityManager.createNativeQuery(query);
        aQuery.setParameter("PRVD_ID", prvdId);

        final String aData = aQuery.getSingleResult().toString();

        Gson gson = new Gson();

        LinkedHashMap<String, Object> metaData = gson.fromJson(aData, new TypeToken<LinkedHashMap<String, Object>>() {
        }.getType());

        return metaData;
    }

}
