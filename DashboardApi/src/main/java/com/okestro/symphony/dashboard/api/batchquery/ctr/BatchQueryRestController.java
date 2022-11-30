package com.okestro.symphony.dashboard.api.batchquery.ctr;

import com.okestro.symphony.dashboard.api.batchquery.fcd.BatchQueryFacade;
import com.okestro.symphony.dashboard.api.batchquery.model.ReqBodyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@Api(tags = {"BatchqueryRestController"})
@SwaggerDefinition(tags = {
    @Tag(name = "BatchqueryRestController", description = "batch query api")
})
@RequestMapping("/batch-query")
@RestController
public class BatchQueryRestController {
    private final BatchQueryFacade batchqueryFacade;

    @Autowired
    BatchQueryRestController(BatchQueryFacade batchqueryFacade) {
        this.batchqueryFacade = batchqueryFacade;
    }

    @ApiOperation(value = "batchQuery", notes = " 정보 제공")
    @PostMapping("/")
    public void batchQuery(@RequestBody ReqBodyDTO reqBodyDTO) {
        batchqueryFacade.startBatchQuery(reqBodyDTO);
    }
}
