package com.jk.labs.fx.qual_engine.util;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.dto.FxQualResp;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class QuoteWfExecTrackingAdvice {

    @Around("@annotation(com.jk.labs.fx.qual_engine.util.QuoteRequestExecution)")
    public Object trackExecution(ProceedingJoinPoint pjp) throws Throwable {
        log.info("STARTED Tracking Execution of {}", pjp.getSignature().getName());

        // Assume Facade methods always receive FxQualExecCtx
        FxQualExecCtx ctx = (FxQualExecCtx) pjp.getArgs()[0];
        FxQualResp resp = ctx.getQualResp();

        resp.setStartTime(System.currentTimeMillis());
        resp.setExecStartDateTime(new Date());

        try {
            return pjp.proceed();   // execute business logic
        } finally {
            long end = System.currentTimeMillis();
            resp.setEndTime(end);
            resp.setExecEndDateTime(new Date(end));
            resp.setTimeTaken(end - resp.getStartTime());

            log.info("COMPLETED Tracking Execution - Quote Request Completed in {} ms", resp.getTimeTaken());
        }
    }
}
