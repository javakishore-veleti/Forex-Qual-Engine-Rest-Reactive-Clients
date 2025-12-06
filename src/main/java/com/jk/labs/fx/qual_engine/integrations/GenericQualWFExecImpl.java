package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerApiClient;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestApiClient;
import com.jk.labs.fx.qual_engine.integrations.product.ProductApiClient;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoApiClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.jk.labs.fx.qual_engine.util.AppConstants.ACT_SUCCESS;

@Slf4j
@Setter
@Getter
public class GenericQualWFExecImpl<C extends CustomerApiClient, PR extends PromoApiClient, P extends ProductApiClient, FXI extends FxInterestApiClient> {

    private C customerClient;
    private PR promoClient;
    private P productClient;
    private FXI fxInterestApiClient;

    public int qualifyWfExecution(FxQualExecCtx ctx, String clientImplName) {
        log.info("qualify: Starting clientImplName {}", clientImplName);

        var customer = customerClient.validateCustomerId(ctx);
        ctx.getQualResp().addCtxData("CustomerApiResp", customer);

        var promo = promoClient.validateFxPromoCodes(ctx);
        ctx.getQualResp().addCtxData("ProductApiResp", promo);

        var product = productClient.getProductInfosByBookCodes(ctx);
        ctx.getQualResp().addCtxData("ProductApiResp", product);

        var rate = fxInterestApiClient.getMarketInterestForCurrencies(ctx);
        ctx.getQualResp().addCtxData("FxInterestApiResp", rate);

        log.info("qualify: Exiting clientImplName {}", clientImplName);
        return ACT_SUCCESS;
    }
}
