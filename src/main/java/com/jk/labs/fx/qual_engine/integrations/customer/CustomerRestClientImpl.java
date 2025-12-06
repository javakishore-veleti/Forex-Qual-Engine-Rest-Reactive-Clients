package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerRestClientImpl implements CustomerApiClient {

    @Override
    public CustomerApiResp validateCustomerId(FxQualReq fxQualReq, FxQualResp fxQualResp) {
        return new CustomerApiResp();
    }
}
