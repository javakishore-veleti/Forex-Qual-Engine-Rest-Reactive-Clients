package com.jk.labs.fx.qual_engine.integrations.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductApiResp {

    private List<String> validBookCodes;
    private List<String> invalidBookCodes;
}
