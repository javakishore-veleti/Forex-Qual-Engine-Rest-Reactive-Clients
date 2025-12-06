package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import org.springframework.stereotype.Component;

@Component
public class PromoRestClientImpl implements PromoApiClient {
    @Override
    public PromoApiResp validateFxPromoCodes(FxQualExecCtx ctx) {
        return new PromoApiResp();
    }
}
