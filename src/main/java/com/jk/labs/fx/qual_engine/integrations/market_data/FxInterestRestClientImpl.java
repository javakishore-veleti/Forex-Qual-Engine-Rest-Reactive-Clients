package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FxInterestRestClientImpl implements FxInterestApiClient {

    @Override
    public FxInterestApiResp getMarketInterestForCurrencies(FxQualExecCtx ctx) {
        return new FxInterestApiResp();
    }
}
