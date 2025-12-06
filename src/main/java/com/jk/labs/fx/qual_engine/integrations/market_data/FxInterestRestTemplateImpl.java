package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import org.springframework.stereotype.Component;

@Component
public class FxInterestRestTemplateImpl implements FxInterestApiClient {

    @Override
    public FxInterestApiResp getMarketInterestForCurrencies(FxQualExecCtx ctx) {
        return new FxInterestApiResp();
    }
}
