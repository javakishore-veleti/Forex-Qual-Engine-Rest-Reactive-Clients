package com.jk.labs.fx.qual_engine.telemetry.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

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

    // ---------------------------------------------------------
    // SIMPLE COUNTERS
    // ---------------------------------------------------------
    public void recordQuoteRequested() {
        quotesRequested.increment();
    }

    public void recordQuoteQualified() {
        quotesQualified.increment();
    }

    // ---------------------------------------------------------
    // DYNAMIC COUNTER: promo usage (tag = promoCode)
    // ---------------------------------------------------------
    public void recordPromoUsage(String promoCode) {
        Counter.builder("fxqual_promo_usage_total")
                .description("Total number of promo validations")
                .tag("promo", promoCode)
                .register(registry)
                .increment();
    }

    // ---------------------------------------------------------
    // DISTRIBUTION SUMMARY (currency pair tagged)
    // ---------------------------------------------------------
    public void recordNotional(String currencyPair, double notional) {
        DistributionSummary.builder("fxqual_volume_notional")
                .description("Trade notional value by currency pair")
                .baseUnit("USD")
                .tag("pair", currencyPair)
                .register(registry)
                .record(notional);
    }
}
