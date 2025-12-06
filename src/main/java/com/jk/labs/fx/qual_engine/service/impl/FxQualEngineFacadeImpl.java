package com.jk.labs.fx.qual_engine.service.impl;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.integrations.FxQualClient;
import com.jk.labs.fx.qual_engine.integrations.FxQualClientFactory;
import com.jk.labs.fx.qual_engine.service.FxQualEngineFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FxQualEngineFacadeImpl implements FxQualEngineFacade {

    private final FxQualClientFactory clientFactory;

    @Override
    public int qualify(FxQualExecCtx ctx) {
        log.info("qualify: Starting with strategy client type {}", ctx.getClientType());

        // 1. Resolve strategy based on clientType
        FxQualClient clientStrategy = clientFactory.resolve(ctx.getClientType());

        // 2. Execute the qualification using the correct HTTP stack
        int strategyResp = clientStrategy.qualify(ctx);

        log.info("qualify: Exiting with strategy response {}", strategyResp);
        return strategyResp;
    }
}
