package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;

public interface FxInterestApiClient {

    FxInterestApiResp getMarketInterestForCurrencies(FxQualExecCtx fxQualExecCtx);
}
