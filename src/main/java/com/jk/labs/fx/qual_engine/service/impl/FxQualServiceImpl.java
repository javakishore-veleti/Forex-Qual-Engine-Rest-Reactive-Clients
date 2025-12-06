package com.jk.labs.fx.qual_engine.service.impl;

import com.jk.labs.fx.qual_engine.dto.FxQualExecCtx;
import com.jk.labs.fx.qual_engine.service.FxQualService;
import org.springframework.stereotype.Service;

import static com.jk.labs.fx.qual_engine.util.AppConstants.ACT_SUCCESS;

@Service
public class FxQualServiceImpl implements FxQualService {
    @Override
    public int qualify(FxQualExecCtx ctx) {
        return ACT_SUCCESS;
    }
}
