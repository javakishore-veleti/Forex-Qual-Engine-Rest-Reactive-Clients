package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;

public interface CustomerApiClient {

    CustomerApiResp validateCustomerId(FxQualExecCtx fxQualExecCtx);
}
