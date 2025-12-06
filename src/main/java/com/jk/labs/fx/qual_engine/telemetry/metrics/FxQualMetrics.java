package com.jk.labs.fx.qual_engine.telemetry.metrics;

import io.micrometer.core.instrument.*;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class FxQualMetrics {

    private final MeterRegistry registry;

    private final Counter quotesRequested;
    private final Counter quotesQualified;

    public FxQualMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.quotesRequested = Counter.builder("fxqual_quotes_requested_total")
                .description("Total number of quotes requested")
                .register(registry);

        this.quotesQualified = Counter.builder("fxqual_quotes_qualified_total")
                .description("Total number of quotes successfully qualified")
                .register(registry);
    }

    // --------------------------------------------------------------------
    // 1. SIMPLE COUNTERS
    // --------------------------------------------------------------------
    public void recordQuoteRequested() { quotesRequested.increment(); }

    public void recordQuoteQualified() { quotesQualified.increment(); }

    public void recordPromoUsage(String promoCode) {
        Counter.builder("fxqual_promo_usage_total")
                .description("Total number of promo validations")
                .tag("promo", promoCode)
                .register(registry)
                .increment();
    }

    public void recordNotional(String currencyPair, double notional) {
        DistributionSummary.builder("fxqual_volume_notional")
                .description("Trade notional value by currency pair")
                .baseUnit("USD")
                .tag("pair", currencyPair)
                .register(registry)
                .record(notional);
    }

    // --------------------------------------------------------------------
    // 2. **METHOD INVOCATION COUNTER** (new)
    // --------------------------------------------------------------------
    public Counter invocationCounter(String methodName) {
        return Counter.builder("fxqual_method_invocations_total")
                .description("Java method invocation counter")
                .tag("method", methodName)
                .register(registry);
    }

    // --------------------------------------------------------------------
    // 3. **WORKFLOW TIMER** (new)
    // --------------------------------------------------------------------
    public Timer workflowTimer(String clientType) {
        return Timer.builder("fxqual_workflow_duration_ms")
                .description("Full workflow execution time")
                .tag("clientType", clientType)
                .register(registry);
    }

    // --------------------------------------------------------------------
    // 4. **STEP TIMER** (per API call)
    // --------------------------------------------------------------------
    public Timer stepTimer(String stepName) {
        return Timer.builder("fxqual_step_duration_ms")
                .description("Downstream service call duration")
                .tag("step", stepName)
                .register(registry);
    }
}
