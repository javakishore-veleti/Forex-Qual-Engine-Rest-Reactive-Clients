package com.jk.labs.fx.qual_engine.api;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import com.jk.labs.fx.qual_engine.service.FxQualEngineFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fx")
@RequiredArgsConstructor
public class FxQualController {

    private final FxQualEngineFacade fxQualEngineFacade;

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

        fxQualEngineFacade.qualify(ctx);

        // Aspect programming will update additional fields in resp object
        // Review QuoteWfExecTrackingAdvice class
        return ResponseEntity.ok(resp);
    }
}
