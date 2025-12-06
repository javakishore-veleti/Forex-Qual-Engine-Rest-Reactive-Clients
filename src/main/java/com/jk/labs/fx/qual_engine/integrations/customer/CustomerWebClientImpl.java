package com.jk.labs.fx.qual_engine.integrations.customer;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("customerWebClient")
@RequiredArgsConstructor
@Slf4j
public class CustomerWebClientImpl implements CustomerApiClient {

    private final WebClientTracingWrapper tracing;

    @Value("${integrations.customer.base-url}")
    private String baseUrl; // Example: http://fx-qual-wiremock:8080/api/customer

    @Override
    public CustomerApiResp validateCustomerId(FxQualExecCtx ctx) {

        String customerId = ctx.getQualReq().getCustomerId();
        String url = baseUrl + "/" + customerId;

        log.info("Calling Customer API via WebClient: {}", url);

        CustomerApiResp resp =
                tracing.get(url, CustomerApiResp.class, "customer-service")
                        .block(); // OK because your service is synchronous

        log.info("Customer API Response (WebClient): {}", resp);

        return resp;
    }
}

