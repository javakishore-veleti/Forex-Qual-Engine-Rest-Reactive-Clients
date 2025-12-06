package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;

public interface CustomerApiClient {

    CustomerApiResp validateCustomerId(FxQualReq fxQualReq, FxQualResp fxQualResp);
}
