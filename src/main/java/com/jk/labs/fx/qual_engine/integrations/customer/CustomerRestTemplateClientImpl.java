package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import org.springframework.stereotype.Component;

@Component
public class CustomerRestTemplateClientImpl implements CustomerApiClient {

    @Override
    public CustomerApiResp validateCustomerId(FxQualReq fxQualReq, FxQualResp fxQualResp) {
        return new CustomerApiResp();
    }
}

