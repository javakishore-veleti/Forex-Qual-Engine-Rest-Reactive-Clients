package com.jk.labs.fx.qual_engine.telemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class RestClientTracingWrapper {

    private final RestClient restClient;
    private final Tracer tracer;
    private final OtelSpanUtil spanUtil;

    public <T> T get(String url, Class<T> type, String service) {

        Span span = tracer.spanBuilder("restclient.call." + service)
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();

        spanUtil.enrichDownstreamSpan(span, service, url);

        try {
            return restClient.get().uri(url).retrieve().body(type);
        } catch (Exception ex) {
            span.recordException(ex);
            span.setAttribute("fxqual.error", true);
            throw ex;
        } finally {
            span.end();
        }
    }
}
