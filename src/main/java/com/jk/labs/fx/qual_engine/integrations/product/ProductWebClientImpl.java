package com.jk.labs.fx.qual_engine.integrations.product;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import org.springframework.stereotype.Component;

@Component
public class ProductWebClientImpl  implements ProductApiClient {

    @Override
    public ProductApiResp getProductInfosByBookCodes(FxQualExecCtx ctx) {
        return new ProductApiResp();
    }
}