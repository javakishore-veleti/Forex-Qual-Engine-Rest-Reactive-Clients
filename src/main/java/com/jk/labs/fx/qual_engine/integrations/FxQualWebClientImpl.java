package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerWebClientImpl;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestWebClientImpl;
import com.jk.labs.fx.qual_engine.integrations.product.ProductWebClientImpl;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoWebClientImpl;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component("webClientStrategy")
@RequiredArgsConstructor
@Slf4j
public class FxQualWebClientImpl extends GenericQualWFExecImpl<CustomerWebClientImpl, PromoWebClientImpl, ProductWebClientImpl, FxInterestWebClientImpl> implements FxQualClient {

    private final CustomerWebClientImpl customerClient;
    private final PromoWebClientImpl promoClient;
    private final ProductWebClientImpl productClient;
    private final FxInterestWebClientImpl fxInterestApiClient;

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
        return "FxQualWebClientImpl";
    }
}