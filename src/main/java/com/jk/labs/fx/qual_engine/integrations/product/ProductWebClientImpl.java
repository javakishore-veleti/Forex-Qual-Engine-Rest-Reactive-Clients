package com.jk.labs.fx.qual_engine.integrations.product;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("productWebClient")
@RequiredArgsConstructor
public class ProductWebClientImpl  implements ProductApiClient {

    private final WebClientTracingWrapper tracing;

    @Override
    public ProductApiResp getProductInfosByBookCodes(FxQualExecCtx ctx) {
        String url = "http://local-wiremock/product/" + ctx.getQualReq().getTradingBookId();
        return tracing.get(url, ProductApiResp.class, "product-service").block();
    }
}