package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.product.ProductRestClientImpl;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoRestClientImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("restClientStrategy")
@RequiredArgsConstructor
@Slf4j
public class FxQualRestClientImpl extends GenericQualWFExecImpl<CustomerRestClientImpl, PromoRestClientImpl, ProductRestClientImpl, FxInterestRestClientImpl> implements FxQualClient {

    private final CustomerRestClientImpl customerClient;
    private final PromoRestClientImpl promoClient;
    private final ProductRestClientImpl productClient;
    private final FxInterestRestClientImpl fxInterestApiClient;

    @PostConstruct
    public void setup() {
        super.setCustomerClient( customerClient);
        super.setPromoClient( promoClient);
        super.setProductClient( productClient);
        super.setFxInterestApiClient( fxInterestApiClient);
    }

    @Override
    public int qualify(FxQualExecCtx ctx) {
        return super.qualifyWfExecution(ctx, clientImplName());
    }

    @Override
    public String clientImplName() {
        return "FxQualRestClientImpl";
    }
}
