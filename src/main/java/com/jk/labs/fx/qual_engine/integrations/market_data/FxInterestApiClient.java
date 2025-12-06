package com.jk.labs.fx.qual_engine.integrations.market_data;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;

public interface FxInterestApiClient {

    FxInterestApiResp getMarketInterestForCurrencies(FxQualReq fxQualReq, FxQualResp fxQualResp);
}
