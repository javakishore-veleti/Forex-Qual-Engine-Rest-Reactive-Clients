package com.jk.labs.fx.qual_engine.telemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestTemplateTracingWrapper {

    private final RestTemplate restTemplate;
    private final Tracer tracer;
    private final OtelSpanUtil spanUtil;

    public <T> T get(String url, Class<T> responseType, String service) {

        Span span = tracer.spanBuilder("resttemplate.call." + service)
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();

        spanUtil.enrichDownstreamSpan(span, service, url);

        try {
            return restTemplate.getForObject(url, responseType);
        } catch (Exception ex) {
            span.recordException(ex);
            span.setAttribute("fxqual.error", true);
            throw ex;
        } finally {
            span.end();
        }
    }
}
