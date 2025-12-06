package com.jk.labs.fx.qual_engine.integrations.market_data;

import lombok.Data;
import java.util.List;

@Data
public class FxInterestApiResp {

    private List<FxCurrencyInterestInfo> currencyInterestInfos;
}
