package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PromoRestTemplateImpl implements PromoApiClient {

    @Qualifier("promoRestTemplate")
    private RestTemplate promoRestTemplate;

    @Qualifier("promoBaseUrl")
    private String customerBaseUrl;

    @Override
    public PromoApiResp validateFxPromoCodes(FxQualExecCtx ctx) {
        String promoCodesCSV = String.join(",", ctx.getQualReq().getPromoCodes());
        String url = customerBaseUrl + "/validate?promoCodes=" + promoCodesCSV;

        log.info("Calling Promo API via RestTemplate: {}", url);

        PromoApiResp resp = promoRestTemplate.getForObject(url, PromoApiResp.class);

        log.info("Promo API Response (RestTemplate): {}", resp);

        return resp;
    }
}
