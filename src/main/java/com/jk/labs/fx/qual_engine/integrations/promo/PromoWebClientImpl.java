package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("promoWebClient")
@RequiredArgsConstructor
public class PromoWebClientImpl implements PromoApiClient {

    private final WebClientTracingWrapper tracing;

    @Override
    public PromoApiResp validateFxPromoCodes(FxQualExecCtx ctx) {
        String url = "http://local-wiremock/promo/" + ctx.getQualReq().getPromoCodes().get(0);
        return tracing.get(url, PromoApiResp.class, "promo-service").block();
    }
}
