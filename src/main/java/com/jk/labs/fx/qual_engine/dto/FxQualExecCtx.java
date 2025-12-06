package com.jk.labs.fx.qual_engine.dto;

import lombok.Data;

@Data
public class FxQualExecCtx {

    private FxQualReq qualReq;
    private FxQualResp qualResp;

    // rest_template | rest_client | web_client
    private String clientType;

    // OpenID, JWT, OAuth, BasicAuth, AWSCognito, AzureEntraID
    private String clientAuthType;
}
