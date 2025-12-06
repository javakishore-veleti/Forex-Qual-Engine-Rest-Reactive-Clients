package com.jk.labs.fx.qual_engine.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FxQualReq {

    private String channel;
    private String intent;
    private String customerId;
    // trading bood id = product id
    private String tradingBookId;

    private String fromCurrency;
    private String toCurrency;
    private long quantity;
    private List<String> promoCodes;

    public FxQualReq() {
        this.promoCodes = new ArrayList<>();
    }
}
