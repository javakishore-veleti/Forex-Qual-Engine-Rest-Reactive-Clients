package com.jk.labs.fx.qual_engine.api;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import com.jk.labs.fx.qual_engine.service.FxQualEngineFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
    @SuppressWarnings("NullableProblems")
    @PostMapping("/qualify")
    public ResponseEntity<FxQualResp> qualify(
            @RequestParam(name = "clientType", defaultValue = "rest_client") String clientType,
            @RequestBody FxQualReq req
    ) {
        FxQualResp resp = new FxQualResp();

        FxQualExecCtx ctx = new FxQualExecCtx();
        ctx.getQualResp().setClientType(clientType);

        fxQualEngineFacade.qualify(ctx);

        resp.setExecEndDateTime(new Date());
        resp.setEndTime(System.currentTimeMillis());
        resp.setTimeTaken(resp.getEndTime() - resp.getStartTime());

        return ResponseEntity.ok(resp);
    }
}
