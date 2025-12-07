package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.telemetry.WebClientTracingWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("promoWebClient")
@RequiredArgsConstructor
@Slf4j
public class PromoWebClientImpl implements PromoApiClient {

    private final WebClientTracingWrapper tracing;

    @Value("${integrations.promo.base-url}")
    private String baseUrl; // Example: http://fx-qual-wiremock:8080/api/promo

    @Override
    public PromoApiResp validateFxPromoCodes(FxQualExecCtx ctx) {

        String customerId = ctx.getQualReq().getCustomerId();
        String promoCodesCSV = String.join(",", ctx.getQualReq().getPromoCodes());

        String url = baseUrl + "/validate?promoCodes=" + promoCodesCSV;

        log.info("Calling Promo API via WebClient: {}", url);

        PromoApiResp resp =
                tracing.get(url, PromoApiResp.class, "promo-service")
                        .block(); // OK because your service is synchronous

        log.info("Promo API Response (WebClient): {}", resp);

        return resp;
    }
}
