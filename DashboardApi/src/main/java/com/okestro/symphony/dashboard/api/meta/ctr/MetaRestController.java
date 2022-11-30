package com.okestro.symphony.dashboard.api.meta.ctr;

import com.okestro.symphony.dashboard.api.meta.model.HostDTO;
import com.okestro.symphony.dashboard.api.meta.model.ProviderDTO;
import com.okestro.symphony.dashboard.api.meta.model.VmDTO;
import com.okestro.symphony.dashboard.api.meta.svc.MetaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@Api(tags = {"MetaRestController"})
@SwaggerDefinition(tags = {
    @Tag(name = "MetaRestController", description = "AI분석 위해 메타 데이터 제공")
})
@RequestMapping("/meta")
@RestController
public class MetaRestController {

    private MetaService metaService;

    @Autowired
    MetaRestController(MetaService metaService) {
        this.metaService = metaService;
    }

    @ApiOperation(value = "providerInfoList", notes = "provider 정보 제공")
    @GetMapping("/providers")
    public List<ProviderDTO> providerInfoList() {
        return metaService.providerInfoList();
    }

    @ApiOperation(value = "vmInfoList", notes = "vm 정보 제공")
    @GetMapping("/vms")
    public List<VmDTO> vmInfoList(@RequestParam String prvdId) {
        return metaService.vmInfoList(prvdId);
    }

    @ApiOperation(value = "hostInfoList", notes = "host 정보 제공")
    @GetMapping("/hosts")
    public List<HostDTO> hostInfoList(@RequestParam String prvdId) {
        return metaService.hostInfoList(prvdId);
    }

    @ApiOperation(value = "metaInfoList", notes = "meta 전체 정보 제공")
    @GetMapping("/metas")
    public LinkedHashMap<String, Object> metaInfoList(@RequestParam String prvdId) {
        return metaService.metaInfoList(prvdId);
    }

}
