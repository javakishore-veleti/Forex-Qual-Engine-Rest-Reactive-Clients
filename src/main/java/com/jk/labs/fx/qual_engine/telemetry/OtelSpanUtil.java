package com.jk.labs.fx.qual_engine.telemetry;

import io.opentelemetry.api.trace.Span;
import com.jk.labs.fx.qual_engine.dto.FxQualReq;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OtelSpanUtil {

    private final Tracer tracer;

    public void enrichRootSpan(Span span, FxQualReq req, String clientType) {
        span.setAttribute("fxqual.client_type", clientType);
        span.setAttribute("fxqual.channel", req.getChannel());
        span.setAttribute("fxqual.intent", req.getIntent());
        span.setAttribute("fxqual.customer_id", req.getCustomerId());
        span.setAttribute("fxqual.from_currency", req.getFromCurrency());
        span.setAttribute("fxqual.to_currency", req.getToCurrency());
        span.setAttribute("fxqual.quantity", req.getQuantity());

        List<String> promoCodes = req.getPromoCodes();
        if (promoCodes != null && !promoCodes.isEmpty()) {
            String promoCsv = String.join(",", req.getPromoCodes());
            span.setAttribute("fxqual.promo_codes", promoCsv);
        } else {
            span.setAttribute("fxqual.promo_codes", "");
        }
    }

    public void enrichDownstreamSpan(Span span, String serviceName, String endpoint) {
        span.setAttribute("fxqual.downstream.service", serviceName);
        span.setAttribute("fxqual.downstream.endpoint", endpoint);
    }

    public Span startChildSpan(String spanName) {
        return tracer.spanBuilder(spanName).startSpan();
    }

    public void markDownstream(Span span, String service, String endpoint) {
        span.setAttribute("fxqual.downstream.service", service);
        span.setAttribute("fxqual.downstream.endpoint", endpoint);
    }

    public void markSuccess(Span span) {
        span.setAttribute("fxqual.downstream.status", "SUCCESS");
    }

    public void markFailure(Span span, Exception e) {
        span.setAttribute("fxqual.downstream.status", "FAILED");
        span.recordException(e);
    }
}
