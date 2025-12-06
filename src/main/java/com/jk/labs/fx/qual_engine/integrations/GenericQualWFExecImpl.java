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

        // ---------------------------------------------------------
        // 1️⃣ Track invocation count
        // ---------------------------------------------------------
        metrics.invocationCounter("GenericQualWFExecImpl.qualifyWfExecution").increment();

        // ---------------------------------------------------------
        // 2️⃣ Full workflow timer
        // ---------------------------------------------------------
        Timer.Sample workflowSample = Timer.start();

        try {
            // ---------------------------------------------------------
            // 3️⃣ Customer step timer
            // ---------------------------------------------------------
            var customer = metrics.stepTimer("customer")
                    .record(() -> customerClient.validateCustomerId(ctx));
            ctx.getQualResp().addCtxData("CustomerApiResp", customer);

            // ---------------------------------------------------------
            // 4️⃣ Promo step timer
            // ---------------------------------------------------------
            var promo = metrics.stepTimer("promo")
                    .record(() -> promoClient.validateFxPromoCodes(ctx));
            ctx.getQualResp().addCtxData("PromoApiResp", promo);

            // ---------------------------------------------------------
            // 5️⃣ Product step timer
            // ---------------------------------------------------------
            var product = metrics.stepTimer("product")
                    .record(() -> productClient.getProductInfosByBookCodes(ctx));
            ctx.getQualResp().addCtxData("ProductApiResp", product);

            // ---------------------------------------------------------
            // 6️⃣ FX Interest Market Data timer
            // ---------------------------------------------------------
            var rate = metrics.stepTimer("fxInterest")
                    .record(() -> fxInterestApiClient.getMarketInterestForCurrencies(ctx));
            ctx.getQualResp().addCtxData("FxInterestApiResp", rate);

            log.info("qualify: Exiting clientImplName {}", clientImplName);
            return ACT_SUCCESS;

        } finally {
            // ---------------------------------------------------------
            // 7️⃣ Stop workflow timer
            // ---------------------------------------------------------
            workflowSample.stop(
                    Timer.builder("fxqual_workflow_duration_ms")
                            .tag("clientImpl", clientImplName)
                            .register(metrics.getRegistry())
            );
        }
    }
}
