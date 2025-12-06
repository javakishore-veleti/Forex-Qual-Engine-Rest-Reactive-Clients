package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("customerWebClient")
@RequiredArgsConstructor
public class CustomerWebClientImpl implements CustomerApiClient {

    private final WebClientTracingWrapper tracing;

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx ctx) {
        String url = "http://local-wiremock/customer/" + ctx.getQualReq().getCustomerId();
        return tracing.get(url, CustomerApiResp.class, "customer-service").block();
    }
}

