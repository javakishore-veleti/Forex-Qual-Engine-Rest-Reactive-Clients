package com.jk.labs.fx.qual_engine.integrations.product;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;

public interface ProductApiClient {

    ProductApiResp getProductInfosByBookCodes(FxQualExecCtx fxQualExecCtx);
}
