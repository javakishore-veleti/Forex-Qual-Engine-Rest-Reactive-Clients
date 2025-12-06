package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FxInterestRestClientImpl implements FxInterestApiClient {

    @Override
    public FxInterestApiResp getMarketInterestForCurrencies(FxQualReq fxQualReq, FxQualResp fxQualResp) {
        return new FxInterestApiResp();
    }
}
