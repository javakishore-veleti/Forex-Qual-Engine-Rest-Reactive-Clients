package com.jk.labs.fx.qual_engine.integrations.promo;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Slf4j
public class PromoRestClientImpl implements PromoApiClient {

    @Autowired
    @Qualifier("promoRestClient")
    private RestClient promoRestClient;

    @Override
    public PromoApiResp validateFxPromoCodes(FxQualExecCtx ctx) {

        PromoApiResp promoApiResp = new PromoApiResp();

        if(ObjectUtils.isEmpty(ctx.getQualReq().getPromoCodes())) {
            log.info("NOT Calling Customer API for customerId={}", ctx.getQualReq().getCustomerId());
            return promoApiResp;
        }

        log.info("Calling Customer API for customerId={}", ctx.getQualReq().getCustomerId());

        String promoCodesCSV = String.join(",", ctx.getQualReq().getPromoCodes());
        promoApiResp = promoRestClient
                .post()
                .uri("/validate", promoCodesCSV)
                .body(Map.of("promoCodes", ctx.getQualReq().getPromoCodes()))
                .retrieve()
                .body(PromoApiResp.class);

        log.info("Promo API Response: {}", promoApiResp);
        return promoApiResp;
    }
}
