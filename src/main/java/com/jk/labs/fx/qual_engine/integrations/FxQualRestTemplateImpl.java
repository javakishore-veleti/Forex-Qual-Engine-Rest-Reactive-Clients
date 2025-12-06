package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerRestTemplateClientImpl;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestRestTemplateImpl;
import com.jk.labs.fx.qual_engine.integrations.product.ProductRestTemplateImpl;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoRestTemplateImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.jk.labs.fx.qual_engine.util.AppConstants.ACT_SUCCESS;

@Component("restTemplateStrategy")
@RequiredArgsConstructor
public class FxQualRestTemplateImpl implements FxQualClient {

    private final CustomerRestTemplateClientImpl customerClient;
    private final PromoRestTemplateImpl promoClient;
    private final ProductRestTemplateImpl productClient;
    private final FxInterestRestTemplateImpl fxRateClient;

    @Override
    public int qualify(FxQualExecCtx fxQualExecCtx) {
        return ACT_SUCCESS;
    }

    @Override
    public String clientImplName() {
        return "FxQualRestTemplateImpl";
    }
}