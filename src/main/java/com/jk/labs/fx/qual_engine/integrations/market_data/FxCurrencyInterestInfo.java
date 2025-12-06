package com.jk.labs.fx.qual_engine.integrations.market_data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FxCurrencyInterestInfo {

    private String fromCurrency;
    private String toCurrency;
    private BigDecimal interestRate;
}
