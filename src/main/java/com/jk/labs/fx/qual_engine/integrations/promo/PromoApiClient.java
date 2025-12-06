package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import com.jk.labs.fx.qual_engine.integrations.product.ProductApiResp;

public interface PromoApiClient {

    ProductApiResp validateFxPromoCodes(FxQualReq fxQualReq, FxQualResp fxQualResp);
}
