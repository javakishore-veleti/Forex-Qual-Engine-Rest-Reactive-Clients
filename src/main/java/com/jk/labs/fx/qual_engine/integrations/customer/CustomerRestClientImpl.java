package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class CustomerRestClientImpl implements CustomerApiClient {

    @Autowired
    @Qualifier("customerRestClient")
    private RestClient customerRestClient;

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx fxQualExecCtx) {
        String customerId = fxQualExecCtx.getQualReq().getCustomerId();
        log.info("Calling Customer API for customerId={}", customerId);

        CustomerApiResp resp = customerRestClient
                .get()
                .uri("/{id}", customerId)
                .retrieve()
                .body(CustomerApiResp.class);

        log.info("Customer API Response: {}", resp);
        return resp;
    }
}
