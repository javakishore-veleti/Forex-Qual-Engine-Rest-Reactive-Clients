package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import org.springframework.stereotype.Component;

@Component
public class FxInterestRestTemplateImpl implements FxInterestApiClient {

    @Override
    public FxInterestApiResp getMarketInterestForCurrencies(FxQualReq fxQualReq, FxQualResp fxQualResp) {
        return new FxInterestApiResp();
    }
}
