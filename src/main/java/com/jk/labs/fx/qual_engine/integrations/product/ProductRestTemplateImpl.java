package com.jk.labs.fx.qual_engine.integrations.product;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import org.springframework.stereotype.Component;

@Component
public class ProductRestTemplateImpl  implements ProductApiClient {

    @Override
    public ProductApiResp getProductInfosByBookCodes(FxQualReq fxQualReq, FxQualResp fxQualResp) {
        return new ProductApiResp();
    }
}
