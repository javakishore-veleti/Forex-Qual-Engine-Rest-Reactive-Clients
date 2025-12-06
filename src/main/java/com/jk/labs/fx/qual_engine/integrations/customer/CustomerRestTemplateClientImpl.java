package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class CustomerRestTemplateClientImpl implements CustomerApiClient {

    @Qualifier("customerRestTemplate")
    private RestTemplate customerRestTemplate;

    @Qualifier("customerBaseUrl")
    private String customerBaseUrl;

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx fxQualExecCtx) {
        String customerId = fxQualExecCtx.getQualReq().getCustomerId();
        String url = customerBaseUrl + "/" + customerId;

        log.info("Calling Customer API via RestTemplate: {}", url);

        CustomerApiResp resp = customerRestTemplate.getForObject(url, CustomerApiResp.class);

        log.info("Customer API Response (RestTemplate): {}", resp);

        return resp;
    }
}

