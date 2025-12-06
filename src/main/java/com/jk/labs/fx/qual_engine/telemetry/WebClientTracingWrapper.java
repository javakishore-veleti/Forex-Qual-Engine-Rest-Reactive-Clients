package com.jk.labs.fx.qual_engine.telemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WebClientTracingWrapper {

    private final WebClient webClient;
    private final Tracer tracer;
    private final OtelSpanUtil spanUtil;

    public <T> Mono<T> get(String url, Class<T> type, String service) {

        Span span = tracer.spanBuilder("webclient.call." + service)
                .setSpanKind(SpanKind.CLIENT)
                .startSpan();

        spanUtil.enrichDownstreamSpan(span, service, url);

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(type)
                .doOnError(err -> {
                    span.recordException(err);
                    span.setAttribute("fxqual.error", true);
                })
                .doFinally(signal -> span.end());
    }
}
