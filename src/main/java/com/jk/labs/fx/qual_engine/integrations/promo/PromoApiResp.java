package com.jk.labs.fx.qual_engine.integrations.promo;

import lombok.Data;

import java.util.List;

@Data
public class PromoApiResp {

    private List<String> validPromoCodes;
    private List<String> invalidPromoCodes;
}
