package com.okestro.symphony.dashboard.api.dashboard.ctr;

import com.okestro.symphony.dashboard.api.dashboard.model.MenuDTO;
import com.okestro.symphony.dashboard.api.dashboard.model.ReqParamDTO;
import com.okestro.symphony.dashboard.api.dashboard.svc.DashboardService;
import com.okestro.symphony.dashboard.api.dashboard.model.ProviderDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin("*")
@Api(tags = {"DashboardRestController"})
@SwaggerDefinition(tags = {
    @Tag(name = "DashboardRestController", description = "dashboard api")
})
@RequestMapping("/dashboard")
@RestController
public class DashboardRestController {
    private final DashboardService dashboardService;

    @Autowired
    DashboardRestController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @ApiOperation(value = "providerInfoList", notes = "provider 정보 제공")
    @PostMapping("/type")
    public List<ProviderDTO> providerInfoList(@RequestBody ReqParamDTO reqParamDTO) {
        return dashboardService.providerInfoList(reqParamDTO.getDashboardType());
    }

    @ApiOperation(value = "providerMenuList", notes = "provider menu 정보 제공")
    @PostMapping("/provider")
    public MenuDTO providerMenuList(@RequestBody ReqParamDTO reqParamDTO) {
        return dashboardService.providerMenuList(reqParamDTO.getPrvdId());
    }

}
