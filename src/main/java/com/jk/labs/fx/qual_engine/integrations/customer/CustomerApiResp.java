package com.jk.labs.fx.qual_engine.integrations.customer;

import lombok.Data;

@Data
public class CustomerApiResp {

    private String customerId;
    private String customerName;
    private String customerEmail;
    private String customerStatus;
}
