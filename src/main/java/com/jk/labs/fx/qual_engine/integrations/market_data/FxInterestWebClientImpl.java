package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("fxInterestWebClient")
@RequiredArgsConstructor
public class FxInterestWebClientImpl implements FxInterestApiClient {

    private final WebClientTracingWrapper tracing;

    @Override
    public FxInterestApiResp getMarketInterestForCurrencies(FxQualExecCtx ctx) {
        String url = "http://local-wiremock/market-data/" + ctx.getQualReq().getFromCurrency() + "/" + ctx.getQualReq().getToCurrency();
        return tracing.get(url, FxInterestApiResp.class, "market-data-service").block();

    }
}
