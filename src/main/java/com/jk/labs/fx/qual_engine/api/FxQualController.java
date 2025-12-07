package com.jk.labs.fx.qual_engine.api;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import com.jk.labs.fx.qual_engine.service.FxQualEngineFacade;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fx")
//@RequiredArgsConstructor
@Slf4j
public class FxQualController {

    private final Counter accessCounterRestClient;
    private final Counter accessCounterWebClient;
    private final Counter accessCounterRestTemplate;

    @Autowired
    private FxQualEngineFacade fxQualEngineFacade;

    public FxQualController(@Autowired MeterRegistry registry) {
        this.accessCounterRestClient = Counter.builder("fx_qual_metric_access_counter")
                .description("Counts accesses to the custom endpoint")
                .tag( "endpoint", "/api/fx/qualify?clientType=rest_client")
                .tag("clientType", "rest_client")
                .register(registry);

        this.accessCounterWebClient = Counter.builder("fx_qual_metric_access_counter")
                .description("Counts accesses to the custom endpoint")
                .tag( "endpoint", "/api/fx/qualify?clientType=web_client")
                .tag("clientType", "web_client")
                .register(registry);

        this.accessCounterRestTemplate = Counter.builder("fx_qual_metric_access_counter")
                .description("Counts accesses to the custom endpoint")
                .tag( "endpoint", "/api/fx/qualify?clientType=rest_template")
                .tag("clientType", "rest_template")
                .register(registry);
    }

    /**
     * Qualify an FX quote using the desired HTTP client strategy.
     *
     * @param clientType rest_template | rest_client | web_client
     * @param req qualification request body
     * @return FxQualResp
     */
    @Operation(
            summary = "Qualify FX Request",
            description = "Runs full downstream pipeline: customer, promo, product, market-data"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Qualification successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @SuppressWarnings("NullableProblems")
    @PostMapping("/qualify")
    public ResponseEntity<FxQualResp> qualify(
            @RequestParam(name = "clientType", defaultValue = "rest_client") String clientType,
            @RequestBody FxQualReq req
    ) {
        FxQualResp resp = new FxQualResp();

        FxQualExecCtx ctx = new FxQualExecCtx();
        ctx.setClientType(clientType);

        ctx.setQualReq(req);
        ctx.setQualResp(resp);

        ctx.getQualResp().setClientType(clientType);

        incrementAccessCounter(clientType);

        fxQualEngineFacade.qualify(ctx);

        // Aspect programming will update additional fields in resp object
        // Review QuoteWfExecTrackingAdvice class
        return ResponseEntity.ok(resp);
    }

    private void incrementAccessCounter(String clientType) {
        switch (clientType) {
            case "rest_client": {
                accessCounterRestClient.increment();
                log.info("accessCounterRestClient: clientType {}", clientType);
            } break;
            case "web_client": {
                accessCounterWebClient.increment();
                log.info("accessCounterWebClient: clientType {}", clientType);
            } break;
            case "rest_template": {
                accessCounterRestTemplate.increment();
                log.info("accessCounterRestTemplate: clientType {}", clientType);
            } break;
        }
    }
}
