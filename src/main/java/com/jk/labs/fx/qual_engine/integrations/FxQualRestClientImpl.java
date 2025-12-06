package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.product.ProductRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoRestClientImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.jk.labs.fx.qual_engine.util.AppConstants.ACT_SUCCESS;

@Component("restClientStrategy")
@RequiredArgsConstructor
public class FxQualRestClientImpl implements FxQualClient {

    private final CustomerRestClientImpl customerClient;
    private final PromoRestClientImpl promoClient;
    private final ProductRestClientImpl productClient;
    private final FxInterestRestClientImpl fxInterestApiClient;

    @Override
    public int qualify(FxQualExecCtx ctx) {

        var customer = customerClient.validateCustomerId(ctx);
        ctx.getQualResp().addCtxData("CustomerApiResp", customer);

        var promo = promoClient.validateFxPromoCodes(ctx);
        ctx.getQualResp().addCtxData("ProductApiResp", promo);

        var product = productClient.getProductInfosByBookCodes(ctx);
        ctx.getQualResp().addCtxData("ProductApiResp", product);

        var rate = fxInterestApiClient.getMarketInterestForCurrencies(ctx);
        ctx.getQualResp().addCtxData("FxInterestApiResp", rate);

        return ACT_SUCCESS;
    }
}
