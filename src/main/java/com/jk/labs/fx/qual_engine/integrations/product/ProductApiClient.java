package com.jk.labs.fx.qual_engine.integrations.product;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;

public interface ProductApiClient {

    ProductApiResp getProductInfosByBookCodes(FxQualReq fxQualReq, FxQualResp fxQualResp);
}
