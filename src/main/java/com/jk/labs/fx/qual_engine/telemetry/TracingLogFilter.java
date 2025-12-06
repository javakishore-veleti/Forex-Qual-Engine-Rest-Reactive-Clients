package com.jk.labs.fx.qual_engine.telemetry;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class TracingLogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        try {
            SpanContext ctx = Span.current().getSpanContext();
            if (ctx.isValid()) {
                MDC.put("traceId", ctx.getTraceId());
                MDC.put("spanId", ctx.getSpanId());
            }
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
        }
    }
}
