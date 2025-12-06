package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerRestClientImpl implements CustomerApiClient {

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx fxQualExecCtx) {
        return new CustomerApiResp();
    }
}
