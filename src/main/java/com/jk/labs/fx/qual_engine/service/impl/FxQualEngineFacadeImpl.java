package com.jk.labs.fx.qual_engine.service.impl;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.FxQualClient;
import com.jk.labs.fx.qual_engine.integrations.FxQualClientFactory;
import com.jk.labs.fx.qual_engine.telemetry.OtelSpanUtil;
import com.jk.labs.fx.qual_engine.telemetry.metrics.FxQualMetrics;
import com.jk.labs.fx.qual_engine.service.FxQualEngineFacade;
import com.jk.labs.fx.qual_engine.util.QuoteRequestExecution;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxQualEngineFacadeImpl implements FxQualEngineFacade {

    private final FxQualClientFactory clientFactory;
    private final FxQualMetrics metrics;
    private final Tracer tracer;
    private final OtelSpanUtil spanUtil;

    @QuoteRequestExecution
    @Override
    public int qualify(FxQualExecCtx ctx) {
        log.info("qualify: Starting with client type {}", ctx.getClientType());

        // ROOT SPAN (important!)
        Span root = tracer.spanBuilder("fxqual.qualify")
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        spanUtil.enrichRootSpan(root, ctx.getQualReq(), ctx.getQualResp().getClientType());

        // makeCurrent() ensures all downstream spans become children
        //noinspection unused
        try (var scope = root.makeCurrent()) {

            metrics.recordQuoteRequested();

            // --- STRATEGY RESOLUTION ---
            FxQualClient strategy = clientFactory.resolve(ctx.getClientType());

            // --- ACTUAL QUAL EXECUTION ---
            int result = strategy.qualify(ctx);

            // --- RECORD METRICS ---
            var req = ctx.getQualReq();
            metrics.recordQuoteQualified();
            metrics.recordNotional(req.getFromCurrency() + req.getToCurrency(), req.getQuantity());

            if (ObjectUtils.isNotEmpty(req.getPromoCodes())) {
                req.getPromoCodes().forEach(metrics::recordPromoUsage);
            }

            return result;

        } catch (Exception ex) {
            root.recordException(ex);
            root.setAttribute("fxqual.error", true);
            log.error("qualify: Error during execution", ex);
            throw ex;
        } finally {
            // --- END ROOT SPAN ---
            root.end();

            // --- TIMINGS UPDATED HERE ---
            var resp = ctx.getQualResp();
            resp.setExecEndDateTime(new Date());
            resp.setEndTime(System.currentTimeMillis());
            resp.setTimeTaken(resp.getEndTime() - resp.getStartTime());

            log.info("qualify: Completed. Total time={} ms", resp.getTimeTaken());
        }
    }
}
