package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import org.springframework.stereotype.Component;

@Component
public class CustomerWebClientImpl implements CustomerApiClient {

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx fxQualExecCtx) {
        return new CustomerApiResp();
    }
}

