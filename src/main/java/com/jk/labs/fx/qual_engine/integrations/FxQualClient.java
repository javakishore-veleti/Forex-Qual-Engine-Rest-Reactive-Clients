package com.jk.labs.fx.qual_engine.integrations;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;

public interface FxQualClient {
    int qualify(FxQualExecCtx fxQualExecCtx);

    default String clientImplName() {
        return "FxQualClient";
    }
}
