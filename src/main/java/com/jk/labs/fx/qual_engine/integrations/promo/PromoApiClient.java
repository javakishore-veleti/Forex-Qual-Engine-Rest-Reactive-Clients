package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.product.ProductApiResp;

public interface PromoApiClient {

    ProductApiResp validateFxPromoCodes(FxQualExecCtx fxQualExecCtx);
}
