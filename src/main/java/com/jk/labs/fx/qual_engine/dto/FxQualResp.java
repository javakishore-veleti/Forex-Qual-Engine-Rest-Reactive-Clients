package com.jk.labs.fx.qual_engine.dto;

import lombok.Data;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

@Data
public class FxQualResp {

    private String threadName;
    // rest_template | rest_client | web_client
    private String clientType;

    private Long startTime;
    private Long endTime;
    private Long timeTaken;
    private Date execStartDateTime;
    private Date execEndDateTime;
    private Map<String, Object> ctxData;

    private List<Triple<String,Long, Long>> integrationsCallInfo;

    public FxQualResp() {
        this.ctxData = new LinkedHashMap<>();

        this.execStartDateTime = new Date();
        this.startTime = System.currentTimeMillis();
        this.endTime = System.currentTimeMillis();

        this.execStartDateTime = new Date(startTime);
        this.execEndDateTime = new Date(endTime);

        this.integrationsCallInfo = new ArrayList<>();
    }

    public void addIntegrationCallInfo(Triple<String,Long, Long> integrationCallInfo) {
        this.integrationsCallInfo.add(integrationCallInfo);
    }

    public void addCtxData(String key, Object value) {
        this.ctxData.put(key, value);
    }
}
