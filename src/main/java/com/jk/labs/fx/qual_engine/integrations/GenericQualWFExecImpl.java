package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.customer.CustomerApiClient;
import com.jk.labs.fx.qual_engine.integrations.market_data.FxInterestApiClient;
import com.jk.labs.fx.qual_engine.integrations.product.ProductApiClient;
import com.jk.labs.fx.qual_engine.integrations.promo.PromoApiClient;
import com.jk.labs.fx.qual_engine.telemetry.metrics.FxQualMetrics;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import static com.jk.labs.fx.qual_engine.util.AppConstants.ACT_SUCCESS;

@Slf4j
@Setter
@Getter
public class GenericQualWFExecImpl<C extends CustomerApiClient,
        PR extends PromoApiClient,
        P extends ProductApiClient,
        FXI extends FxInterestApiClient> {

    private C customerClient;
    private PR promoClient;
    private P productClient;
    private FXI fxInterestApiClient;

    @Autowired
    private FxQualMetrics metrics;

    public int qualifyWfExecution(FxQualExecCtx ctx, String clientImplName) {

        log.info("qualify: Starting clientImplName {}", clientImplName);

        // method invocation
        metrics.invocationCounter("GenericQualWFExecImpl.qualifyWfExecution").increment();

        // WORKFLOW TIMER START
        Timer.Sample workflowSample = Timer.start(metrics.getRegistry());

        try {
            // CUSTOMER STEP
            var customer = metrics.stepTimer("customer")
                    .record(() -> customerClient.validateCustomerId(ctx));
            ctx.getQualResp().addCtxData("CustomerApiResp", customer);

            // PROMO STEP
            var promo = metrics.stepTimer("promo")
                    .record(() -> promoClient.validateFxPromoCodes(ctx));
            ctx.getQualResp().addCtxData("PromoApiResp", promo);

            // PRODUCT STEP
            var product = metrics.stepTimer("product")
                    .record(() -> productClient.getProductInfosByBookCodes(ctx));
            ctx.getQualResp().addCtxData("ProductApiResp", product);

            // FX INTEREST STEP
            var rate = metrics.stepTimer("fxInterest")
                    .record(() -> fxInterestApiClient.getMarketInterestForCurrencies(ctx));
            ctx.getQualResp().addCtxData("FxInterestApiResp", rate);

            log.info("qualify: Exiting clientImplName {}", clientImplName);
            return ACT_SUCCESS;

        } finally {

            // WORKFLOW TIMER END
            workflowSample.stop(
                    metrics.workflowTimer(clientImplName)
            );
        }
    }
}
